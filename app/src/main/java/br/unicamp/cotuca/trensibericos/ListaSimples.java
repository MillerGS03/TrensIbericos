package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

public class ListaSimples<Dado extends Comparable<Dado>> implements Serializable, Iterable<Dado> {
    protected No<Dado> comeco, fim;
    private int qtd;

    public ListaSimples()
    {
        comeco = null;
    }

    public void add(Dado dado)
    {
        No<Dado> no = new No<>(dado, null);
        if (comeco == null)
            comeco = no;
        else
            fim.setProx(no);
        fim = no;
        qtd++;
    }

    public void remove(int index) {
        if (comeco == null) return;
        if (index == 0)
        {
            comeco = comeco.getProx();
            return;
        }
        int i = 0;
        No<Dado> no, ant;
        for(no = comeco.getProx(), ant = comeco; no != null && i != index; ant = no, no = no.getProx(), i++) {}
        if (no == null) return;
        ant.setProx(no.getProx());
    }


    public Dado get(int index)
    {
        int i = 0;
        No<Dado> no;
        for(no = comeco; no != null && i != index; no = no.getProx(), i++) {}
        if (no == null)
            return null;
        return no.getInfo();
    }

    public int getSize()
    {
        return qtd;
    }

    public Dado[] toArray(Class<? extends Dado[]> classe)
    {
        ArrayList<Dado> ret = new ArrayList<Dado>();
        No<Dado> no = comeco;

        while(no != null) {
            ret.add(no.getInfo());
            no = no.getProx();
        }

        Object[] vet = ret.toArray();

        return Arrays.copyOf(vet, vet.length, classe);
    }

    public Iterator<Dado> iterator() {
        return new DadoIterator();
    }

    class DadoIterator implements Iterator<Dado>{
        private No<Dado> current;

        public boolean hasNext() {
            if(current == null){
                current = comeco;
                return Optional.ofNullable(current).isPresent();
            }else{
                current = current.getProx();
                return Optional.ofNullable(current).isPresent();
            }
        }

        public Dado next() {
            return current.getInfo();
        }
    }
}
