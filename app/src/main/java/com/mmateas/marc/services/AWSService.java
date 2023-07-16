package com.mmateas.marc.services;

import android.graphics.Bitmap;

import androidx.annotation.WorkerThread;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.mmateas.marc.utils.Configuration;

import org.threeten.bp.Instant;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AWSService {
    private BasicAWSCredentials basicAWSCredentials;
    private AmazonS3Client amazonS3Client;

    public AWSService() {
        basicAWSCredentials = new BasicAWSCredentials(Configuration.getInstance().getAccessId(), Configuration.getInstance().getSecretKey());
        amazonS3Client = new AmazonS3Client(basicAWSCredentials);
    }

    private File convertBitmapToFile(Bitmap bitmap, File cacheDirectory) throws IOException {
        File file = File.createTempFile("prefix", ".extension", cacheDirectory);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        os.close();

        return file;
    }

    private String generateKeyForImage() {
        String key = null;

        Random random = new Random();
        String salt = random.ints(97, 123).limit(8).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        String instant = String.valueOf(Instant.now().toEpochMilli());

        key = salt + ":" + instant;

        return key;
    }

    public void uploadImageToS3(Bitmap bitmap, File cacheDirectory) {
        Thread thread = new Thread() {
            public void run() {
                File file = null;

                try {
                    file = convertBitmapToFile(bitmap, cacheDirectory);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                String key = generateKeyForImage();

                PutObjectResult putObjectResult = null;
                PutObjectRequest putObjectRequest = new PutObjectRequest(Configuration.getInstance().getBucketName(), key, file);
                putObjectResult = amazonS3Client.putObject(putObjectRequest);
                System.out.println(putObjectResult);
            }
        };

        thread.start();
    }
}
