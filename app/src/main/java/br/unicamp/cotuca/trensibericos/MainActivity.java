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

public class MainActivity extends AppCompatActivity {
    LinearLayout llMapa;
    FrameLayout conteudo;
    Button btnAddCidade, btnAddCaminho, btnBuscar, btnCurto, btnRapido;
    Spinner sDe, sPara;
    TextView tvDistancia, tvTempo;

    CanvasMapa mapa;
    Grafo<Cidade> grafo;

    //region operacoes IO

    private void resetarArquivos() throws IOException
    {
        for(File file : getFilesDir().listFiles())
            file.delete();

        OutputStreamWriter escrevedor = new OutputStreamWriter(openFileOutput("Cidades.txt", Context.MODE_PRIVATE));
        String data = "";

        BufferedReader leitor = new BufferedReader(new InputStreamReader(getAssets().open("Cidades.txt")));
        String linha = "";

        while ((linha = leitor.readLine()) != null) {
            data += linha + "\n";
        }

        escrevedor.write(data);
        escrevedor.flush();
        escrevedor.close();

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

    public HashTable<Cidade, String> getCidades(ListaSimples<Cidade> listaCidades)
    {
        HashTable<Cidade, String> ret = new HashTable<>(Cidade.class);
        BufferedReader leitor = null;

        try {
            InputStream is = openFileInput("Cidades.txt");

            if (is != null) {
                leitor = new BufferedReader(new InputStreamReader(is, "UTF8"));
                String linha = "";

                while ((linha = leitor.readLine()) != null) {
                    Cidade c = new Cidade(linha);
                    ret.add(c);
                    listaCidades.add(c);
                }

                is.close();
            }
        }
        catch (FileNotFoundException ex) {
            try {
                resetarArquivos();

                return getCidades(listaCidades);
            } catch (IOException ioEx) {
                Toast.makeText(this, ioEx.getMessage(), Toast.LENGTH_LONG).show();
            }
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return ret;
    }

    public ListaSimples<Caminho> getCaminhos(HashTable<Cidade, String> hash) {
        ListaSimples<Caminho> caminhos = new ListaSimples<>();

        BufferedReader leitor = null;

        try {
            InputStream is = openFileInput("GrafoTremEspanhaPortugal.txt");

            if (is != null) {
                leitor = new BufferedReader(new InputStreamReader(is));
                String linha = "";

                while ((linha = leitor.readLine()) != null)
                    caminhos.add(new Caminho(linha, hash));

                is.close();
            }
        }
        catch (FileNotFoundException ex) {
            try {
                resetarArquivos();

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

        HashTable<Cidade, String> cidades = new HashTable<>(Cidade.class);
        ListaSimples<Caminho> caminhos = new ListaSimples<>();

        try {
            resetarArquivos();

            ListaSimples<Cidade> listaCidades = new ListaSimples<Cidade>();
            cidades = getCidades(listaCidades);
            grafo.setDados(listaCidades);

            caminhos = getCaminhos(cidades);
            for (Caminho caminho : caminhos) {
                grafo.setLigacao(caminho.getCidades().get(0).getId(), caminho.getCidades().get(1).getId(),
                        caminho.getDistancia(), caminho.getTempo());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final HashTable<Cidade, String> hashCidades = cidades;
        final ListaSimples<Caminho> listaCaminhos = caminhos;

        mapa.setCidades(cidades);

        ListaSimples<String> nomes = cidades.getKeys();

        sDe.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nomes.toArray(String[].class)));
        sPara.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nomes.toArray(String[].class)));

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

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRapido.setVisibility(View.INVISIBLE);
                btnCurto.setVisibility(View.INVISIBLE);
                tvDistancia.setText("");
                tvTempo.setText("");
                mapa.setCaminhos(null);

                String c1 = sDe.getSelectedItem().toString();
                String c2 = sPara.getSelectedItem().toString();

                ListaSimples<Path<Cidade>> res = grafo.getPaths(hashCidades.get(c1).getId(), hashCidades.get(c2).getId());
                if (res.getSize() > 0) {
                    exibirCaminhos(res);
                    btnRapido.setVisibility(View.VISIBLE);
                    btnCurto.setVisibility(View.VISIBLE);
                    tvDistancia.setText("Distância: " + mapa.getCaminhoAtual().getDistancia());
                    tvTempo.setText("Tempo: " + mapa.getCaminhoAtual().getTempo());
                } else {
                    Toast.makeText(MainActivity.this, "Não foram encontrados caminhos de " + c1 + " para " + c2, Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCurto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapa.setPrincipal(0);
                tvDistancia.setText("Distância: " + mapa.getCaminhoAtual().getDistancia());
                tvTempo.setText("Tempo: " + mapa.getCaminhoAtual().getTempo());
            }
        });

        btnRapido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapa.setPrincipal(1);
                tvDistancia.setText("Distância: " + mapa.getCaminhoAtual().getDistancia());
                tvTempo.setText("Tempo: " + mapa.getCaminhoAtual().getTempo());
            }
        });
    }

    private void exibirCaminhos(ListaSimples<Path<Cidade>> res) {
        ListaSimples<Caminho> caminhos = new ListaSimples<>();
        for (Path<Cidade> path : res) {
            Caminho caminho = new Caminho();
            caminho.setCidades(path.getPath());
            caminho.setDistancia((double)path.getParams().get(0));
            caminho.setTempo((double)path.getParams().get(1));
            caminhos.add(caminho);
        }

        mapa.setCaminhos(caminhos);
    }
}
