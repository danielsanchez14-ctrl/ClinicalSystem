package com.mycompany.Persistance;

import com.google.gson.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Utilidad para lectura y escritura de archivos JSON usando Gson.
 *
 * <p>
 * Provee métodos genéricos para serializar y deserializar objetos y listas.
 * Maneja tipos de fecha y hora de Java (LocalDate, LocalTime, LocalDateTime, Duration)
 * y asegura la creación de directorios necesarios.
 * <p>
 * Todos los métodos lanzan excepciones estándar (UncheckedIOException, IllegalArgumentException, JsonParseException)
 * en caso de fallos de lectura, escritura o sintaxis de JSON.
 * </p>
 * 
 * @author camil
 */
public class JsonStore {

    private final Gson gson;

    /**
     * Crea una instancia de {@code JsonStore} con soporte para serialización/deserialización
     * de {@link LocalDate}, {@link LocalTime}, {@link LocalDateTime} y {@link Duration},
     * además de formateo bonito de JSON.
     */
    public JsonStore() {
        this.gson = new GsonBuilder()
                // Adaptadores para LocalDate, LocalTime, LocalDateTime y Duration
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

    /**
     * Serializa un objeto o lista y lo escribe en el archivo especificado.
     *
     * @param filePath ruta completa del archivo JSON
     * @param data     objeto o lista a serializar
     * @param <T>      tipo del objeto
     * @throws IllegalArgumentException si {@code filePath} es nulo o vacío
     * @throws UncheckedIOException      si ocurre un error de escritura en disco
     * @throws IllegalStateException     si no se puede acceder al archivo
     */
    public <T> void writeToFile(String filePath, T data) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo no puede ser nula o vacía.");
        }
        try {
            ensureDirectoryExists(filePath);
            try (Writer writer = new OutputStreamWriter(
                    new FileOutputStream(filePath),
                    StandardCharsets.UTF_8)) {
                gson.toJson(data, writer);
            }
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Archivo no encontrado: " + filePath, e);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al escribir en el archivo JSON: " + filePath, e);
        }
    }

    /**
     * Lee un objeto o lista desde un archivo JSON.
     *
     * @param filePath     ruta completa del archivo JSON
     * @param type         tipo de objeto esperado
     * @param defaultValue valor por defecto si el archivo no existe o está vacío
     * @param <T>          tipo del objeto
     * @return objeto leído desde JSON o {@code defaultValue} si el archivo no existe o está vacío
     * @throws IllegalArgumentException si {@code filePath} es nulo o vacío
     * @throws JsonParseException       si el JSON tiene sintaxis incorrecta
     * @throws UncheckedIOException     si ocurre un error de lectura
     */
    public <T> T readFromFile(String filePath, Type type, T defaultValue) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo no puede ser nula o vacía.");
        }
        Path path = Paths.get(filePath);

        if (!Files.exists(path) || path.toFile().length() == 0) {
            return defaultValue;
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8)) {
            T result = gson.fromJson(reader, type);
            return result != null ? result : defaultValue;
        } catch (JsonSyntaxException e) {
            throw new JsonParseException("Error de sintaxis en el archivo JSON: " + filePath, e);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al leer el archivo JSON: " + filePath, e);
        }
    }

    /**
     * Asegura que el directorio padre del archivo exista, creándolo si es necesario.
     *
     * @param filePath ruta del archivo
     * @throws IOException si ocurre un error al crear directorios
     */
    private void ensureDirectoryExists(String filePath) throws IOException {
        Path parentDir = Paths.get(filePath).getParent();

        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }
    }
}