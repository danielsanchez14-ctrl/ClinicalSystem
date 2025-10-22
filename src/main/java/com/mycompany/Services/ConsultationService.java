/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Services;

import com.mycompany.Interfaces.IConsultationRepository;
import com.mycompany.Models.Consultation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Esta clase se encarga de gestionar las consultas.
 *
 * @author kosmo
 */
public class ConsultationService {

    /**
     * La clase tiene como variable de instancia el repositorio de consultas.
     */
    private final IConsultationRepository repo;

    /**
     * Método constructor de la clase
     *
     * @param repo el repositorio de consultas.
     */
    public ConsultationService(IConsultationRepository repo) {
        this.repo = repo;
    }

    /**
     * @param consultation es la consulta a guardar.
     * @return un boolean que indica si la consulta se añadió o no.
     */
    public boolean createConsultation(Consultation consultation) {
        try {
            if (consultation == null) {
                return false;
            } else if (consultation.getAppointment() == null) {
                return false;
            } else if (consultation.getDiagnosis() == null || consultation.getDiagnosis().isBlank()) {
                return false;
            } else if (consultation.getTreatment() == null || consultation.getTreatment().isBlank()) {
                return false;
            }

            var appointmentID = consultation.getAppointment().getId();
            Optional<Consultation> duplicatedConsultation = repo.searchByAppointment(appointmentID);
            if (duplicatedConsultation.isPresent()) {
                return false;
            }

            this.repo.add(consultation);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<Consultation> getConsultHistoryForPatient(String patientID) {
        try {
            if (patientID == null || patientID.isBlank()) {
                return List.of(); //Retorna lista vacía.
            }

            List<Consultation> patientHistory = this.repo.searchByPatient(patientID);

            if (patientHistory.isEmpty()) {
                return List.of();
            }

            Collections.reverse(patientHistory);

            return patientHistory;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }
}
