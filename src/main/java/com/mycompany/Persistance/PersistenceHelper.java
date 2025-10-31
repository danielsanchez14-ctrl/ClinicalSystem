/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Persistance;

import java.util.List;

/**
 *
 * @author kosmo
 * @param <T> Objeto (Puede ser médico, paciente, consulta o cita)
 */
public abstract class PersistenceHelper<T> {

    private final String fileName;

    public PersistenceHelper(String fileName) {
        this.fileName = fileName;
    }
    
    protected abstract boolean save(List<T> data);

    /**
     *
     * @return
     */
    protected abstract List<T> load();

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

}
