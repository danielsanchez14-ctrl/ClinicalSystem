/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Services;

import com.mycompany.Interfaces.IDoctorRepository;
import com.mycompany.Models.Doctor;
import com.mycompany.Models.Specialty;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author camil
 */
public class DoctorService {

    private IDoctorRepository repository;

    public DoctorService(IDoctorRepository repository) {
        this.repository = repository;
    }

    public boolean registerDoctor(Doctor doctor) {
        if (doctor == null) {
            return false;
        }
        if (repository.searchById(doctor.getId()).isPresent()) {
            return false;
        }
        return repository.add(doctor);
    }

    public boolean removeDoctor(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }

        if (repository.searchById(id).isPresent()) {
            return repository.deleteById(id);
        }
        return false;
    }

    public boolean updateDoctor(Doctor doctor) {
        if (doctor == null || !repository.searchById(doctor.getId()).isPresent()) {
            return false;
        }
        return repository.update(doctor);
    }

    public Optional<Doctor> searchById(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }
        return repository.searchById(id);
    }

    public List<Doctor> listAllDoctors() {
        return repository.listAll();
    }

    public boolean assignSpecialty(String id, Specialty specialty) {
        if (specialty == null) {
            return false;
        }

        Optional<Doctor> doctorOpt = repository.searchById(id);
        if (doctorOpt.isEmpty()) {
            return false;
        }

        Doctor doctor = doctorOpt.get();
        doctor.setMedicalSpecialty(specialty);
        return repository.update(doctor);
    }

    public List<Doctor> searchBySpecialty(Specialty specialty) {
        if (specialty == null) {
            return List.of();
        }
        return repository.searchBySpecialty(specialty);
    }
}
