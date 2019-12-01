package br.unicamp.cotuca.trensibericos;

public class Grafo {
    public static int qtdCidades = 54;
    protected HashTable<Cidade, String> cidades;
    protected ListaSimples<Caminho> ligacoes;
    protected Caminho[][] adj;
    protected Fila<Cidade> prioridade;

    public void setCidades(HashTable<Cidade, String> cidades) {
        this.cidades = cidades;
    }
    public void setLigacoes(ListaSimples<Caminho> caminhos) {
        this.ligacoes = caminhos;
    }

    public HashTable<Cidade, String> getCidades() {
        return cidades;
    }
    public ListaSimples<Caminho> getLigacoes() {
        return ligacoes;
    }

    public Grafo()
    {
        adj = new Caminho[qtdCidades][qtdCidades];
    }
    public ListaSimples<Caminho> getPaths(String c1, String c2) {
        Cidade cidade1 = cidades.get(c1);
        Cidade cidade2 = cidades.get(c2);
        return null;
    }
}
