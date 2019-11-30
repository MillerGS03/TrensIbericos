package br.unicamp.cotuca.trensibericos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LinearLayout llMapa;
    CanvasMapa mapa;
    HashTable<Cidade> cidades;

    public HashTable<Cidade> getCidades()
    {
        HashTable<Cidade> ret = new HashTable<Cidade>();
        BufferedReader leitor = null;

        try {
            leitor = new BufferedReader(new InputStreamReader(getAssets().open("Cidades.txt")));
            String linha = null;

            while ((linha = leitor.readLine()) != null) {
                ret.add(new Cidade(linha));
            }
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return ret;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapa = new CanvasMapa(this);

        llMapa = (LinearLayout)findViewById(R.id.llMapa);
        llMapa.removeAllViews();
        llMapa.addView(mapa);

        HashTable<Cidade> cidadesLidas = getCidades();
    }
}
