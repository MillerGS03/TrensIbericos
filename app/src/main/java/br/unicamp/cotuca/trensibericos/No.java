/**
 * @author 18178 - Felipe Scherer Vicentin
 * @author 18179 - Gustavo Miller Santos
 */


package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;

public class No<T> implements Serializable
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

    public No<T> getProx() {
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
}