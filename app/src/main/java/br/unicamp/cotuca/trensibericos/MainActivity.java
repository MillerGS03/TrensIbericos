/**
 * @author 18178 - Felipe Scherer Vicentin
 * @author 18179 - Gustavo Miller Santos
 */


package br.unicamp.cotuca.trensibericos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    //objetos da GUI
    LinearLayout llMapa;
    FrameLayout conteudo;
    Button btnAddCidade, btnAddCaminho, btnBuscar, btnCurto, btnRapido;
    Spinner sDe, sPara;
    TextView tvDistancia, tvTempo;

    CanvasMapa mapa; //objeto que representa o mapa na GUI
    Grafo<Cidade> grafo;

    //region operacoes IO

    //reseta os arquivos do armazenamento interno com base nos arquivos fornecidos para o projeto que se encontram na pasta assets
    private void resetarArquivos() throws IOException
    {
        for(File file : getFilesDir().listFiles())
            file.delete();

        OutputStreamWriter escrevedor = new OutputStreamWriter(openFileOutput("Cidades.txt", Context.MODE_PRIVATE)); //arquivo do armazenamento interno é aberto para escrita
        String data = "";

        BufferedReader leitor = new BufferedReader(new InputStreamReader(getAssets().open("Cidades.txt"))); //arquivo do assets é aberto para leitura
        String linha = "";

        while ((linha = leitor.readLine()) != null) { //para cada linha lida, adiciona-se à data
            data += linha + "\n";
        }

        //ao final do while, data contém todo o arquivo de assets
        escrevedor.write(data);
        escrevedor.flush();
        escrevedor.close();

        //a mesma coisa é feita, porém com o arquivo de ligações
        escrevedor = new OutputStreamWriter(openFileOutput("GrafoTremEspanhaPortugal.txt", Context.MODE_PRIVATE));
        data = "";

        leitor = new BufferedReader(new InputStreamReader(getAssets().open("GrafoTremEspanhaPortugal.txt")));
        linha = "";

        while ((linha = leitor.readLine()) != null) {
            data += linha + "\n";
        }

        escrevedor.write(data);
        escrevedor.flush();
        escrevedor.close();
    }

    //retorna as cidades e adiciona-as na lista passada como parâmetro
    public HashTable<Cidade, String> getCidades(ListaSimples<Cidade> listaCidades)
    {
        HashTable<Cidade, String> ret = new HashTable<>(Cidade.class); //instancia da HashTable
        BufferedReader leitor = null;

        try {
            InputStream is = openFileInput("Cidades.txt"); //arquivo do armazenamento interno aberto

            if (is != null) {
                leitor = new BufferedReader(new InputStreamReader(is, "UTF8")); //formatação UTF8 para suportar acentos
                String linha = "";

                while ((linha = leitor.readLine()) != null) {
                    Cidade c = new Cidade(linha); //cada linha é transformada em uma cidade e adicionada tanto à hashTable quanto a lista passada como parâmetro
                    ret.add(c);
                    listaCidades.add(c);
                }

                is.close();
            }
        }
        catch (FileNotFoundException ex) {
            try {
                resetarArquivos(); //caso o arquivo não exista, ele deve ser resetado (criado)

                return getCidades(listaCidades);
            } catch (IOException ioEx) {
                Toast.makeText(this, ioEx.getMessage(), Toast.LENGTH_LONG).show();
            }
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return ret; //retorna a HashTable completa
    }

    //retorna uma lista com todos os caminhos
    public ListaSimples<Caminho> getCaminhos(HashTable<Cidade, String> hash) {
        ListaSimples<Caminho> caminhos = new ListaSimples<>();

        BufferedReader leitor = null;

        try {
            InputStream is = openFileInput("GrafoTremEspanhaPortugal.txt");

            if (is != null) {
                leitor = new BufferedReader(new InputStreamReader(is));
                String linha = "";

                while ((linha = leitor.readLine()) != null)
                    caminhos.add(new Caminho(linha, hash)); //cada linha contém uma ligação, ou seja, um caminho

                is.close();
            }
        }
        catch (FileNotFoundException ex) {
            try {
                resetarArquivos(); //caso o arquivo não exista, criá-lo

                return getCaminhos(hash);
            } catch (IOException ioEx) {
                Toast.makeText(this, ioEx.getMessage(), Toast.LENGTH_LONG).show();
            }
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return caminhos;
    }
    //endregion

    private void iniciarFullscreen()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                conteudo.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }, 300);

        conteudo.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                conteudo.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapa = new CanvasMapa(this);

        conteudo = (FrameLayout)findViewById(R.id.conteudo);
        llMapa = (LinearLayout)findViewById(R.id.llMapa);
        btnAddCidade = (Button)findViewById(R.id.btnAddCidade);
        btnAddCaminho = (Button)findViewById(R.id.btnAddCaminho);
        sDe = (Spinner)findViewById(R.id.spinnerDe);
        sPara = (Spinner)findViewById(R.id.spinnerPara);
        btnBuscar = (Button)findViewById(R.id.btnBuscar);
        btnCurto = (Button)findViewById(R.id.btnCurto);
        btnRapido = (Button)findViewById(R.id.btnRapido);
        tvDistancia = (TextView)findViewById(R.id.tvDistancia);
        tvTempo = (TextView)findViewById(R.id.tvTempo);

        iniciarFullscreen();

        llMapa.removeAllViews();
        llMapa.addView(mapa);

        grafo = new Grafo<>();

        HashTable<Cidade, String> cidades = new HashTable<>(Cidade.class); //HashTable de cidades iniciada
        ListaSimples<Caminho> caminhos = new ListaSimples<>(); //Lista de caminhos iniciada

        try {
            ListaSimples<Cidade> listaCidades = new ListaSimples<>();
            cidades = getCidades(listaCidades);
            grafo.setDados(listaCidades); //a lista de cidades preenhida é usada para adicionar as Cidades no grafo

            caminhos = getCaminhos(cidades); //caminhos são lidos

            for (Caminho caminho : caminhos) { //para cada caminho, criar uma ligação no grafo entre as respectivas cidades e passamos as variáveis de ordenação (distancia e tempo)
                grafo.setLigacao(caminho.getCidades().get(0).getId(), caminho.getCidades().get(1).getId(),
                        caminho.getDistancia(), caminho.getTempo());

                grafo.setLigacao(caminho.getCidades().get(1).getId(), caminho.getCidades().get(0).getId(),
                        caminho.getDistancia(), caminho.getTempo());
            }
        } catch (Exception ex) {
            try {
                resetarArquivos();
            } catch (IOException e) {}
            ex.printStackTrace();
        }

        final HashTable<Cidade, String> hashCidades = cidades; //constantes para serem usadas dentro de eventos
        final ListaSimples<Caminho> listaCaminhos = caminhos;

        mapa.setCidades(cidades);

        ListaSimples<String> nomes = cidades.getKeys(); //retorna os nomes de todas as cidades para serem adicionadas às listas da GUI (Spinners)

        String[] nomesArr = nomes.toArray(String[].class);
        Arrays.sort(nomesArr);

        sDe.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nomesArr));
        sPara.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nomesArr));

        //abre a página de adição de cidade
        btnAddCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AdicionarCidadeActivity.class);
                Bundle params = new Bundle();
                params.putSerializable("cidades", hashCidades);
                intent.putExtras(params);

                startActivity(intent);
            }
        });

        //abre a página de adição de caminho
        btnAddCaminho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AdicionarCaminhoActivity.class);
                Bundle params = new Bundle();
                params.putSerializable("cidades", hashCidades);
                params.putSerializable("caminhos", listaCaminhos);
                intent.putExtras(params);

                startActivity(intent);
            }
        });

        //buscar os caminhos entre duas cidades escolhidas
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GUI é resetada
                btnRapido.setVisibility(View.INVISIBLE);
                btnCurto.setVisibility(View.INVISIBLE);
                tvDistancia.setText("");
                tvTempo.setText("");
                mapa.setCaminhos(null);

                //nomes das cidades escolhidas
                String c1 = sDe.getSelectedItem().toString();
                String c2 = sPara.getSelectedItem().toString();

                //pesquisa no grafo retornará dois Path<Cidade> em lista
                //um para cada atributo de ordenação passado (distancia e tempo), caso haja caminho
                ListaSimples<Path<Cidade>> res = grafo.getPaths(hashCidades.get(c1).getId(), hashCidades.get(c2).getId());

                //se houver caminhos
                if (res.getSize() > 0) {
                    exibirCaminhos(res); //eles são adicionados ao mapa
                    //e, por padrão, o caminho mais curto é mostrado primeiro
                    btnRapido.setVisibility(View.VISIBLE);
                    btnCurto.setVisibility(View.VISIBLE);
                    tvDistancia.setText("Distância: " + mapa.getCaminhoAtual().getDistancia());
                    tvTempo.setText("Tempo: " + mapa.getCaminhoAtual().getTempo());
                } else {
                    Toast.makeText(MainActivity.this, "Não foram encontrados caminhos de " + c1 + " para " + c2, Toast.LENGTH_LONG).show();
                }
            }
        });

        //mostra o caminho mais curto e atualiza a GUI
        btnCurto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapa.setPrincipal(0);
                tvDistancia.setText("Distância: " + mapa.getCaminhoAtual().getDistancia());
                tvTempo.setText("Tempo: " + mapa.getCaminhoAtual().getTempo());
            }
        });

        //mostra o caminho mais rápido e atualiza a GUI
        btnRapido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapa.setPrincipal(1);
                tvDistancia.setText("Distância: " + mapa.getCaminhoAtual().getDistancia());
                tvTempo.setText("Tempo: " + mapa.getCaminhoAtual().getTempo());
            }
        });
    }

    //adiciona todos os Path<Cidade> retornados do grafo ao mapa
    private void exibirCaminhos(ListaSimples<Path<Cidade>> res) {
        ListaSimples<Caminho> caminhos = new ListaSimples<>();

        for (Path<Cidade> path : res) {
            Caminho caminho = new Caminho();
            caminho.setCidades(path.getPath());
            caminho.setDistancia((double)path.getParams().get(0)); //parametro 0 é distancia e 1 é tempo (ordem de passagem quando adicionamos ligações ao grafo
            caminho.setTempo((double)path.getParams().get(1));
            caminhos.add(caminho);
        }

        mapa.setCaminhos(caminhos);
    }
}
