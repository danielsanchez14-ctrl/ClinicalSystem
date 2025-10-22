/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Services;

import com.mycompany.Interfaces.IConsultationRepository;
import com.mycompany.Models.Consultation;
import java.util.Optional;

/**
 *
 * @author kosmo
 */
public class ConsultationService {

    private final IConsultationRepository repo;

    public ConsultationService(IConsultationRepository repo) {
        this.repo = repo;
    }

    public boolean createConsultation(Consultation consultation) {
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
    }
}
