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

    public void addToParams(Object data, int index) {
        Object add = params.get(index);
        if (data instanceof Character) {
            add = ((Character)data) + ((Character)add);
        } else if (data instanceof Number) {
            add = ((Number)data).doubleValue() + ((Number)add).doubleValue();
        } else {
            add = data.toString() + add.toString();
        }
        params.set(add, index);
    }
}
