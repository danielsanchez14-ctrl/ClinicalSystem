package com.mycompany.Services;

import java.util.List;
import java.util.Optional;

import com.mycompany.Interfaces.IPatientRepository;
import com.mycompany.Models.Patient;

public class PatientService {
    private IPatientRepository repository;

    public PatientService(IPatientRepository patientRepository) {
        this.repository = patientRepository;
    }

    public boolean addPatient(Patient patient) {
        if(patient==null|| repository.searchById(patient.getId()).isPresent()){
            return  false;
        }
        return repository.add(patient);
    }

    public boolean deletePatient(String id) {
        if (id==null || id.isEmpty()) {
            return false;
        }
        if (repository.searchById(id).isPresent()){
            return repository.deleteById(id);
        }
        return false;
    }

    public boolean updatePatient(Patient patient) {
        if (patient == null || !repository.searchById(patient.getId()).isPresent()) {   
            return false;
        }   
        return repository.update(patient);
    }

    public Optional<Patient> searchById(String id) {
        if (id == null || id.isEmpty()) {   
            return Optional.empty();
        }
        return repository.searchById(id);
    }

    public List<Patient> listAllPatients() {
        return repository.listAll();
    }
}
