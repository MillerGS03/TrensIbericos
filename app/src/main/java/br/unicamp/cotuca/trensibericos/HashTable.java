/**
 * @author 18178 - Felipe Scherer Vicentin
 * @author 18179 - Gustavo Miller Santos
 */


package br.unicamp.cotuca.trensibericos;

import java.io.Serializable;
import java.util.function.Predicate;

public class HashTable<Dado extends Hashable<Dado, Key>, Key extends Comparable<Key> & Serializable> implements Serializable {
    protected final int TAMANHO_MAX = 10000;
    protected ListaHash[] vet;
    protected int qtdDados;
    protected Class<? extends Dado> classe;

    // A classe é passada em ambos os construtores, pois é necessário obter o hashcode da chave e não é possível criar métodos estáticos abstratos
    // Portanto, é necessário criar uma instância (usando a classe.newInstance()) e chamar o "getKeyHashcode", que não é estático.
    public HashTable(int tamanho, Class<? extends Dado> c)
    {
        vet = new ListaHash[tamanho];
        qtdDados = 0;
        classe = c;
    }
    public HashTable(Class<? extends Dado> c)
    {
        classe = c;
        vet = new ListaHash[TAMANHO_MAX];
        qtdDados = 0;
    }

    protected int hash(Key key)
    {
        try {
            Dado dado = classe.newInstance();
            long ret = dado.getKeyHashcode(key); // Pega o hashCode da chave. Não usamos o padrão "getHashcode()" porque o usuário pode
                                                // querer fazer o próprio hashCode de uma classe já existente, como uma String (nosso caso)

            ret %= vet.length - 1;

            return (int) Math.abs(ret); // retorna o índice do vetor onde o dado precisa estar
        } catch (Exception ex) {
            return -1;
        }
    }

    public void add(Dado dado)
    {
        int index = hash(dado.getKey());
        ListaHash lista = vet[index];

        if (lista == null)
            lista = new ListaHash(); // Como é BucketHash, os dados são adicionados numa lista para evitar que sejam sobrescritos

        lista.add(dado);

        vet[index] = lista;

        qtdDados++;
    }

    public Dado get(Key key)
    {
        int index = hash(key);
        ListaHash lista = vet[index];

        if (lista == null)
            return null;

        return (Dado)lista.find(key); // Procura na lista daquela posição o dado correspondente àquela chave e retorna
    }

    public ListaHash[] getVetor() // Transforma a hashTable em um vetor, com os dados em ordem
    {
        ListaHash[] arr = new ListaHash[qtdDados];
        int ind = 0;

        for (int i = 0; i < vet.length; i++)
        {
            if (vet[i] != null)
                arr[ind++] = vet[i];
        }

        ListaHash[] ret = new ListaHash[ind];

        for(int i = 0; i < ind; i++)
            ret[i] = arr[i];

        return ret;
    }

    public ListaSimples<Key> getKeys() // Retorna todas as chaves
    {
        ListaSimples<Key> keys = new ListaSimples<>();
        ListaHash[] arr = getVetor();
        for(ListaHash<Dado, Key> lista : arr) {
            for (Dado dado : lista)
                keys.add(dado.getKey());
        }

        return keys;
    }

    public ListaSimples<Key> getKeys(Predicate<Dado> predicate) // Retorna apenas as chaves dos dados que cumprem certo requisito
    {
        ListaSimples<Key> keys = new ListaSimples<>();
        ListaHash[] arr = getVetor();
        for(ListaHash<Dado, Key> lista : arr) {
            for (Dado dado : lista) {
                if (predicate.test(dado))
                    keys.add(dado.getKey());
            }
        }

        return keys;
    }

    public int getSize()
    {
        return qtdDados;
    }
}
