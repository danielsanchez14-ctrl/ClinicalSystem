/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 *
 * @author kosmo
 */
public class Consultation {

    private final String id;
    private Appointment appointment;
    private String diagnosis;
    private String treatment;
    private final LocalDateTime registrationDate;

    public Consultation() {
        this.id = UUID.randomUUID().toString();
        this.registrationDate = LocalDateTime.now();
    }

    public Consultation(Appointment appointment, String diagnosis, String treatment) {
        this.id = UUID.randomUUID().toString();
        this.appointment = appointment;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.registrationDate = LocalDateTime.now();
    }

    public String getRegistrationDateAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return registrationDate.format(formatter);
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the appointment
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * @param appointment the appointment to set
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * @return the diagnosis
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * @param diagnosis the diagnosis to set
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    /**
     * @return the treatment
     */
    public String getTreatment() {
        return treatment;
    }

    /**
     * @param treatment the treatment to set
     */
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    /**
     * @return the registrationDate
     */
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

}
