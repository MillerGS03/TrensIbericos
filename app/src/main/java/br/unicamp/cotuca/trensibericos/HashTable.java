package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.function.Predicate;

public class HashTable<Dado extends Hashable<Dado, Key>, Key extends Comparable<Key> & Serializable> implements Serializable {
    protected final int TAMANHO_MAX = 10000;
    protected ListaHash[] vet;
    protected int qtdDados;
    protected Class<? extends Dado> classe;

    public HashTable(int tamanho, Class<? extends Dado> c)
    {
        vet = new ListaHash[tamanho];
        qtdDados = 0;
        classe = c;
    }
    public HashTable(Class<? extends Dado> c)
    {
        classe = c;
        vet = new ListaHash[TAMANHO_MAX];
        qtdDados = 0;
    }

    protected int hash(Key key)
    {
        try {
            Dado dado = classe.newInstance();
            long ret = dado.getKeyHashcode(key);

            ret %= vet.length - 1;

            return (int) Math.abs(ret);
        } catch (Exception ex) {
            return -1;
        }
    }

    public void add(Dado dado)
    {
        int index = hash(dado.getKey());
        ListaHash lista = vet[index];

        if (lista == null)
            lista = new ListaHash();

        lista.add(dado);

        vet[index] = lista;

        qtdDados++;
    }

    public Dado get(Key key)
    {
        int index = hash(key);
        ListaHash lista = vet[index];

        if (lista == null)
            return null;

        return (Dado)lista.find(key);
    }

    public ListaHash[] getVetor()
    {
        ListaHash[] arr = new ListaHash[qtdDados];
        int ind = 0;

        for (int i = 0; i < vet.length; i++)
        {
            if (vet[i] != null)
                arr[ind++] = vet[i];
        }

        ListaHash[] ret = new ListaHash[ind];

        for(int i = 0; i < ind; i++)
            ret[i] = arr[i];

        return ret;
    }

    public ListaSimples<Key> getKeys() {
        ListaSimples<Key> keys = new ListaSimples<>();
        ListaHash[] arr = getVetor();
        for(ListaHash<Dado, Key> lista : arr) {
            for (Dado dado : lista)
                keys.add(dado.getKey());
        }

        return keys;
    }

    public ListaSimples<Key> getKeys(Predicate<Dado> predicate)
    {
        ListaSimples<Key> keys = new ListaSimples<>();
        ListaHash[] arr = getVetor();
        for(ListaHash<Dado, Key> lista : arr) {
            for (Dado dado : lista) {
                if (predicate.test(dado))
                    keys.add(dado.getKey());
            }
        }

        return keys;
    }

    public int getSize()
    {
        return qtdDados;
    }
}
