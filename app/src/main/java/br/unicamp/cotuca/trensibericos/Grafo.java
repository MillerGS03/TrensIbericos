package br.unicamp.cotuca.trensibericos;

public class Grafo {
    public static int qtdCidades = 54;
    Caminho[][] adj;
    Fila<Cidade> prioridade;

    public Grafo()
    {
        adj = new Caminho[qtdCidades][qtdCidades];
    }
    public Caminho getPath(Cidade c1, Cidade c2) {
        return null;
    }
}
