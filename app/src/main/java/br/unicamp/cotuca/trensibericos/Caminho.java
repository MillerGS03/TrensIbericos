package br.unicamp.cotuca.trensibericos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Caminho implements Keyable {
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

    public String toString()
    {
        return String.format("%s --> %s [%d, %d]",
                cidades.get(0).getNome(), cidades.get(1).getNome(), getDistancia(), getTempo());
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
