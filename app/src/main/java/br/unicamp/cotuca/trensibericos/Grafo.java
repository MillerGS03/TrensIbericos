package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;

public class Grafo<Dado extends Serializable & Comparable<Dado>> {
    protected int qtdDados = 54;
    protected int qtdParams = 1;

    protected ListaSimples<NoGrafo<Dado>> nos;
    protected ListaSimples[][] adj;

    public void setDados(ListaSimples<Dado> dados) {
        qtdDados = dados.getSize();
        nos = new ListaSimples<>();
        for(Dado dado : dados)
            nos.add(new NoGrafo<>(dado, null, Double.POSITIVE_INFINITY));
        adj = new ListaSimples[qtdDados][qtdDados];
    }

    public void setLigacao(int d1, int d2, Comparable v1, Object... vals) {
        adj[d1][d2] = new ListaSimples<>(v1, vals);
        if (vals.length + 1 > qtdParams)
            qtdParams = 1 + vals.length;
    }
    public void setLigacao(int d1, int d2, ListaSimples<Object> lista) {
        if (lista.get(0) instanceof Comparable) {
            adj[d1][d2] = lista;
            if (lista.getSize() > qtdParams)
                qtdParams = lista.getSize();
        }
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

    protected Path<Dado> getPath(int c1, int c2, int ordenacao) {
        resetarNos();

        nos.get(c1).setFoiVisitado(true);
        nos.get(c1).setDistancia(0);

        Path<Dado> path = new Path<>();
        int no = c1;

        return path;
    }

    public Path<Dado>[] getPaths(int c1, int c2) {
        Path[] paths = new Path[qtdParams];

        for(int i = 0; i < qtdParams; i++)
            paths[i] = getPath(c1, c2, i);

        return paths;
    }
}
