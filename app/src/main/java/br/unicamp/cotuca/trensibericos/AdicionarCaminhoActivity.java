/**
 * @author 18178 - Felipe Scherer Vicentin
 * @author 18179 - Gustavo Miller Santos
 */


package br.unicamp.cotuca.trensibericos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class AdicionarCaminhoActivity extends AppCompatActivity {
    //objetos dos elementos gráficos
    LinearLayout llConteudo;
    EditText edtCidade1, edtCidade2, edtDistancia, edtTempo;
    Button btnAdd, btnVoltar;

    //objetos passados como parâmetro para a página atual
    HashTable<Cidade, String> cidades;
    ListaSimples<Caminho> caminhosAtuais;

    //método para deixar a página em fullscreen
    private void iniciarFullscreen()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                llConteudo.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }, 300);
    }

    //fecha o teclado do celular
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_caminho);

        //atribuição de objetos da GUI
        llConteudo = (LinearLayout)findViewById(R.id.llConteudo);
        edtCidade1 = (EditText) findViewById(R.id.edtCidade1);
        edtCidade2 = (EditText) findViewById(R.id.edtCidade2);
        edtDistancia = (EditText)findViewById(R.id.edtDistancia);
        edtTempo = (EditText)findViewById(R.id.edtTempo);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnVoltar = (Button)findViewById(R.id.btnVoltar);

        iniciarFullscreen();

        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        cidades = (HashTable<Cidade, String>) params.getSerializable("cidades"); //parâmetros passados
        caminhosAtuais = (ListaSimples<Caminho>) params.getSerializable("caminhos");

        //ao clicar em algum lugar da tela, o teclado some
        llConteudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    hideKeyboard(AdicionarCaminhoActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Caminho caminho;
                if ((caminho = getCaminho()) != null) //se alguma coisa que o usuário inseriu estiver incorreta, getCaminho() retornará null
                {
                    try {
                        //abre-se o arquivo para escrita no armazenamento interno e salva-se o novo caminho
                        OutputStreamWriter escrevedor = new OutputStreamWriter(openFileOutput("GrafoTremEspanhaPortugal.txt", Context.MODE_APPEND));
                        escrevedor.append(caminho.toString() + "\n");
                        escrevedor.flush();
                        escrevedor.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        //volta à tela inicial
                        Intent intent = new Intent(AdicionarCaminhoActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(AdicionarCaminhoActivity.this, "Preencha os campos corretamente!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //se o usuário clicar por engano, ele poderá retornar à tela principal ao clicar no botão "Voltar"
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdicionarCaminhoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //retorna o novo caminho caso as informações estiverem corretas. Caso contrário, retornará null
    private Caminho getCaminho()
    {
        try {
            //variáveis do caminho
            String cidade1 = edtCidade1.getText().toString().trim();
            String cidade2 = edtCidade2.getText().toString().trim();
            int dist = Integer.parseInt(edtDistancia.getText().toString().trim());
            int tempo = Integer.parseInt(edtTempo.getText().toString().trim());

            Caminho caminho = new Caminho(cidades.get(cidade1), cidades.get(cidade2), dist, tempo);

            if (dist <= 0 || tempo <= 0) //verifica os valores da distancia e do tempo
                return null;

            if (cidade1.equals("") || cidade2.equals("")) //verifica os nomes das cidades
                return null;

            if (cidade1.length() > 15 || cidade2.length() > 15) //verifica se está dentro do tamanho estipulado
                return null;

            for (Caminho c : caminhosAtuais) {
                if (c.getCidades().equals(caminho.getCidades())) { //se já houver um caminho que liga essas duas cidades, o novo caminho não deve ser adicionado
                    return null;
                }
            }

            return caminho;
        } catch (Exception ex) { //qualquer exceção significa erro de informações por parte do usuário
            return null;
        }
    }
}
