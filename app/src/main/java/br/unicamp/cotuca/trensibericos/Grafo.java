/**
 * @author 18178 - Felipe Scherer Vicentin
 * @author 18179 - Gustavo Miller Santos
 */


package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;

//Classe genérica Grafo
public class Grafo<Dado extends Serializable & Comparable<Dado>> {
    protected int qtdDados = 54;
    protected int qtdParams = 1;

    protected ListaSimples<NoGrafo<Dado>> nos; //lista de nós do grafo
    protected ListaSimples[][] adj; //matriz de adjacencia
    protected ListaSimples<Object> demonstracaoParams = null; //Lista que representa uma demonstração de parâmetros acumuladores passados

    //adiciona a lista de dados passada aos nós e reinicia a matriz de adjacencia com o tamanho necessário
    public void setDados(ListaSimples<Dado> dados) {
        qtdDados = dados.getSize();
        nos = new ListaSimples<>();
        for(Dado dado : dados)
            nos.add(new NoGrafo<>(dado, null, Double.POSITIVE_INFINITY, nos.getSize()));
        adj = new ListaSimples[qtdDados][qtdDados];
    }

    //adiciona uma ligação entre dois vértices (d1 e d2) com n parâmetros de acumulação (vals)
    public void setLigacao(int d1, int d2, Object... vals) {
        adj[d1][d2] = new ListaSimples<>(vals);
        if (vals.length > qtdParams)
            qtdParams = vals.length;
        demonstracaoParams = adj[d1][d2]; //valor de demonstracaoParams é atribuido à Lista de objetos passada
    }

    //adiciona uma ligação entre dois vértices (d1 e d2) com a lista passada contendo os parâmetros de acumulação
    public void setLigacao(int d1, int d2, ListaSimples<Object> lista) {
        adj[d1][d2] = lista;
        if (lista.getSize() > qtdParams)
            qtdParams = lista.getSize();
        demonstracaoParams = lista;
    }

    public Grafo()
    {
        nos = new ListaSimples<>();
    }

    //reseta os nós do grafo para nova execução do algoritmo de Dijkstra
    protected void resetarNos(int inicio, int ordenacao) {
        NoGrafo<Dado> comeco = nos.get(inicio);
        int ind = 0;

        for(NoGrafo<Dado> no : nos) {
            no.setFoiVisitado(false); //ainda não foi visitado
            no.setAnterior(comeco); //o pai é o nó inicial
            ListaSimples lista = adj[inicio][ind]; //lista de parametros de acumulação
            if (lista != null && lista.get(ordenacao) instanceof Number) //se a posição atual da lista de parâmetros acumuladores for ordenável (número) a distância é
                //atribuida com base no número presente na lista
                no.setDistancia(((Number)lista.get(ordenacao)).doubleValue());
            else //se não houver lista ou o parâmetro não for ordenável, a distância do nó atual é infinito
                no.setDistancia(Double.POSITIVE_INFINITY);
            ind++;
        }

        //o nó inicial já foi visitado e não têm pai
        comeco.setFoiVisitado(true);
        comeco.setAnterior(null);
    }

    //retorna o Path<Dado> relativo ao Dijkstra de inicio c1, fim c2 e ordenação ord
    protected Path<Dado> getPath(int c1, int c2, int ord) {
        resetarNos(c1, ord); //reseta os nós para um novo Dijkstra

        Path<Dado> path = new Path<>();

        for (int i = 0; i < nos.getSize(); i++) //para cada nó do grafo
        {
            int verticeAtual = findClosest(); //achar o indice do nó com menor distancia
            if (verticeAtual < 0) continue;
            nos.get(verticeAtual).setFoiVisitado(true); //nó atual já foi visitado
            updateNearNodes(verticeAtual, ord); //atualização dos nós que têm ligação com o atual
        }

        buildPath(path, c2); //caminho é construido com nos anteriores de cada nó

        if (path.getPath().getSize() == 0) //se o caminho estiver vazio, retornar null
            return null;
        return path;
    }

    //acha o indice do nó com menor distância
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

    //constroi o caminho com base no final
    protected void buildPath(Path<Dado> path, int end)
    {
        NoGrafo<Dado> atual = nos.get(end);
        int indiceAnterior = end;

        path.getPath().addToBeginning(atual.getDado()); //dado do nó atual é adicionado no começo da lista do caminho, já que o percurso será invertido

        while ((atual = atual.getAnterior()) != null) {
            ListaSimples lista = adj[atual.getIndice()][indiceAnterior]; //lista de parâmetros acumuláveis

            if (lista != null) {
                path.getPath().addToBeginning(atual.getDado()); //nó atual é adicionado no começo
                path.getParams().addValues(lista); //lista de parâmetros acumuláveis é adicionada à lista de parâmetros atual

                indiceAnterior = atual.getIndice();
            } else {
                path.getPath().reset(); //se a lista de parâmetros acumuláveis for null, o caminho não é valido e deve ser resetado
                break;
            }
        }
    }

    //atualiza os nós adjacentes ao nó atual
    protected void updateNearNodes(int indiceMenor, int ordenacao)
    {
        NoGrafo<Dado> menor = nos.get(indiceMenor);

        for (int i = 0; i < nos.getSize(); i++)
        {
            NoGrafo<Dado> atual = nos.get(i);

            if (!atual.foiVisitado()) //se o nó atual não foi visitado ainda
            {
                //verificar se existe ligação entre menor e atual e, se houver, obter o parâmetro de ordenação atual. Caso contrário, atualAteProximo será null
                Object atualAteProximo = adj[indiceMenor][i] != null?adj[indiceMenor][i].get(ordenacao):null;
                if (atualAteProximo == null) continue;      //se não houver ligação, continuar com o for
                if (!(atualAteProximo instanceof Number))   //se o parâmetro de ordenação atual não for ordenável, parar a atualização
                    break;

                double distancia = ((Number)atualAteProximo).doubleValue() + menor.getDistancia(); //nova distância entre menor e atual

                if (distancia < atual.getDistancia()) //se a distância for, de fato, menor, atualizar atual
                {
                    atual.setAnterior(menor);
                    atual.setDistancia(distancia);
                }
            }
        }
    }

    //retorna uma Lista de Path<Dado>. Cada Path corresponde à um parâmetro de ordenação
    public ListaSimples<Path<Dado>> getPaths(int c1, int c2) {
        ListaSimples<Path<Dado>> paths = new ListaSimples<>();

        for(int i = 0; i < qtdParams; i++)
            if (demonstracaoParams.get(i) instanceof Number) //se o parâmetro atual for ordenável
                paths.add(getPath(c1, c2, i));

        return paths;
    }
}
