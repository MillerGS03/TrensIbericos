package br.unicamp.cotuca.trensibericos;

public class Pilha<Dado extends Comparable<Dado>> {
    //region No de lista
    public class No<T extends Comparable<T>> implements Comparable<No<T>>
    {
        No prox;
        T info;

        public No()
        {
            prox = null;
            info = null;
        }

        public No(T info, No prox)
        {
            setInfo(info);
            setProx(prox);
        }

        public No(No<T> no)
        {
            setInfo(no.getInfo());
            setProx(no.getProx());
        }

        public T getInfo() {
            return info;
        }
        public void setInfo(T info) {
            this.info = info;
        }

        public No getProx() {
            return prox;
        }
        public void setProx(No prox) {
            this.prox = prox;
        }

        public String toString()
        {
            return "[No] " + info.toString();
        }
        public int hashCode()
        {
            int ret = 3;
            ret = ret * 5 + info.hashCode();
            return ret;
        }
        public boolean equals(Object outro)
        {
            if (outro == this)
                return true;
            if (outro == null)
                return false;
            if (!(outro instanceof No))
                return false;
            No no = (No)outro;
            if (!this.info.equals(no.getInfo()))
                return false;
            return true;
        }

        public int compareTo(No<T> outro)
        {
            return info.compareTo(outro.getInfo());
        }
    }
    //endregion

    No<Dado> comeco;
    int qtdDados;

    public Pilha()
    {
        comeco = null;
        qtdDados = 0;
    }

    public Pilha(Pilha<Dado> pilha)
    {
        comeco = pilha.comeco;
        qtdDados = pilha.qtdDados;
    }

    public Dado getTopo()
    {
        return comeco.getInfo();
    }

    public Dado pop() throws Exception
    {
        if (qtdDados == 0)
            throw new Exception("Pilha vazia!");

        Dado ret = comeco.getInfo();
        comeco = comeco.getProx();

        qtdDados--;

        return ret;
    }

    public void push(Dado dado)
    {
        comeco = new No<Dado>(dado, comeco);
        qtdDados++;
    }

    public int getTamanho()
    {
        return qtdDados;
    }
}
