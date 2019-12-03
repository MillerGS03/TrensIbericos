package br.unicamp.cotuca.trensibericos;

import android.content.Intent;
import android.util.Log;

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
            nos.add(new NoGrafo<>(dado, null, Double.POSITIVE_INFINITY, nos.getSize()));
        adj = new ListaSimples[qtdDados][qtdDados];
    }
    public void setLigacao(int d1, int d2, Object... vals) {
        adj[d1][d2] = new ListaSimples<>(vals);
        if (vals.length > qtdParams)
            qtdParams = vals.length;
        demonstracaoParams = adj[d1][d2];
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
        nos = new ListaSimples<>();
    }
    protected void resetarNos(int inicio, int ordenacao) {
        int ind = 0;

        for(NoGrafo<Dado> no : nos) {
            no.setAnterior(null);
            ListaSimples lista = adj[inicio][ind];
            if (lista != null)
                no.setDistancia(((Number)lista.get(ordenacao)).doubleValue());
            else
                no.setDistancia(Double.POSITIVE_INFINITY);
            no.setFoiVisitado(false);

            ind++;
        }

    }

    protected Path<Dado> getPath(int c1, int c2, int ordenacao) {
        resetarNos(c1, ordenacao);

        nos.get(c1).setFoiVisitado(true);
        nos.get(c1).setDistancia(0);

        Path<Dado> path = new Path<>();

        for (int i = 0; i < nos.getSize(); i++)
        {
            int indiceMenor = findClosest();
            if (indiceMenor < 0) continue;
            nos.get(indiceMenor).setFoiVisitado(true);
            updateNearNodes(indiceMenor, ordenacao);
        }

        buildPath(path, c2);

        return path;
    }
    protected int findClosest()
    {
        int indiceAtual = 0, indiceMenor = -1;
        double minDist = Double.POSITIVE_INFINITY;
        for (NoGrafo<Dado> no : nos) {
            if (!no.foiVisitado() && no.getDistancia() != -1 && no.getDistancia() <= minDist) {
                indiceMenor = indiceAtual;
                minDist = no.getDistancia();
            }
            indiceAtual++;
        }

        return indiceMenor;
    }
    protected void buildPath(Path<Dado> path, int end)
    {
        NoGrafo<Dado> atual = nos.get(end);
        int indiceAnterior = end;
        path.getPath().addToBeginning(atual.getDado());
        while ((atual = atual.getAnterior()) != null) {
            path.getPath().addToBeginning(atual.getDado());
            path.getParams().addValues(adj[atual.getIndice()][indiceAnterior]);
            indiceAnterior = atual.getIndice();
        }
    }
    protected void updateNearNodes(int indiceMenor, int ordenacao)
    {
        for (int i = 0; i < nos.getSize(); i++)
        {
            NoGrafo<Dado> atual = nos.get(i);
            if (!atual.foiVisitado())
            {
                Object atualAteProximo = adj[indiceMenor][i] != null?adj[indiceMenor][i].get(ordenacao):null;
                if (atualAteProximo == null) continue;
                if (!(atualAteProximo instanceof Number))
                    break;

                double distancia = ((Number)atualAteProximo).doubleValue() + nos.get(indiceMenor).getDistancia();

                if (distancia < atual.getDistancia())
                {
                    atual.setAnterior(nos.get(indiceMenor));
                    atual.setDistancia(distancia);
                }
            }
        }
    }

    ListaSimples<Object> demonstracaoParams = null;
    public ListaSimples<Path<Dado>> getPaths(int c1, int c2) {
        ListaSimples<Path<Dado>> paths = new ListaSimples<>();

        for(int i = 0; i < qtdParams; i++)
            if (demonstracaoParams.get(i) instanceof Number)
                paths.add(getPath(c1, c2, i));

        return paths;
    }
}
