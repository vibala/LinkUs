package com.start_up.dev.apilinkus.Tool;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by Huong on 28/01/2017.
 */

public class CircleTransform {
    public CircleTransform(){

    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    public  Bitmap transform(Bitmap source,int resize) {
        float coeff=(float)(source.getWidth())/(float)(source.getHeight());
        Bitmap source_resized;
        if(coeff<1)
            source_resized=scaleBitmap(source, resize, (int)(coeff*(float)resize));
        else
            source_resized=scaleBitmap(source, (int)(coeff*(float)resize), resize);
        source.recycle();

        int size = Math.min(source_resized.getWidth(), source_resized.getHeight());

        int x = (source_resized.getWidth() - size) / 2;
        int y = (source_resized.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source_resized, x, y, size, size);

        if (squaredBitmap != source_resized) {
            source_resized.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source_resized.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
    }
}