/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Persistance;

import com.mycompany.Interfaces.IConsultationRepository;
import com.mycompany.Models.Consultation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author kosmo
 */
public class ConsultationRepositoryMemory implements IConsultationRepository {

    private final List<Consultation> repo;

    public ConsultationRepositoryMemory() {
        this.repo = new ArrayList<>();
    }

    public ConsultationRepositoryMemory(List<Consultation> repo) {
        this.repo = repo;
    }

    @Override
    public boolean add(Consultation consultation) {
        if (repo != null) {
            this.repo.add(consultation);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Consultation> searchByPatient(String patientID) {
        return this.repo.stream().
                filter(c -> c.getAppointment().getPatient().getId().equals(patientID))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> searchByDoctor(String doctorID) {
        return this.repo.stream().
                filter(c -> c.getAppointment().getDoctor().getId().equals(doctorID))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> listAll() {
        return this.repo;
    }

}
