/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Models;

import java.util.UUID;

/**
 * Representa la especialidad de un médico.
 *
 * @author kosmo
 */
public class Specialty {

    private final String id; //Identificador único de la especialidad
    private SpecialtyName specialtyName; //Enumerado que indica su nombre
    //private String description;

    public Specialty() {
        this.id = UUID.randomUUID().toString();
    }

    public Specialty(SpecialtyName specialtyName) {
        this.id = UUID.randomUUID().toString();
        this.specialtyName = specialtyName;
        //this.description = description;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the specialtyName
     */
    public SpecialtyName getSpecialtyName() {
        return specialtyName;
    }

    /**
     * @param specialtyName the specialtyName to set
     */
    public void setSpecialtyName(SpecialtyName specialtyName) {
        this.specialtyName = specialtyName;
    }

}
