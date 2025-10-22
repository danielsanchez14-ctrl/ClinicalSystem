/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Models;

/**
 *
 * @author kosmo
 */
public class Doctor extends User {

    private Specialty medicalSpecialty;

    public Doctor() {
        super();
    }

    public Doctor(String username, String phoneNumber, String password, String fullName,
            String documentNumber, Specialty medicalSpecialty) {
        super(username, phoneNumber, password, fullName, documentNumber);
        this.medicalSpecialty = medicalSpecialty;
    }

    /**
     * @return the medicalSpecialty
     */
    public Specialty getMedicalSpecialty() {
        return medicalSpecialty;
    }

    /**
     * @param medicalSpecialty the medicalSpecialty to set
     */
    public void setMedicalSpecialty(Specialty medicalSpecialty) {
        this.medicalSpecialty = medicalSpecialty;
    }

}
