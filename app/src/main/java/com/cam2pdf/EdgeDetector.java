package com.cam2pdf;

import android.graphics.Bitmap;



public class EdgeDetector {

    Bitmap bitmap;

    public EdgeDetector(Bitmap bitmap_){
        bitmap = toGrayScale(bitmap_);
    }

    private Bitmap toGrayScale(Bitmap bitmap_){
        //BufferedImage gray_bitmap = new BufferedImage(bitmap_.getWidth(), bitmap_.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Bitmap grayscale_bitmap = Bitmap.createBitmap(bitmap_.getWidth(),bitmap_.getHeight(),Bitmap.Config.ALPHA_8);  // each pixel is stored as a single translucency (alpha) channel, no color information is stored. (ALPHA_8)
        for (int y=0; y < bitmap_.getHeight(); y++)
            for (int x=0; x < bitmap_.getWidth(); x++)
                grayscale_bitmap.setPixel(x, y, bitmap_.getPixel(x, y));
        return grayscale_bitmap;
    }

    // never used
    /*
    public Bitmap toGrayScale(){
        //BufferedImage gray_bitmap = new BufferedImage(bitmap_.getWidth(), bitmap_.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Bitmap grayscale_bitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.ALPHA_8);
        for (int y=0; y<bitmap.getHeight(); y++){
            for (int x=0; x<bitmap.getWidth(); x++){
                grayscale_bitmap.setPixel(x, y, bitmap.getPixel(x, y));
            }
        }
        return grayscale_bitmap;
    }
    */
    // never used

    public Bitmap detectEdges(){

        // code that detects edges

        return bitmap;
    }



}
