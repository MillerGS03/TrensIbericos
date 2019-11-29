package br.unicamp.cotuca.trensibericos;

import java.util.Objects;

public class Cidade {
    private int id;
    private String nome;
    private double x, y;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String toString()
    {
        return String.format("%i - %s - (%f, %f)", getId(), getNome(), getX(), getY());
    }

    public Cidade()
    {
        setId(-1);
        setNome("");
        setX(0);
        setY(0);
    }

    public Cidade(int id, String nome, double x, double y)
    {
        setId(id);
        setNome(nome);
        setX(x);
        setY(y);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cidade)) return false;
        Cidade cidade = (Cidade) o;
        return getId() == cidade.getId() &&
                Double.compare(cidade.getX(), getX()) == 0 &&
                Double.compare(cidade.getY(), getY()) == 0 &&
                getNome().equals(cidade.getNome());
    }

    public int hashCode() {
        return Objects.hash(getId(), getNome(), getX(), getY());
    }
}
