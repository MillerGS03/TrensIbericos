package br.unicamp.cotuca.trensibericos;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class CanvasMapa extends View {
    private Context contexto;
    private ArrayList<Cidade> cidades;
    private Bitmap background;
    private View parent;

    public CanvasMapa(Context context) {
        super(context);
        contexto = context;
        background = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("mapa", "drawable", context.getPackageName()));
    }

    public void setCidades(ArrayList<Cidade> cidades)
    {
        this.cidades = cidades;
        invalidate();
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
        paint.setColor(Color.BLACK);

        if (cidades == null)
            return;

        for(Cidade cidade : cidades)
            canvas.drawCircle(cidade.getX(), cidade.getY(), 2, paint);

        canvas.restore();
    }
    public void desenharCaminhos(Canvas canvas)
    {
        //
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
