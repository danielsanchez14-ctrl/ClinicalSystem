/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 *
 * @author kosmo
 */
public class Appointment {

    private final String id;
    private LocalDateTime scheduledAt;
    private Duration duration;
    private Patient patient;
    private Doctor doctor;
    private AppointmentStatus status;

    public Appointment() {
        this.id = UUID.randomUUID().toString();
    }

    public Appointment(LocalDateTime scheduledAt, Duration duration, Patient patient, Doctor doctor) {
        this.id = UUID.randomUUID().toString();
        this.scheduledAt = scheduledAt;
        this.duration = duration;
        this.patient = patient;
        this.doctor = doctor;
    }

    public String getScheduledAtAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return scheduledAt.format(formatter);
    }

    public String getDurationAsString() {
        return duration.toString();
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the scheduledAt
     */
    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    /**
     * @param scheduledAt the scheduledAt to set
     */
    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    /**
     * @return the duration
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    /**
     * @return the patient
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * @param patient the patient to set
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     * @return the doctor
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * @param doctor the doctor to set
     */
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    /**
     * @return the status
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

}
