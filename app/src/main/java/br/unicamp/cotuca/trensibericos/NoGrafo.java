package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;

public class NoGrafo<Dado extends Serializable & Comparable<Dado>> implements Serializable, Comparable<NoGrafo<Dado>> {
    protected Dado dado;
    protected Dado anterior;
    protected double distancia;
    protected boolean foiVisitado;
    protected boolean estaAtivo;

    public boolean estaAtivo() {
        return estaAtivo;
    }

    public void setEstaAtivo(boolean estaAtivo) {
        this.estaAtivo = estaAtivo;
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

    public Dado getAnterior() {
        return anterior;
    }
    public void setAnterior(Dado ant) {
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
    }

    public NoGrafo(Dado d, Dado ant, double dist) {
        dado = d;
        anterior = ant;
        distancia = dist;
    }

    public int compareTo(NoGrafo<Dado> dadoNoGrafo) {
        return dado.compareTo(dadoNoGrafo.getDado());
    }
}
