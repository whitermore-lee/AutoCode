package org.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesUtiles {
    private static Properties properties = new Properties();
    private static Map<String, String> propertiesMap = new ConcurrentHashMap<>();

    static {
        InputStream inputStream = null;
        try {
            inputStream = PropertiesUtiles.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(new InputStreamReader(inputStream,"UTF-8"));
            ;
            Iterator<Object> iterator = properties.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next().toString();
                propertiesMap.put(key, properties.getProperty(key));
            }
        }catch (Exception e) {
        }finally {
        if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getProperty(String key) {
        return propertiesMap.get(key);
    }

    public static void main(String[] args) {
        System.out.println(getProperty("db.driver.name"));
    }
}