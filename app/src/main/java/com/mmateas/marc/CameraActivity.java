package com.mmateas.marc;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.mmateas.marc.services.AWSService;
import com.mmateas.marc.utils.Configuration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        ImageView imgView = findViewById(R.id.imageView);
        Bundle extras = getIntent().getExtras();

        Bitmap bitmap = null;

        if (extras != null) {
            bitmap = (Bitmap)extras.get("image");
            if (bitmap != null) {
                imgView.setImageBitmap(bitmap);
            }
        }

        AWSService awsService = new AWSService();
        awsService.uploadImageToS3(bitmap, getCacheDir());
    }
}