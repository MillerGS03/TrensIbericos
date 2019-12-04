/**
 * @author 18178 - Felipe Scherer Vicentin
 * @author 18179 - Gustavo Miller Santos
 */


package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;

//classe abstrata que representa o Dado da HashTable que implementa compareTo e é serializável
//o Dado pode ter uma chave de um tipo genérico, não apenas String
public abstract class Hashable<Dado extends Hashable<Dado, Key>, Key extends Comparable<Key>> implements Comparable<Dado>, Serializable {

    public abstract Key getKey();

    public abstract long getKeyHashcode(Key key);

    //o compareTo já é implementado. Assim, a classe que herdar Hashable não precisará implementá-lo
    public int compareTo(Dado dado)
    {
        return getKey().compareTo(dado.getKey());
    }
}
