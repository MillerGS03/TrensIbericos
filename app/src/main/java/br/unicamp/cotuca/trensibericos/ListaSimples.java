/**
 * @author 18178 - Felipe Scherer Vicentin
 * @author 18179 - Gustavo Miller Santos
 */


package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

//classe ListaSimples
public class ListaSimples<Dado> implements Serializable, Iterable<Dado> {
    //variáveis de controle
    protected No<Dado> comeco, fim, atual;
    private int qtd;
    private int index;

    //no construtor, as variáveis são zeradas
    public ListaSimples()
    {
        index = 0;
        comeco = null;
        atual = null;
    }

    //construtor com n Dados para serem adicionados
    public ListaSimples(Dado... vet) {
        for (Dado dado : vet)
            add(dado);
        index = 0;
        atual = comeco;
    }

    //método para adicionar um dado ao final da lista
    public void add(Dado dado)
    {
        if (dado == null) //se o dado for null, ele não é adicionado
            return;
        No<Dado> no = new No<>(dado, null);
        if (comeco == null) //se o comeco for null, o dado será o novo comeco
            comeco = no;
        else
            fim.setProx(no); //nó novo é adicionado ao fim da lista
        fim = no;
        qtd++;
    }

    //método que adiciona o dado ao começo da lista
    public void addToBeginning(Dado dado)
    {
        No<Dado> no = new No<>(dado, comeco);
        comeco = no;
        if (fim == null)
            fim = no;
        qtd++;
    }

    //retorna o nó de índice index
    protected void getNode(int index) {
        if (index < this.index) { //se o índice passado for maior que o index
            this.index = 0; //index e atual são resetados
            atual = comeco;
        }
        if (atual == null)
            atual = comeco; //se atual for null, atribui-se à ele o comeco
        //for para percorrer os nós até index
        for(; atual != null && this.index < index; atual = atual.getProx(), this.index++) {}
        //ao final do for, atual valerá o nó desejado
    }

    //retorna o dado de índice index
    public Dado get(int index)
    {
        getNode(index);
        if (atual != null)
            return atual.getInfo();
        return null;
    }

    //adiciona aos itens da lista atual todos os valores da lista l
    public void addValues(ListaSimples<Dado> l) {
        No<Dado> no1 = comeco; //nó da lista atual
        No<Dado> no2 = l.comeco; //nó da lista passada como parâmetro

        while (no1 != null && no2 != null) {
            Dado info1 = no1.getInfo();
            Dado info2 = no2.getInfo();
            Object set;

            if (info1 instanceof Number && info2 instanceof Number) { //se os valores forem números, eles são somados
                set = ((Number)info1).doubleValue() + ((Number)info2).doubleValue();
            } else { //se não, as strings são somadas
                set = info1.toString() + info2.toString();
            }

            no1.setInfo((Dado)set); //nó da lista atual tem valor atualizado

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

    //método que reseta a lista
    public void reset() {
        comeco = fim = null;
        qtd = 0;
        atual = null;
    }

    //método retorna array de Dado[] relativo à lista
    public Dado[] toArray(Class<? extends Dado[]> classe) //é necessário a passagem da classe de Dado
    {
        ArrayList<Dado> ret = new ArrayList<>(); //arraylist é estritamente necessário para formar o vetor, já que ele tem o método toArray(), que retorna Object[]
        No<Dado> no = comeco;

        while(no != null) {
            ret.add(no.getInfo()); //todos os nós são adicionados ao arraylist
            no = no.getProx();
        }

        Object[] vet = ret.toArray(); //vet é Object[], contendo todos os dados

        return Arrays.copyOf(vet, vet.length, classe); //retorna uma cópia de vet, mas com o tipo classe, que é o objeto genérico
    }

    //iterator para fazer foreach com a lista
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
