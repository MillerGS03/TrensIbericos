package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;

public class Grafo<Dado extends Serializable & Comparable<Dado>> {
    protected int qtdDados = 54;

    protected ListaSimples<NoGrafo<Dado>> nos;
    protected ListaSimples<Double>[][] adj;

    public void setDados(ListaSimples<Dado> dados) {
        qtdDados = dados.getSize();
        nos = new ListaSimples<>();
    }

    public void setLigacao(int d1, int d2, Double... vals) {
        adj[d1][d2] = new ListaSimples<>(vals);
    }
    public void setLigacao(int d1, int d2, ListaSimples<Double> lista) {
        adj[d1][d2] = lista;
    }

    protected void adicinarLista(ListaSimples<Double> l1, ListaSimples<Double> l2) {
        //
    }

    public Grafo()
    {
        adj = new ListaSimples[qtdDados][qtdDados];
        nos = new ListaSimples<>();
    }
    protected void resetarNos() {
        for(NoGrafo<Dado> no : nos) {
            no.setAnterior(null);
            no.setDistancia(Double.POSITIVE_INFINITY);
            no.setEstaAtivo(true);
            no.setFoiVisitado(false);
        }
    }

    public Path<Dado> getPath(int c1, int c2, int ordenacao) {
        resetarNos();

        nos.get(c1).setFoiVisitado(true);
        nos.get(c1).setDistancia(0);

        Path<Dado> path = new Path<>();

        int noAtual = c1;

        return path;
    }
}
