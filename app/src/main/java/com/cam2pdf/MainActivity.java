package com.cam2pdf;


import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Camera2Helper camera2Helper;

    public Bitmap onImageAcquired(Bitmap imageIn) {
        Bitmap bitmap = Bitmap.createBitmap(imageIn.getWidth(), imageIn.getHeight(), Bitmap.Config.ARGB_8888); // each pixel is stored on 4 bytes (ARGB_8888)
        int w = imageIn.getWidth(), h = imageIn.getHeight();
        for(int x=0; x<w; x++)
            for(int y=0; y<h; y++)
                bitmap.setPixel(x, y, imageIn.getPixel(x, h - y - 1));   // imageIn.getPixel(x, y) = getColor of that pixelint
        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextureView textureView = findViewById(R.id.texture);
        ImageView imageView = findViewById(R.id.imageView);

        camera2Helper = new Camera2Helper(this, textureView, imageView);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(camera2Helper != null)
            camera2Helper.onResume();
    }

    @Override
    public void onPause() {
        if(camera2Helper != null)
            camera2Helper.onPause();
        super.onPause();
    }
}


