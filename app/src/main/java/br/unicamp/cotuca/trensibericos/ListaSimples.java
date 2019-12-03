package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

public class ListaSimples<Dado> implements Serializable, Iterable<Dado> {
    protected No<Dado> comeco, fim;
    private int qtd;

    public ListaSimples()
    {
        comeco = null;
    }
    public ListaSimples(Dado... vet) {
        for (Dado dado : vet)
            add(dado);
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

    public void addToBeginning(Dado dado)
    {
        No<Dado> no = new No<>(dado, comeco);
        comeco = no;
        if (fim == null)
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

    protected No<Dado> getNode(int index) {
        int i = 0;
        No<Dado> no;
        for(no = comeco; no != null && i != index; no = no.getProx(), i++) {}
        return no;
    }

    public Dado get(int index)
    {
        No<Dado> no = getNode(index);
        if (no != null)
            return no.getInfo();
        return null;
    }
    public void set(Dado data, int index) {
        getNode(index).setInfo(data);
    }

    public void addValues(ListaSimples<Dado> l) {
        No<Dado> no1 = comeco;
        No<Dado> no2 = l.comeco;

        while (no1 != null && no2 != null) {
            Dado info1 = no1.getInfo();
            Dado info2 = no2.getInfo();
            Object set = info1;

            if (info1 instanceof Character && info2 instanceof Character) {
                set = ((Character)info1) + ((Character)info2);
            } else if (info1 instanceof Number && info2 instanceof Number) {
                set = ((Number)info1).doubleValue() + ((Number)info2).doubleValue();
            } else {
                set = info1.toString() + info2.toString();
            }

            no1.setInfo((Dado)set);

            no1 = no1.getProx();
            no2 = no2.getProx();
        }
        while(no2 != null) {
            add(no2.getInfo());
            no2 = no2.getProx();
        }
    }

    public int getSize()
    {
        return qtd;
    }

    public String toString() {
        String ret = "";
        No<Dado> no = comeco;

        while(no != null) {
            ret += no.getInfo().toString() + ", ";
            no = no.getProx();
        }

        if (ret.length() > 2)
            ret = ret.substring(0, ret.length() - 2);
        return ret;
    }

    public Dado[] toArray(Class<? extends Dado[]> classe)
    {
        ArrayList<Dado> ret = new ArrayList<>();
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
