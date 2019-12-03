package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

public class ListaSimples<Dado> implements Serializable, Iterable<Dado> {
    protected No<Dado> comeco, fim, atual;
    private int qtd;
    private int index;

    public ListaSimples()
    {
        index = 0;
        comeco = null;
        atual = null;
    }
    public ListaSimples(Dado... vet) {
        for (Dado dado : vet)
            add(dado);
        index = 0;
        atual = comeco;
    }

    public void add(Dado dado)
    {
        if (dado == null)
            return;
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

    protected void getNode(int index) {
        if (index < this.index) {
            this.index = 0;
            atual = comeco;
        }
        if (atual == null)
            atual = comeco;
        for(; atual != null && this.index < index; atual = atual.getProx(), this.index++) {}
    }

    public Dado get(int index)
    {
        getNode(index);
        if (atual != null)
            return atual.getInfo();
        return null;
    }

    public void addValues(ListaSimples<Dado> l) {
        No<Dado> no1 = comeco;
        No<Dado> no2 = l.comeco;

        while (no1 != null && no2 != null) {
            Dado info1 = no1.getInfo();
            Dado info2 = no2.getInfo();
            Object set;

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

    public void reset() {
        comeco = fim = null;
        qtd = 0;
        atual = null;
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
