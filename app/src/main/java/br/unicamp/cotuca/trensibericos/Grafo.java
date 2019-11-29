package br.unicamp.cotuca.trensibericos;

public class Grafo {
    public static int qtdCidades = 54;
    CidadeInfo[][] adj;

    public Grafo()
    {
        adj = new CidadeInfo[qtdCidades][qtdCidades];
    }
}
