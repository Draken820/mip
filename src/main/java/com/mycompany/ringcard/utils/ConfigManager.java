package com.mycompany.ringcard.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class ConfigManager {
    private static final String CONFIG_FILE = System.getProperty("user.home") + File.separator + ".ringcard_config.properties";

    public static String getRutaDocumentos() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
            return props.getProperty("ruta_documentos");
        } catch (Exception e) {
            return null; // Si no existe o hay error, retorna null
        }
    }

    public static boolean setRutaDocumentos(String ruta) {
        Properties props = new Properties();
        try {
            File file = new File(CONFIG_FILE);
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    props.load(fis);
                }
            }
            props.setProperty("ruta_documentos", ruta);
            try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
                props.store(fos, "Configuración de Ring-Card");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}