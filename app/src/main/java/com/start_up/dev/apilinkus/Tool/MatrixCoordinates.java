package com.start_up.dev.apilinkus.Tool;

import android.graphics.Matrix;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Huong on 22/01/2017.
 */

public class MatrixCoordinates {
    public static void logImageViewMatrixInfos(Matrix matrix, ImageView imageView) {
        float[] values = new float[9];
        matrix.getValues(values);
        float globalX = values[2];
        float globalY = values[5];
        float width = values[0]* imageView.getWidth();
        float height = values[4] * imageView.getHeight();

        Log.i("Log value", "Image Details: \nxPos: " + globalX + " \nyPos: " + globalY + "\nwidth: " + width + " \nheight: " + height);
    }

    public static float getXValueFromMatrix(Matrix matrix) {

        float[] values = new float[9];
        matrix.getValues(values);
        float globalX = values[2];

        return globalX;
    }

    public static float getYValueFromMatrix(Matrix matrix) {

        float[] values = new float[9];
        matrix.getValues(values);
        float globalY = values[5];

        return globalY;
    }

    public static float getWidthFromMatrix(Matrix matrix, ImageView imageview) {

        float[] values = new float[9];
        matrix.getValues(values);

        float width = values[0]* imageview.getWidth();

        return width;
    }

    public static float getHeightFromMatrix(Matrix matrix, ImageView imageview) {

        float[] values = new float[9];
        matrix.getValues(values);

        float height = values[4] * imageview.getHeight();

        return height;
    }
}
