package br.unicamp.cotuca.trensibericos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;


public class CanvasMapa extends View {
    protected Context contexto;
    protected HashTable<Cidade, String> cidades;
    protected Bitmap background;
    protected View parent;
    protected ListaSimples<Caminho> caminhos;
    protected Caminho caminhoAtual;
    protected int corPadrao = Color.BLUE;
    protected int principal = 0;

    public CanvasMapa(Context context) {
        super(context);
        contexto = context;
        background = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("mapa", "drawable", context.getPackageName()));
    }

    public void setCidades(HashTable<Cidade, String> cidades)
    {
        this.cidades = cidades;
        invalidate();
    }
    public void setCaminhos(ListaSimples<Caminho> caminhos)
    {
        if (caminhos != null && caminhos.getSize() > 0) {
            this.caminhos = caminhos;
            caminhoAtual = caminhos.get(0);
        }
        else if (this.caminhos != null)
            this.caminhos.reset();
        invalidate();
    }
    public void setPrincipal(int p) {
        principal = p;
    }
    public Caminho getCaminhoAtual() {
        return caminhoAtual;
    }

    private void redimensionar()
    {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, background.getWidth(), background.getHeight()), new RectF(0, 0, getWidth(), getHeight()), Matrix.ScaleToFit.CENTER);
        background =  Bitmap.createBitmap(background, 0, 0, background.getWidth(), background.getHeight(), m, true);

        ViewGroup.LayoutParams lp = parent.getLayoutParams();
        lp.width = background.getWidth();
        lp.height = background.getHeight();
        parent.setLayoutParams(lp);
    }
    public void desenharPontosCidades(Canvas canvas)
    {
        canvas.save();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(corPadrao);

        if (cidades == null)
            return;

        ListaSimples[] listas = cidades.getVetor();

        for(ListaSimples<Cidade> lista : listas) {
            for (Cidade cidade : lista)
                canvas.drawCircle(getWidth() * cidade.getX(), getHeight() * cidade.getY(), 5, paint);
        }

        canvas.restore();
    }
    public void desenharCaminhos(Canvas canvas)
    {
        if (caminhos == null)
            return;

        Paint paint = new Paint();
        float x1 = -1, x2 = -1, y1 = -1, y2 = -1;

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.argb(0x66, 161, 161, 161));

        int ind = 0;

        for (Caminho caminho : caminhos) {
            ListaSimples<Cidade> cidades = caminho.getCidades();
            if (ind != principal) {
                for (Cidade cidade : cidades) {
                    x2 = cidade.getX() * getWidth();
                    y2 = cidade.getY() * getHeight();

                    if (x1 != -1 && x2 != -1 && y1 != -1 && y2 != -1)
                        canvas.drawLine(x1, y1, x2, y2, paint);

                    x1 = cidade.getX() * getWidth();
                    y1 = cidade.getY() * getHeight();
                }
            }
            else
                caminhoAtual = caminho;
            x1 = y1 = x2 = y2 = -1;
            ind++;
        }

        paint.setColor(corPadrao);

        for (Cidade cidade : caminhoAtual.getCidades()) {
            x2 = cidade.getX() * getWidth();
            y2 = cidade.getY() * getHeight();

            if (x1 != -1 && x2 != -1 && y1 != -1 && y2 != -1)
                canvas.drawLine(x1, y1, x2, y2, paint);

            x1 = cidade.getX() * getWidth();
            y1 = cidade.getY() * getHeight();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        parent = (View)getParent();

        redimensionar();

        canvas.drawBitmap(background, 0, 0, null);

        desenharPontosCidades(canvas);
        desenharCaminhos(canvas);
    }
}
