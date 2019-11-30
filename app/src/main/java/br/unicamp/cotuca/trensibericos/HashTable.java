package br.unicamp.cotuca.trensibericos;

public class HashTable<Dado extends Keyable> {
    protected final int TAMANHO_MAX = 10000;
    ListaSimples[] vet;
    int qtdDados;

    public HashTable(int tamanho)
    {
        vet = new ListaSimples[tamanho];
        qtdDados = 0;
    }
    public HashTable()
    {
        vet = new ListaSimples[TAMANHO_MAX];
        qtdDados = 0;
    }

    protected int hash(String key)
    {
        long ret = 37;

        for (byte b : key.getBytes())
            ret += 2003 * ret + b; //2003 é primo!

        ret %= vet.length - 1;

        return (int)Math.abs(ret);
    }

    public void add(Dado dado)
    {
        int index = hash(dado.getKey());
        ListaSimples lista = vet[index];

        if (lista == null)
            lista = new ListaSimples();

        lista.add(dado);

        vet[index] = lista;

        qtdDados++;
    }

    public Dado get(String key)
    {
        int index = hash(key);
        ListaSimples lista = vet[index];

        if (lista == null)
            return null;

        return (Dado)lista.find(key);
    }
}
