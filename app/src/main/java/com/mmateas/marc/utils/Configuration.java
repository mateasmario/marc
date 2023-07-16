package com.mmateas.marc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static Configuration configuration;

    /* Amazon Web Services Credentials */
    private String accessId;
    private String secretKey;
    private String bucketName;

    private Configuration() {}

    public static synchronized Configuration getInstance() {
        if (configuration == null) {
            configuration = new Configuration();
        }
        return configuration;
    }

    public void loadConfigurationFromInputStream(InputStream propertiesInputStream) throws IOException {
        Properties props = new Properties();
        props.load(propertiesInputStream);

        accessId = props.getProperty("ACCESS_ID");
        secretKey = props.getProperty("SECRET_KEY");
        bucketName = props.getProperty("BUCKET_NAME");

        propertiesInputStream.close();
    }

    public String getAccessId() {
        return accessId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }
}
