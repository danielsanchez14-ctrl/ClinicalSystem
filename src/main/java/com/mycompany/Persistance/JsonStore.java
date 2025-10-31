package com.mycompany.Persistance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
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
                .setPrettyPrinting() // para que los JSON sean legibles
                .create();
    }

    /**
     * Guarda una lista u objeto genérico en un archivo JSON.
     */
    public <T> void writeToFile(String filePath, T data) {
        try {
            ensureDirectoryExists(filePath);

            try (Writer writer = new FileWriter(filePath)) {
                gson.toJson(data, writer);
            }
        } catch (IOException e) {
            System.err.println("❌ Error al escribir el archivo JSON: " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Lee un archivo JSON y lo convierte a la estructura indicada.
     * Si el archivo no existe o está vacío, devuelve defaultValue.
     */
    public <T> T readFromFile(String filePath, Type type, T defaultValue) {
        if (!Files.exists(Paths.get(filePath))) {
            return defaultValue;
        }

        try (Reader reader = new FileReader(filePath)) {
            // Evita error si el archivo está vacío
            if (new File(filePath).length() == 0) {
                return defaultValue;
            }

            T result = gson.fromJson(reader, type);
            return (result != null) ? result : defaultValue;

        } catch (FileNotFoundException e) {
            return defaultValue;

        } catch (JsonSyntaxException e) {
            System.err.println("⚠️ Error de sintaxis JSON en: " + filePath);
            e.printStackTrace();
            return defaultValue;

        } catch (IOException e) {
            System.err.println("❌ Error al leer el archivo JSON: " + filePath);
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * Asegura que exista la carpeta donde se guardará el JSON.
     */
    private void ensureDirectoryExists(String filePath) throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            Files.createDirectories(parentDir.toPath());
        }
    }
}