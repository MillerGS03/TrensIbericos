package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;

public class Fila<Dado extends Keyable & Serializable> {
    No<Dado> comeco, ultimo;
    int qtdDados;

    public Fila()
    {
        comeco = null;
        qtdDados = 0;
    }

    public Fila(Fila<Dado> fila)
    {
        comeco = fila.comeco;
        qtdDados = fila.qtdDados;
    }

    public Dado getFirst()
    {
        return comeco.getInfo();
    }

    public Dado dequeue() throws Exception
    {
        if (qtdDados == 0)
            throw new Exception("Fila vazia!");

        Dado ret = comeco.getInfo();
        comeco = comeco.getProx();

        qtdDados--;

        return ret;
    }

    public void enqueue(Dado dado)
    {
        if (comeco == null || qtdDados == 0) {
            comeco = new No<Dado>(dado, null);
            ultimo = comeco;
        }

        No<Dado> aux = new No<Dado>(dado, null);
        ultimo.setProx(aux);
        ultimo = aux;
        qtdDados++;
    }

    public int getTamanho()
    {
        return qtdDados;
    }
}
