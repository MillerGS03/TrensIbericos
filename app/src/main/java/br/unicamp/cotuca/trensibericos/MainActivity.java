package br.unicamp.cotuca.trensibericos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
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
    CanvasMapa mapa;
    HashTable<Cidade> cidades;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapa = new CanvasMapa(this);

        llMapa = (LinearLayout)findViewById(R.id.llMapa);
        llMapa.removeAllViews();
        llMapa.addView(mapa);

        try {
            resetarArquivos();
        } catch (IOException ex) {}

        HashTable<Cidade> cidadesLidas = getCidades();
        mapa.setCidades(cidadesLidas);

        Intent intent = new Intent(MainActivity.this, AdicionarCidadeActivity.class);
        startActivity(intent);
    }
}
