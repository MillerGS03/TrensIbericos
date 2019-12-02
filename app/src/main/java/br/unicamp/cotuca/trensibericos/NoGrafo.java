package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;

public class NoGrafo<Dado extends Serializable & Comparable<Dado>> implements Serializable, Comparable<NoGrafo<Dado>> {
    protected Dado dado;
    protected NoGrafo<Dado> anterior;
    protected double distancia;
    protected boolean foiVisitado;
    protected int indice;

    public int getIndice() {
        return indice;
    }
    public void setIndice(int indice) {
        this.indice = indice;
    }

    public boolean foiVisitado() {
        return foiVisitado;
    }

    public void setFoiVisitado(boolean foiVisitado) {
        this.foiVisitado = foiVisitado;
    }

    public Dado getDado() {
        return dado;
    }
    public void setDado(Dado dado) {
        this.dado = dado;
    }

    public NoGrafo<Dado> getAnterior() {
        return anterior;
    }
    public void setAnterior(NoGrafo<Dado> ant) {
        this.anterior = ant;
    }

    public double getDistancia() {
        return distancia;
    }
    public void setDistancia(double d) {
        this.distancia = d;
    }

    public NoGrafo() {
        dado = null;
        anterior = null;
        distancia = -1;
        indice = -1;
    }

    public NoGrafo(Dado d, NoGrafo<Dado> ant, double dist, int ind) {
        dado = d;
        anterior = ant;
        distancia = dist;
        indice = ind;
    }

    public int compareTo(NoGrafo<Dado> dadoNoGrafo) {
        return dado.compareTo(dadoNoGrafo.getDado());
    }
}
