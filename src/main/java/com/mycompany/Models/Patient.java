/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author kosmo
 */
public class Patient extends User {

    private LocalDate birthDate;

    public Patient() {
        super();
    }

    public Patient(String username, String phoneNumber, String password,
            String fullName, String documentNumber, LocalDate birthDate) {
        super(username, phoneNumber, password, fullName, documentNumber);
        this.birthDate = birthDate;
    }

    public String getBirthDateAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return birthDate.format(formatter);
    }

    /**
     * @return the birthDate
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * @param birthDate the birthDate to set
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

}
