package com.t.module_t.database.config;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final String CONFIG_FILE_PATH = "config.properties";

    public static String getFCMAuthorizationKey(Context context) throws IOException {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("config.properties");
        // Загрузка содержимого файла в объект Properties
        properties.load(inputStream);
        return properties.getProperty("fcm.authorization.key");

    }
}
