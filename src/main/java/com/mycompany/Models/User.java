/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Models;
import java.util.UUID;

/**
 *  Representa a un usuario dentro del sistema de gestión de citas y consultas
 * médicas.
 * @author kosmo
 */
public class User {
    
    private final String id; //Identificador único del usuario.
    private String username;
    private String phoneNumber;
    private String password;
    private boolean currentStatus;
    
    
    public User(){
        this.id = UUID.randomUUID().toString();
    }

    public User(String username, String phoneNumber, String password) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.currentStatus = false;
    }

   //Métodos de encapsulamiento
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public boolean isCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(boolean currentStatus) {
        this.currentStatus = currentStatus;
    }

}
