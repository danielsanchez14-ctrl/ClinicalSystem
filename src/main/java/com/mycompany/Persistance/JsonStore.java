package com.mycompany.Persistance;

import com.google.gson.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Utilidad para lectura/escritura de archivos JSON usando Gson.
 * <p>
 * Provee métodos genéricos para serializar y deserializar objetos y listas,
 * y gestiona la creación de directorios si es necesario.
 * </p>
 *
 * @author camil
 */
public class JsonStore {

    private final Gson gson;

    public JsonStore() {
        this.gson = new GsonBuilder()
                // --- LocalDate ---
                .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                    @Override
                    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE));
                    }
                })
                .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    @Override
                    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        return LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
                    }
                })

                // --- LocalTime ---
                .registerTypeAdapter(LocalTime.class, new JsonSerializer<LocalTime>() {
                    @Override
                    public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_TIME));
                    }
                })
                .registerTypeAdapter(LocalTime.class, new JsonDeserializer<LocalTime>() {
                    @Override
                    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        return LocalTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_TIME);
                    }
                })

                // --- LocalDateTime ---
                .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                    @Override
                    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    }
                })
                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    }
                })

                // --- Duration ---
                .registerTypeAdapter(Duration.class, new JsonSerializer<Duration>() {
                    @Override
                    public JsonElement serialize(Duration src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.toString()); // Ej: "PT30M"
                    }
                })
                .registerTypeAdapter(Duration.class, new JsonDeserializer<Duration>() {
                    @Override
                    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        return Duration.parse(json.getAsString());
                    }
                })

                .setPrettyPrinting()
                .create();
    }

    public <T> void writeToFile(String filePath, T data) {
        try {
            ensureDirectoryExists(filePath);
            try (Writer writer = new OutputStreamWriter(
                    new FileOutputStream(filePath),
                    StandardCharsets.UTF_8)) {
                gson.toJson(data, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir el archivo JSON: " + filePath, e);
        }
    }

    public <T> T readFromFile(String filePath, Type type, T defaultValue) {
        if (!Files.exists(Paths.get(filePath))) {
            return defaultValue;
        }

        File file = new File(filePath);
        if (file.length() == 0) {
            return defaultValue;
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            T result = gson.fromJson(reader, type);
            return result != null ? result : defaultValue;
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Error de sintaxis en el archivo JSON: " + filePath, e);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON: " + filePath, e);
        }
    }

    private void ensureDirectoryExists(String filePath) throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            Files.createDirectories(parentDir.toPath());
        }
    }
}