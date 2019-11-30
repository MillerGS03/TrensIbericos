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
    LinearLayout llConteudo;
    EditText edtCidade1, edtCidade2, edtDistancia, edtTempo;
    Button btnAdd;

    HashTable<Cidade> cidades;

    String cidade1, cidade2;
    int dist, tempo;

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

        llConteudo = (LinearLayout)findViewById(R.id.llConteudo);
        edtCidade1 = (EditText) findViewById(R.id.edtCidade1);
        edtCidade2 = (EditText) findViewById(R.id.edtCidade2);
        edtDistancia = (EditText)findViewById(R.id.edtDistancia);
        edtTempo = (EditText)findViewById(R.id.edtTempo);
        btnAdd = (Button)findViewById(R.id.btnAdd);

        iniciarFullscreen();

        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        cidades = (HashTable<Cidade>) params.getSerializable("cidades");

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
                if (isOk())
                {
                    Caminho caminho = new Caminho(cidades.get(cidade1), cidades.get(cidade2), dist, tempo);

                    try {
                        OutputStreamWriter escrevedor = new OutputStreamWriter(openFileOutput("GrafoTremEspanhaPortugal.txt", Context.MODE_APPEND));
                        escrevedor.append(caminho.toString());
                        escrevedor.flush();
                        escrevedor.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AdicionarCaminhoActivity.this, "Preencha os campos corretamente!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isOk()
    {
        try {
            cidade1 = edtCidade1.getText().toString().trim();
            cidade2 = edtCidade2.getText().toString().trim();
            dist = Integer.parseInt(edtDistancia.getText().toString().trim());
            tempo = Integer.parseInt(edtTempo.getText().toString().trim());

            if (dist <= 0 || tempo <= 0)
                return false;

            if (cidade1.equals("") || cidade2.equals(""))
                return false;

            if (cidade1.length() > 15 || cidade2.length() > 15)
                return false;

            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
