/**
 * @author 18178 - Felipe Scherer Vicentin
 * @author 18179 - Gustavo Miller Santos
 */


package br.unicamp.cotuca.trensibericos;

import java.util.Locale;

//classe do Caminho
public class Caminho{
    private ListaSimples<Cidade> cidades; //lista de cidades que pertencem a esse caminho
    private double distancia, tempo;

    public double getDistancia()
    {
        return distancia;
    }
    public void setDistancia(double d)
    {
        distancia = d;
    }

    public double getTempo()
    {
        return tempo;
    }
    public void setTempo(double t)
    {
        tempo = t;
    }

    public ListaSimples<Cidade> getCidades()
    {
        return cidades;
    }
    public void setCidades(ListaSimples<Cidade> cidades) {
        this.cidades = cidades;

    }

    public Caminho()
    {
        cidades = new ListaSimples<Cidade>();
        tempo = distancia = -1;
    }

    public Caminho(Cidade c1, Cidade c2, double d, double t)
    {
        cidades = new ListaSimples<Cidade>();
        cidades.add(c1);
        cidades.add(c2);
        setDistancia(d);
        setTempo(t);
    }

    public Caminho(ListaSimples<Cidade> cidades, double d, double t) {
        this.cidades = cidades;
        setDistancia(d);
        setTempo(t);
    }

    public Caminho(String linha, HashTable<Cidade, String> hashCidades) //construtor a partir de linha do arquivo texto
    {
        cidades = new ListaSimples<Cidade>();

        for (int i = 0; i < linha.length(); i += 15) {
            String atual = linha.substring(i, Math.min(linha.length(), i + 15)); //blocos de 15 caracteres são lidos da linha e armazenados em atual até que
            //não haja mais 15 caracteres para serem lidos

            //cada bloco de 15 caracteres representa o nome de uma cidade que deve ser pesquisado na HashTable passada como parâmetro para, depois, ser adicionada
            //à lista de cidades desse caminho

            if (linha.length() - i > 15) { //se houver mais 15 caracteres para serem lidos
                cidades.add(hashCidades.get(atual.trim()));
            } else { //se não houver, o que falta ler é a distância e tempo do caminho
                distancia = Integer.parseInt(atual.substring(0, 5).trim());
                tempo = Integer.parseInt(atual.substring(5).trim());
            }
        }
    }

    //caminho é formatado para ser escrito no arquivo texto
    public String toString()
    {
        String format = "";
        Object[] args = new Object[2 + cidades.getSize()];
        int i;

        for (i = 0; i < cidades.getSize(); i++) {
            format += "%-15s";
            args[i] = cidades.get(i).getNome();
        }

        format += "%-4s %-4s";

        args[i++] = Math.round(distancia);
        args[i] = Math.round(tempo);

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
}
