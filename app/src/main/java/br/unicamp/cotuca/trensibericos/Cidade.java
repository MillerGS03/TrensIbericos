/**
 * @author 18178 - Felipe Scherer Vicentin
 * @author 18179 - Gustavo Miller Santos
 */


package br.unicamp.cotuca.trensibericos;

import java.util.Locale;

//classe Cidade que herda de Hashable<String> para poder ser tipo válido da HashTable
public class Cidade extends Hashable<Cidade, String> {
    public final int comecoId = 0;
    public final int fimId = 2;
    public final int comecoNome = fimId;
    public final int fimNome = comecoNome + 16;
    public final int comecoX = fimNome;
    public final int fimX = comecoX + 6;
    public final int comecoY = fimX + 1;

    protected int id;
    protected String nome;
    protected float x, y;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String toString()
    {
        //id tem dois digitos, nome tem 16 e cordendas têm 3 casas decimais
        String s = String.format(Locale.FRANCE, "%2s%-16s%.3f %.3f", ""+getId(), getNome(), getX(), getY());
        return s;
    }

    public Cidade()
    {
        setId(-1);
        setNome("");
        setX(0);
        setY(0);
    }

    public Cidade(String linha)
    {
        setId(Integer.parseInt(linha.substring(comecoId, fimId).trim()));
        setNome(linha.substring(comecoNome, fimNome).trim());
        setX(Float.parseFloat(linha.substring(comecoX, fimX).trim().replace(',', '.'))); //replace feito para padronizar String
        setY(Float.parseFloat(linha.substring(comecoY).trim().replace(',', '.')));
    }

    public Cidade(int id, String nome, float x, float y)
    {
        setId(id);
        setNome(nome);
        setX(x);
        setY(y);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cidade)) return false;
        Cidade cidade = (Cidade) o;
        return getId() == cidade.getId() &&
                Double.compare(cidade.getX(), getX()) == 0 &&
                Double.compare(cidade.getY(), getY()) == 0 &&
                getNome().equals(cidade.getNome());
    }

    //métodos que devem ser implementados por Cidade herdar de Hashable<String>
    public String getKey()
    {
        return getNome();
    }

    public long getKeyHashcode(String hash) {
        long ret = 88;
        for (int i = 0; i < hash.length(); i++)
            ret = 7 * ret + hash.charAt(i);
        return ret;
    }
}
