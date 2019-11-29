package br.unicamp.cotuca.trensibericos;

import java.util.Arrays;
import java.util.Objects;

public class CidadeInfo {
    private Cidade[] cidades;
    private int distancia, tempo;

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

    public Cidade getCidade(int i)
    {
        return cidades[i];
    }
    public void setCidade(int i, Cidade cid)
    {
        cidades[i] = cid;
    }

    public CidadeInfo()
    {
        cidades = new Cidade[2];
        tempo = distancia = -1;
    }

    public CidadeInfo(Cidade c1, Cidade c2, int d, int t)
    {
        cidades = new Cidade[2];
        setCidade(0, c1);
        setCidade(1, c2);
        setDistancia(d);
        setTempo(t);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CidadeInfo)) return false;
        CidadeInfo that = (CidadeInfo) o;
        return getDistancia() == that.getDistancia() &&
                getTempo() == that.getTempo() &&
                Arrays.equals(cidades, that.cidades);
    }

    public int hashCode() {
        int result = Objects.hash(getDistancia(), getTempo());
        result = 31 * result + Arrays.hashCode(cidades);
        return result;
    }

    public String toString()
    {
        return String.format("%s --> %s [%d, %d]",
                getCidade(0).getNome(), getCidade(1).getNome(), getDistancia(), getTempo());
    }
}
