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

    public boolean add(Patient patient) {
        return repository.add(patient);
    }

    public boolean deletePatient(String id) {
        return repository.deleteById(id);
    }

    public boolean updatePatient(Patient patient) {
        return repository.update(patient);
    }

    public Optional<Patient> searchById(String id) {
        return repository.searchById(id);
    }

    public List<Patient> listAllPatients() {
        return repository.listAll();
    }
}
