package com.mycompany.Persistance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Clase utilitaria para manejo de persistencia en formato JSON.
 * Permite leer y escribir cualquier tipo de objeto usando Gson.
 * 
 * @author camil
 */
public class JsonStore {
    private final Gson gson;
    
    public JsonStore() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    /**
     * Lee un archivo JSON y lo convierte a un objeto del tipo especificado.
     * Si el archivo no existe, lo crea con el valor por defecto.
     *
     * @param path Ruta del archivo JSON
     * @param typeOfT Tipo genérico de dato (usando TypeToken)
     * @param defaultValue Valor por defecto si el archivo no existe o está vacío
     * @return Objeto del tipo especificado
     */
    public <T> T readFromFile(String path, java.lang.reflect.Type typeOfT, T defaultValue) {
        try {
            Path filePath = Paths.get(path);

            if (!Files.exists(filePath)) {
                if (filePath.getParent() != null)
                    Files.createDirectories(filePath.getParent());
                writeToFile(path, defaultValue);
                return defaultValue;
            }

            try (Reader reader = new InputStreamReader(
                    new FileInputStream(filePath.toFile()), StandardCharsets.UTF_8)) {

                T obj = gson.fromJson(reader, typeOfT);
                return obj != null ? obj : defaultValue;
            }

        } catch (IOException e) {
            throw new RuntimeException("Error leyendo JSON: " + path, e);
        }
    }

    /**
     * Escribe un objeto en un archivo JSON.
     *
     * @param path Ruta del archivo JSON
     * @param obj Objeto a guardar
     */
    public void writeToFile(String path, Object obj) {
        try {
            Path filePath = Paths.get(path);
            if (filePath.getParent() != null)
                Files.createDirectories(filePath.getParent());

            try (Writer writer = new OutputStreamWriter(
                    new FileOutputStream(filePath.toFile()), StandardCharsets.UTF_8)) {

                gson.toJson(obj, writer);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo JSON: " + path, e);
        }
    }

    /**
     * Verifica si un archivo existe en el sistema.
     *
     * @param path Ruta del archivo
     * @return true si existe, false en caso contrario
     */
    public boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }
}
