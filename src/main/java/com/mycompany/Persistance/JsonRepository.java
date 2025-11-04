package com.mycompany.Persistance;

import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio genérico en memoria con persistencia JSON.
 *
 * <p>
 * Esta clase proporciona funcionalidades básicas para almacenar, cargar y persistir
 * colecciones de objetos de tipo {@code T} en archivos JSON. Las clases concretas
 * deben extenderlo y exponer operaciones específicas.
 * </p>
 *
 * <p>
 * Manejo de excepciones:
 * <ul>
 *     <li>{@link IllegalArgumentException} para parámetros nulos o inválidos.</li>
 *     <li>{@link RuntimeException} o sus subclases para errores de lectura/escritura en archivos JSON.</li>
 *     <li>{@link UncheckedIOException} si ocurre un error de entrada/salida durante persistencia.</li>
 *     <li>{@link IllegalStateException} si no se puede acceder al archivo.</li>
 * </ul>
 * </p>
 *
 * @param <T> Tipo de entidad almacenada.
 * @author camil
 */
public abstract class JsonRepository<T> {
    protected final JsonStore store;
    protected final String filePath;
    protected final Type listType;
    protected  List<T> data;
    private static final String BASE_PATH = "data";

    /**
     * Constructor que inicializa el repositorio cargando los datos desde el archivo JSON.
     *
     * @param fileName nombre del archivo JSON donde se persistirán los datos
     * @param listType tipo de lista para deserialización
     * @throws IllegalArgumentException si {@code fileName} es nulo o vacío
     * @throws NullPointerException     si {@code listType} es nulo
     * @throws RuntimeException         si ocurre un error al cargar los datos
     */
    protected JsonRepository(String fileName, Type listType) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("El nombre del archivo no puede ser nulo o vacío.");
        }
        Objects.requireNonNull(listType, "El tipo de lista no puede ser nulo.");

        this.filePath = Paths.get(BASE_PATH, fileName).toString();
        this.store = new JsonStore();
        this.listType = listType;
        try{
            this.data = store.readFromFile(filePath, listType, new ArrayList<>());
        } catch (RuntimeException e){
            System.err.println("Error al cargar desde " + filePath + ": " + e.getMessage());
            this.data = new ArrayList<>();
        }
    }

    /**
     * Persiste el contenido actual en memoria al archivo JSON.
     *
     * @throws UncheckedIOException      si ocurre un error de escritura
     * @throws IllegalStateException     si no se puede acceder al archivo
     * @throws RuntimeException         si ocurre otro error inesperado
     */
    protected void save() {
        try {
            store.writeToFile(filePath, data);
        } catch (IllegalStateException | UncheckedIOException e){
            System.err.println("Error al guardar en " + filePath + ": " + e.getMessage());
            throw e;
        } catch (RuntimeException e){
            System.err.println("Error inesperado al guardar en " + filePath + ": " + e.getMessage());
            throw e;
        }
    }

    /**
     * Recarga los datos desde el archivo JSON.
     * <p>
     * Si el archivo no existe o está vacío, se inicializa con una nueva {@link ArrayList}.
     * </p>
     *
     * <p>
     * En caso de error de lectura, se imprime un mensaje en la consola y se mantiene
     * la colección actual en memoria.
     * </p>
     */
    public void reload(){
        try{
            this.data = store.readFromFile(filePath, listType, new ArrayList<>());
        } catch (RuntimeException e){
            System.err.println("Error al recargar desde " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Devuelve una copia de la lista completa de datos.
     *
     * @return nueva {@link ArrayList} con todos los elementos almacenados
     */
    public List<T> getAll() {
        return new ArrayList<>(data);
    }

    /**
     * Borra todos los registros en memoria y los persiste en el archivo JSON.
     *
     * <p>
     * Si ocurre un error al guardar, se imprime un mensaje en la consola.
     * </p>
     */
    public void clear() {
        data.clear();
        try{
            save();
        } catch (RuntimeException e){
            System.err.println("Error al limpiar " + filePath + ": " + e.getMessage());
        }
    }
}
