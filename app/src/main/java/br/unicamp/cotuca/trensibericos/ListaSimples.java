package br.unicamp.cotuca.trensibericos;

public class ListaSimples<Dado extends Keyable> {
    private No<Dado> comeco, fim;
    private int qtd;

    public ListaSimples()
    {
        comeco = null;
    }

    public void add(Dado dado)
    {
        No<Dado> no = new No<Dado>(dado, null);
        if (comeco == null)
            comeco = no;
        else
            fim.setProx(no);
        fim = no;
        qtd++;
    }

    public void remove(String key)
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

    public Dado find(String key)
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

    public Dado get(int index)
    {
        int i = 0;
        No<Dado> no;
        for(no = comeco; no != null && i != index; no = no.getProx(), i++) {}
        if (no == null)
            return null;
        return no.getInfo();
    }
}
