package com.cam2pdf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;


public class CannyEdge {

    public final int WHITE = Color.WHITE;
    public final int BLACK = Color.BLACK;

    private Bitmap bitmap;
    private Bitmap gray_bitmap;

    public CannyEdge(Bitmap bitmap_){
        bitmap = bitmap_;
        gray_bitmap = toGrayScale(bitmap_);
    }

    private Bitmap toGrayScale(Bitmap bmp){         // grayscale funzionante
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        // sono i valori per cui vengono moltiplicati i valori di r, g e b per trovare il valore di grigio di quel pixel
        double GS_RED = 0.299;      // grayscale di red
        double GS_GREEN = 0.587;    // grayscale di green
        double GS_BLUE = 0.114;     // grayscale di blu
        int pixel;

        // qui salviamo i valori di alfa, red, green e blu di ogni pixel
        int A, R, G, B;

        Bitmap bitmap_gray_scale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get one pixel color
                pixel = bmp.getPixel(x, y);

                // salviamo i valori di alfa, r, g & b di ogni pixel
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                // facciamo le nostre operazioni algebriche per trasformare ogni pixel in scala di grigio
                R = G = B = (int) (R * GS_RED  + G * GS_GREEN + B * GS_BLUE);
                // aggiorniamo la bitmap di output
                bitmap_gray_scale.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bitmap_gray_scale;
    }

    public Bitmap detectEdges(){
        Bitmap cannyEdgeBmp = Bitmap.createBitmap(gray_bitmap);

        int width = gray_bitmap.getWidth();
        int height = gray_bitmap.getHeight();

        // code that detects edges (experimental)

        int prev_horizontal_color;
        int prev_vertical_color;

        int horizontal_color;
        int vertical_color;

        int threshold = 65536 * 10;

        for (int y = 1; y < height; y++){
            for (int x = 1; x < width; x++){
                prev_horizontal_color = Math.abs(gray_bitmap.getPixel(x - 1, y));
                prev_vertical_color = Math.abs(gray_bitmap.getPixel(x, y - 1));
                horizontal_color = Math.abs(gray_bitmap.getPixel(x, y));
                vertical_color = Math.abs(gray_bitmap.getPixel(x, y-1));
                if (Math.abs(prev_horizontal_color - horizontal_color) > threshold || Math.abs(prev_vertical_color - vertical_color) > threshold){
                    cannyEdgeBmp.setPixel(x, y, WHITE);
                    prev_horizontal_color = horizontal_color;
                    prev_vertical_color = vertical_color;
                }else{
                    cannyEdgeBmp.setPixel(x, y, BLACK);
                }
            }
        }

        return cannyEdgeBmp;
    }




}
