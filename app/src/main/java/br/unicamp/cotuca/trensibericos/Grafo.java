package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;

public class Grafo<Dado extends Serializable & Comparable<Dado>> {
    protected int qtdDados = 54;
    protected int qtdParams = 1;

    protected ListaSimples<NoGrafo<Dado>> nos;
    protected ListaSimples[][] adj;
    protected ListaSimples<Object> demonstracaoParams = null;

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
        NoGrafo<Dado> comeco = nos.get(inicio);
        int ind = 0;

        for(NoGrafo<Dado> no : nos) {
            no.setFoiVisitado(false);
            no.setAnterior(comeco);
            ListaSimples lista = adj[inicio][ind];
            if (lista != null)
                no.setDistancia(((Number)lista.get(ordenacao)).doubleValue());
            else
                no.setDistancia(Double.POSITIVE_INFINITY);
            ind++;
        }

        comeco.setFoiVisitado(true);
        comeco.setAnterior(null);
    }

    protected Path<Dado> getPath(int c1, int c2, int ordenacao) {
        resetarNos(c1, ordenacao);

        Path<Dado> path = new Path<>();

        for (int i = 0; i < nos.getSize(); i++)
        {
            int verticeAtual = findClosest();
            if (verticeAtual < 0) continue;
            nos.get(verticeAtual).setFoiVisitado(true);
            updateNearNodes(verticeAtual, ordenacao);
        }

        buildPath(path, c2);

        if (path.getPath().getSize() == 0)
            return null;
        return path;
    }
    protected int findClosest()
    {
        int indiceAtual = 0, indiceMenor = -1;
        double minDist = Double.POSITIVE_INFINITY;
        for (NoGrafo<Dado> no : nos) {
            if (!no.foiVisitado() && no.getDistancia() < minDist) {
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
            ListaSimples lista = adj[atual.getIndice()][indiceAnterior];
            if (lista != null) {
                path.getPath().addToBeginning(atual.getDado());
                path.getParams().addValues(lista);
                indiceAnterior = atual.getIndice();
            } else {
                path.getPath().reset();
                break;
            }
        }
    }
    protected void updateNearNodes(int indiceMenor, int ordenacao)
    {
        NoGrafo<Dado> menor = nos.get(indiceMenor);
        for (int i = 0; i < nos.getSize(); i++)
        {
            NoGrafo<Dado> atual = nos.get(i);
                if (!atual.foiVisitado())
            {
                Object atualAteProximo = adj[indiceMenor][i] != null?adj[indiceMenor][i].get(ordenacao):null;
                if (atualAteProximo == null) continue;
                if (!(atualAteProximo instanceof Number))
                    break;

                double distancia = ((Number)atualAteProximo).doubleValue() + menor.getDistancia();

                if (distancia < atual.getDistancia())
                {
                    atual.setAnterior(menor);
                    atual.setDistancia(distancia);
                }
            }
        }
    }

    public ListaSimples<Path<Dado>> getPaths(int c1, int c2) {
        ListaSimples<Path<Dado>> paths = new ListaSimples<>();

        for(int i = 0; i < qtdParams; i++)
            if (demonstracaoParams.get(i) instanceof Number)
                paths.add(getPath(c1, c2, i));

        return paths;
    }
}
