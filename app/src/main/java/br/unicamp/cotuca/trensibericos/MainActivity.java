package br.unicamp.cotuca.trensibericos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LinearLayout llMapa;
    FrameLayout conteudo;
    Button btnAddCidade, btnAddCaminho;

    CanvasMapa mapa;
    HashTable<Cidade> cidades;

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

    public HashTable<Cidade> getCidades()
    {
        HashTable<Cidade> ret = new HashTable<Cidade>();
        BufferedReader leitor = null;

        try {
            InputStream is = openFileInput("Cidades.txt");

            if (is != null) {
                leitor = new BufferedReader(new InputStreamReader(is));
                String linha = "";

                while ((linha = leitor.readLine()) != null)
                    ret.add(new Cidade(linha));

                is.close();
            }
        }
        catch (FileNotFoundException ex) {
            try {
                resetarArquivos();

                return getCidades();
            } catch (IOException ioEx) {
                Toast.makeText(this, ioEx.getMessage(), Toast.LENGTH_LONG).show();
            }
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return ret;
    }

    public ListaSimples<Caminho> getCaminhos(HashTable<Cidade> hash) {
        ListaSimples<Caminho> caminhos = new ListaSimples<Caminho>();

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

        iniciarFullscreen();

        llMapa.removeAllViews();
        llMapa.addView(mapa);

        try {
            resetarArquivos();

            cidades = getCidades();
            ListaSimples<Caminho> caminhos = getCaminhos(cidades);
            mapa.setCidades(cidades);
        } catch (IOException ex) {}

        btnAddCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AdicionarCidadeActivity.class);
                startActivity(intent);
            }
        });

        btnAddCaminho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AdicionarCaminhoActivity.class);
                Bundle params = new Bundle();
                params.putSerializable("cidades", cidades);
                intent.putExtras(params);

                startActivity(intent);
            }
        });
    }
}
