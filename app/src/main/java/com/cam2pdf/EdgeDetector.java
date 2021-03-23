package com.cam2pdf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;


public class EdgeDetector {

    public final int WHITE = Color.WHITE;
    public final int BLACK = Color.BLACK;

    private Bitmap bitmap;
    private Bitmap gray_bitmap;

    public EdgeDetector(Bitmap bitmap_){
        bitmap = bitmap_;
        gray_bitmap = toGrayScale(bitmap_);
    }

    private Bitmap toGrayScale(Bitmap bitmap_){
        int width = bitmap_.getWidth();
        int height = bitmap_.getHeight();

        Bitmap bitmap_gray_scale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap_gray_scale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bitmap_, 0, 0, paint);
        return bitmap_gray_scale;
    }

    public Bitmap toGrayScale(){
        return gray_bitmap;
    }

    public Bitmap detectEdges(){
        Bitmap edges = Bitmap.createBitmap(gray_bitmap);

        int width = gray_bitmap.getWidth();
        int height = gray_bitmap.getHeight();

        /*int prev_horizontal_color;
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
                    edges.setPixel(x, y, WHITE);
                    prev_horizontal_color = horizontal_color;
                    prev_vertical_color = vertical_color;
                }else{
                    edges.setPixel(x, y, BLACK);
                }
            }
        }*/
        // code that detects edges
        for (int y = 1; y < height - 1; y++){
            for (int x = 1; x < width - 1; x++){
                int gx = sobelSum(x, y, false);
                int gy = sobelSum(x, y, true);
                int g = (int)Math.sqrt(gx * gx + gy * gy);
                edges.setPixel(x, y, g);
            }
        }

        return edges;
    }

    private int sobelSum(int x, int y, boolean orientation){
        final boolean HORIZONTAL = false;
        final boolean VERTICAL = true;
        int g = 0;
        int[][] sobel_operator = null;
        if (orientation == HORIZONTAL){
            sobel_operator = new int[][]{{-1, 0, 1},
                                         {-2, 0, -2},
                                         {-1, 0, 1}};
        }else if (orientation == VERTICAL){
            sobel_operator = new int[][]{{-1, -2, -1},
                                         {0, 0, 0},
                                         {1, 2, 1}};
        }
        for (int i = y - 1; i < y + 1; i++){
            int i1 = 0;
            for (int j = x - 1; j < x + 1; j++){
                int j1 = 0;
                g += gray_bitmap.getPixel(j, i) * sobel_operator[i1][j1];
                j1++;
            }
            i1++;
        }
        return g;
    }

}
