package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;

public abstract class Hashable<Dado extends Hashable<Dado, Key>, Key extends Comparable<Key>> implements Comparable<Dado>, Serializable {

    public abstract Key getKey();

    public int compareTo(Dado dado)
    {
        return getKey().compareTo(dado.getKey());
    }
}
