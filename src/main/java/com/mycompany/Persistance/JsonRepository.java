package com.mycompany.Persistance;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    protected void save() {
        store.writeToFile(filePath, data);
    }

    public List<T> getAll() {
        return new ArrayList<>(data);
    }

    public void clear() {
        data.clear();
        save();
    }
}
