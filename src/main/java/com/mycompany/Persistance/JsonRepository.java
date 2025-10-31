package com.mycompany.Persistance;

import java.lang.reflect.Type;
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
    protected final List<T> data;

    protected JsonRepository(String filePath, Type listType) {
        this.filePath = filePath;
        this.store = new JsonStore();
        this.listType = listType;
        this.data = store.readFromFile(filePath, listType, new ArrayList<>());
    }

    /**
     * Persiste el contenido actual en memoria al archivo JSON.
     */
    protected void save() {
        store.writeToFile(filePath, data);
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
