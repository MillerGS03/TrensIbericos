package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;

public class Path<Dado extends Serializable & Comparable<Dado>> {
    protected ListaSimples<Dado> path;
    protected ListaSimples<Object> params;

    public ListaSimples<Dado> getPath() {
        return path;
    }

    public ListaSimples<Object> getParams() {
        return  params;
    }

    public Path() {
        path = new ListaSimples<>();
        params = new ListaSimples<>();
    }
}
