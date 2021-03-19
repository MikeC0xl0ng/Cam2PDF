package com.cam2pdf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE_PERMISSIONS = 0;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};

    private TextureView textureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        textureView = (TextureView) findViewById(R.id.preview);

        if (permissionsGranted()){
            startCamera();
        }else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void startCamera(){
        CameraX.unbindAll();

        Rational aspect_ratio = new Rational(textureView.getWidth(), textureView.getHeight());
        Size screen_size = new Size(textureView.getWidth(), textureView.getHeight());

        PreviewConfig preview_config = new PreviewConfig.Builder()
                .setTargetAspectRatio(aspect_ratio)
                .setTargetResolution(screen_size)
                .build();
        Preview preview = new Preview(preview_config);
        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                ViewGroup parent = (ViewGroup) textureView.getParent();
                parent.removeView(textureView);
                parent.addView(textureView);
                textureView.setSurfaceTexture(output.getSurfaceTexture());

                updateTransform();
            }
        });

        ImageCaptureConfig image_capture_config = new ImageCaptureConfig.Builder()
                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                .build();
        ImageCapture img_capture = new ImageCapture(image_capture_config);
        findViewById(R.id.takePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File f = new File("sdcard/Camera_X" + System.currentTimeMillis());
                img_capture.takePicture(f, new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {

                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {

                    }
                });
            }
        });
        CameraX.bindToLifecycle(this, preview, img_capture);
    }

    private void updateTransform(){
        Matrix m = new Matrix();

        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();

        float c_x = w/2f;
        float c_y = h/2f;

        int rotation_dgr;
        int rotation = (int)textureView.getRotation();
        switch(rotation){
            case Surface.ROTATION_0:
                rotation_dgr = 0;
                break;
            case Surface.ROTATION_90:
                rotation_dgr = 90;
                break;
            case Surface.ROTATION_180:
                rotation_dgr = 180;
                break;
            case Surface.ROTATION_270:
                rotation_dgr = 270;
                break;
            default:
                return;
        }
        m.postRotate((float)rotation_dgr, c_x, c_y);
        textureView.setTransform(m);
    }

    private boolean permissionsGranted(){
        for (String permission : REQUIRED_PERMISSIONS){
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }

}