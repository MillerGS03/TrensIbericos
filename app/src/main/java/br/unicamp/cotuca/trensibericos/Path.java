/**
 * @author 18178 - Felipe Scherer Vicentin
 * @author 18179 - Gustavo Miller Santos
 */


package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;

// Classe do objeto retornado pelo método de buscar caminhos
public class Path<Dado extends Serializable & Comparable<Dado>> {
    protected ListaSimples<Dado> path; // Dados (cidades) percorridos
    protected ListaSimples<Object> params; // Distância percorrida (pode ser mais de uma, como distância e tempo, e pode não ser um número, como uma String que tem o percurso concatenado)

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
