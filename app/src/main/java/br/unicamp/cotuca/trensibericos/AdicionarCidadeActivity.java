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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class AdicionarCidadeActivity extends AppCompatActivity {
    //objetos da GUI
    LinearLayout llConteudo;
    EditText edtNome, edtX, edtY;
    Button btnAdd, btnVoltar;

    //HashTable é passada como parâmetro para a página
    HashTable<Cidade, String> cidades;

    //faz a página atual ficar em fullscreen
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
        setContentView(R.layout.activity_adicionar_cidade);

        llConteudo = (LinearLayout)findViewById(R.id.llConteudo);
        edtNome = (EditText)findViewById(R.id.edtNome);
        edtX = (EditText)findViewById(R.id.edtX);
        edtY = (EditText)findViewById(R.id.edtY);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnVoltar = (Button)findViewById(R.id.btnVoltar);

        iniciarFullscreen();

        //se o usuário clicar em qualquer lugar da tela, o seu teclado fechará
        llConteudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    hideKeyboard(AdicionarCidadeActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle params = getIntent().getExtras();
        cidades = (HashTable<Cidade, String>) params.getSerializable("cidades");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cidade cidade = getCidade(); //getCidade() retornará null caso as informações inseridas estiverem erradas
                if (cidade != null)
                {
                    try {
                        adicionarCidade(cidade); //adiciona a cidade no arquivo texto
                    } catch (Exception ex) {
                        Toast.makeText(AdicionarCidadeActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    } finally {
                        //volta à tela inicial
                        Intent intent = new Intent(AdicionarCidadeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(AdicionarCidadeActivity.this, "Preencha os campos corretamente!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //volta à tela inicial
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdicionarCidadeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //retorna o id da última cidade inserida
    private int getLastId() throws IOException
    {
        BufferedReader leitor = null;
        InputStream is = openFileInput("Cidades.txt"); //abre o arquivo texto do armazenamento interno

        if (is != null) {
            leitor = new BufferedReader(new InputStreamReader(is));
            String ultimaLinha = null, linhaAtual = null;

            while ((linhaAtual = leitor.readLine()) != null)
                ultimaLinha = linhaAtual;

            is.close();

            Cidade ultima = new Cidade(ultimaLinha); //instancia a cidade com a última linha salva (última cidade)

            return ultima.getId(); //retorna o seu id
        }
        return -2; //retorna -2, porque quando for ser somado para atribuir o id da nova cidade, o novo id será -1 (-2 + 1)
    }

    //retorna a nova cidade e, caso as informações estiverem erradas, null
    private Cidade getCidade()
    {
        try {
            String nome = edtNome.getText().toString();
            float x = Float.parseFloat(edtX.getText().toString());
            float y = Float.parseFloat(edtY.getText().toString());

            Cidade cidade = new Cidade(-1, nome, x, y);

            if (nome.trim().equals("") || nome.length() > 15) //se o nome for inválido
                return null;

            if (x > 1 || x < 0) //se a cordenada x estiver errada
                return null;

            if (y > 1 || y < 0) //se a cordenada y estiver errada
                return null;

            for (ListaHash<Cidade, String> lista : cidades.getVetor()) {
                for (Cidade c : lista) {
                    if (c.getNome().equals(nome)) //se já houver uma cidade de mesmo nome, a nova cidade não deverá ser adicionada
                        return null;
                }
            }

            cidade.setId(getLastId() + 1); //novo id é o id da última cidade + 1

            return cidade;
        } catch (IOException e) {return null;} //qualquer situação de exceção é sinônimo de erro de entrada por parte do usuário
    }

    private void adicionarCidade(Cidade cidade)
    {
        try {
            //abre o arquivo texto e salva lá
            OutputStreamWriter escrevedor = new OutputStreamWriter(openFileOutput("Cidades.txt", Context.MODE_APPEND));
            escrevedor.append(cidade.toString() + "\n");
            escrevedor.flush();
            escrevedor.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
