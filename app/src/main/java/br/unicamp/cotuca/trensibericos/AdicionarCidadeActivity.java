package br.unicamp.cotuca.trensibericos;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;


public class AdicionarCidadeActivity extends AppCompatActivity {

    LinearLayout llConteudo;
    EditText edtNome, edtX, edtY;
    Button btnAdd;

    String nome;
    float x, y;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_cidade);

        llConteudo = (LinearLayout)findViewById(R.id.llConteudo);
        edtNome = (EditText)findViewById(R.id.edtNome);
        edtX = (EditText)findViewById(R.id.edtX);
        edtY = (EditText)findViewById(R.id.edtY);
        btnAdd = (Button)findViewById(R.id.btnAdd);

        iniciarFullscreen();

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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOk())
                {
                    Cidade cidade = new Cidade(-1, nome, x, y);
                    BufferedReader leitor = null;

                    try {
                        InputStream is = openFileInput("Cidades.txt");

                        if (is != null) {
                            leitor = new BufferedReader(new InputStreamReader(is));
                            String ultimaLinha = null, linhaAtual = null;

                            while ((linhaAtual = leitor.readLine()) != null)
                                ultimaLinha = linhaAtual;

                            is.close();

                            Cidade ultima = new Cidade(ultimaLinha);
                            cidade.setId(ultima.getId() + 1);

                            adicionarCidade(cidade);

                            Intent intent = new Intent(AdicionarCidadeActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    } catch (IOException ex) {
                        Toast.makeText(AdicionarCidadeActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean isOk()
    {
        try {
            nome = edtNome.getText().toString();
            x = Float.parseFloat(edtX.getText().toString());
            y = Float.parseFloat(edtY.getText().toString());

            if (nome.trim().equals(""))
                return false;

            if (x > 1 || x < 0)
                return false;

            if (y > 1 || y < 0)
                return false;

            return true;
        } catch (Exception e) {return false;}
    }

    private void adicionarCidade(Cidade cidade)
    {
        try {
            OutputStreamWriter escrevedor = new OutputStreamWriter(openFileOutput("Cidades.txt", Context.MODE_APPEND));
            escrevedor.append(cidade.toString());
            escrevedor.flush();
            escrevedor.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
