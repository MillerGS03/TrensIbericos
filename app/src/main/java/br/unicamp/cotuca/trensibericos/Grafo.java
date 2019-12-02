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
    protected void resetarNos() {
        for(NoGrafo<Dado> no : nos) {
            no.setAnterior(null);
            no.setDistancia(Double.POSITIVE_INFINITY);
            no.setFoiVisitado(false);
        }
    }

    protected Path<Dado> getPath(int c1, int c2, int ordenacao) {
        resetarNos();

        nos.get(c1).setFoiVisitado(true);
        nos.get(c1).setDistancia(0);

        Path<Dado> path = new Path<>();

        for (NoGrafo<Dado> no : nos)
        {
            int indiceMenor = findClosest();
            nos.get(indiceMenor).setFoiVisitado(true);
            updateNearNodes(indiceMenor, ordenacao);
        }

        buildPath(path, c2);

        return path;
    }
    protected int findClosest()
    {
        NoGrafo<Dado> menor = null;
        int indiceAtual = 0, indiceMenor = -1;
        for (NoGrafo<Dado> no : nos) {
            if (!no.foiVisitado() && no.getDistancia() != -1 && (menor == null || no.getDistancia() < menor.getDistancia())) {
                menor = no;
                indiceMenor = indiceAtual;
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
                Object atualAteProximo = adj[indiceMenor][i].get(ordenacao);
                if (!(atualAteProximo instanceof Number))
                    break;
                Double distancia = nos.get(indiceMenor).getDistancia() + (Double)atualAteProximo;

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
            if (!(demonstracaoParams.get(i) instanceof Number))
                paths.add(getPath(c1, c2, i));

        return paths;
    }
}
