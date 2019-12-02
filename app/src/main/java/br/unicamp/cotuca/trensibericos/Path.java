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
            add = ((Character)data) + ((Character)params.get(index));
        } else if (data instanceof String) {
            add = ((String)data) + ((String)params.get(index));
        } else if (data instanceof Number) {
            add = ((Double)data) + ((Double)params.get(index));
        }
        params.set(add, index);
    }
    public void addToPath(Dado dado) {
        path.add(dado);
    }
}
