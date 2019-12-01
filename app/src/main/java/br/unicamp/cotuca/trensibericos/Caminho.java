package br.unicamp.cotuca.trensibericos;

import java.util.Locale;
import java.util.Objects;

public class Caminho extends Hashable<Caminho, String> {
    private ListaSimples<Cidade> cidades;
    private int distancia, tempo;
    private boolean principal;

    public int getDistancia()
    {
        return distancia;
    }
    public void setDistancia(int d)
    {
        distancia = d;
    }

    public int getTempo()
    {
        return tempo;
    }
    public void setTempo(int t)
    {
        tempo = t;
    }

    public ListaSimples<Cidade> getCidades()
    {
        return cidades;
    }

    public Caminho()
    {
        cidades = new ListaSimples<Cidade>();
        tempo = distancia = -1;
    }

    public Caminho(Cidade c1, Cidade c2, int d, int t)
    {
        cidades = new ListaSimples<Cidade>();
        cidades.add(c1);
        cidades.add(c2);
        setDistancia(d);
        setTempo(t);
    }

    public Caminho(String linha, HashTable<Cidade, String> hashCidades)
    {
        cidades = new ListaSimples<Cidade>();
        String[] blocos = new String[(int)Math.ceil((float)linha.length()/15)];
        for (int i = 0; i < linha.length(); i += 15) {
            String atual = linha.substring(i, Math.min(linha.length(), i + 15));
            if (linha.length() - i > 15) {
                cidades.add(hashCidades.get(atual.trim()));
            } else {
                distancia = Integer.parseInt(atual.substring(0, 5).trim());
                tempo = Integer.parseInt(atual.substring(5).trim());
            }
        }
    }

    public String toString()
    {
        String format = "";
        Object[] args = new Object[2 + cidades.getSize()];
        int i;
        for (i = 0; i < cidades.getSize(); i++) {
            format += "%-15s";
            args[i] = cidades.get(i).getNome();
        }
        format += "%4s %4s";
        args[i++] = distancia;
        args[i] = tempo;
        return String.format(Locale.FRANCE, format, args);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Caminho)) return false;
        Caminho caminho = (Caminho) o;
        return getDistancia() == caminho.getDistancia() &&
                getTempo() == caminho.getTempo() &&
                getCidades().equals(caminho.getCidades());
    }

    public int hashCode() {
        return Objects.hash(getCidades(), getDistancia(), getTempo());
    }

    public boolean isPrincipal() {
        return principal;
    }
    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public String getKey()
    {
        return toString();
    }
}
