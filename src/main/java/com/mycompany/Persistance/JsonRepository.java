package com.mycompany.Persistance;

import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio genérico en memoria con persistencia JSON.
 * <p>
 * Carga los datos desde un archivo JSON al instanciarse y provee utilidades
 * básicas (save, getAll, clear). Las clases concretas deben extenderlo y
 * exponer las operaciones específicas.
 * </p>
 *
 * @param <T> tipo de entidad almacenada
 * @author camil
 */
public abstract class JsonRepository<T> {
    protected final JsonStore store;
    protected final String filePath;
    protected final Type listType;
    protected  List<T> data;
    private static final String BASE_PATH = "data";

    protected JsonRepository(String fileName, Type listType) {
        this.filePath = Paths.get(BASE_PATH, fileName).toString();
        this.store = new JsonStore();
        this.listType = listType;
        this.data = store.readFromFile(filePath, listType, new ArrayList<>());
    }

    /**
     * Persiste el contenido actual en memoria al archivo JSON.
     */
    protected void save() {
        try {
            store.writeToFile(filePath, data);
        } catch (RuntimeException e){
            System.err.println("Error al guardar en " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Recarga los datos desde el archivo JSON especificado en filePath.
     * Este método actualiza la colección de datos interna leyendo el contenido
     * del archivo JSON nuevamente y reemplazando los datos existentes.
     * Si el archivo no existe o está vacío, se inicializa con una nueva ArrayList vacía.
     */
    public void reload(){
        this.data = store.readFromFile(filePath, listType, new ArrayList<>());
    }

    /**
     * Devuelve una copia de la lista completa de datos (incluye inactivos si están presentes).
     *
     * @return lista nueva con los elementos almacenados
     */
    public List<T> getAll() {
        return new ArrayList<>(data);
    }

    /**
     * Borra todos los registros en memoria y en el archivo JSON.
     */
    public void clear() {
        data.clear();
        save();
    }
}
