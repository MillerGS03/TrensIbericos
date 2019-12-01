package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;

public class ListaHash<Dado extends Hashable<Dado, Key>, Key extends Comparable<Key> & Serializable> extends ListaSimples<Dado> implements Serializable {
    public void remove(Key key)
    {
        if (comeco == null)
            return;
        if (comeco.getInfo().getKey().equals(key)) {
            comeco = comeco.getProx();
            return;
        }
        No<Dado> aux = comeco.getProx();
        No<Dado> ant = comeco;
        while(aux != null)
        {
            if (aux.getInfo().getKey().equals(key))
            {
                ant.setProx(aux.getProx());
                break;
            }

            ant = aux;
            aux = aux.getProx();
        }
    }

    public Dado find(Key key)
    {
        if (key.equals(comeco.getInfo().getKey()))
            return comeco.getInfo();
        No<Dado> aux = comeco;
        while(aux != null && !aux.getInfo().getKey().equals(key))
            aux = aux.getProx();
        if (aux == null)
            return null;
        return aux.getInfo();
    }
}
