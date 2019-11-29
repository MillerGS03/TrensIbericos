package br.unicamp.cotuca.trensibericos;

public class Pilha<Dado extends Comparable<Dado>> {
    No<Dado> comeco;
    int qtdDados;

    public Pilha()
    {
        comeco = null;
        qtdDados = 0;
    }

    public Pilha(Pilha<Dado> pilha)
    {
        comeco = pilha.comeco;
        qtdDados = pilha.qtdDados;
    }

    public Dado getTopo()
    {
        return comeco.getInfo();
    }

    public Dado pop() throws Exception
    {
        if (qtdDados == 0)
            throw new Exception("Pilha vazia!");

        Dado ret = comeco.getInfo();
        comeco = comeco.getProx();

        qtdDados--;

        return ret;
    }

    public void push(Dado dado)
    {
        comeco = new No<Dado>(dado, comeco);
        qtdDados++;
    }

    public int getTamanho()
    {
        return qtdDados;
    }
}
