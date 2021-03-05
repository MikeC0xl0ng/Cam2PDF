package com.cam2pdf;

import android.graphics.Bitmap;



public class EdgeDetector {
    
    Bitmap img;
    
    public EdgeDetector(Bitmap img_){
        img = toGrayScale(img_);
    }
    
    private Bitmap toGrayScale(Bitmap img_){
        //BufferedImage gray_img = new BufferedImage(img_.getWidth(), img_.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Bitmap gray_img = Bitmap.createBitmap(img_.getWidth(),img_.getHeight(),Bitmap.Config.ARGB_8888);
        for (int y=0; y<img_.getHeight(); y++){
            for (int x=0; x<img_.getWidth(); x++){
                gray_img.setPixel(x, y, img_.getPixel(x, y));
            }
        }
        return gray_img;
    }
    
    public Bitmap detectEdges(){
        int [][] img_= new int [img.getHeight()][img.getWidth()];
        for(int y=0;y<img_.length;y++){
            for(int x=0;x<img_[0].length;x++){
                img_[y][x]=img.getPixel(x, y);
            }
        }
        //BufferedImage buffer=new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);
        Bitmap buffer = Bitmap.createBitmap(img.getWidth(),img.getHeight(),Bitmap.Config.ARGB_8888);
        for(int y=0;y<img_.length;y++){
            for(int x=0;x<img_[0].length;x++){
                buffer.setPixel(x,y,img_[y][x]);
            }
        }
        return img;
    }
    
   
    
}
