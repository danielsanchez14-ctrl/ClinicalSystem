/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Models;

import java.util.UUID;

/**
 * Representa a un usuario dentro del sistema de gestión de citas y consultas
 * médicas.
 *
 * @author kosmo
 */
public class User {

    private final String id; // Identificador único del usuario.
    private String fullName;
    private String documentNumber;
    private String username;
    private String phoneNumber;
    private String password;
    private boolean currentStatus; // true = cuenta activa, false = eliminada lógicamente

    public User() {
        this.id = UUID.randomUUID().toString();
        this.currentStatus = true;
    }

    public User(String username, String phoneNumber, String password, String fullName, String documentNumber) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.fullName = fullName;
        this.documentNumber = documentNumber;
        this.currentStatus = true;
    }

    public boolean isCurrentStatus() {
        return currentStatus;
    }

    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * @param currentStatus the currentStatus to set
     */
    public void setCurrentStatus(boolean currentStatus) {
        this.currentStatus = currentStatus;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the documentNumber
     */
    public String getDocumentNumber() {
        return documentNumber;
    }

    /**
     * @param documentNumber the documentNumber to set
     */
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

}
