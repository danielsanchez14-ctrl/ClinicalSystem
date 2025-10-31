/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Persistance;

import com.mycompany.Interfaces.IAuthenticableRepository;
import com.mycompany.Interfaces.IDoctorRepository;
import com.mycompany.Models.Doctor;
import com.mycompany.Models.Specialty;
import com.mycompany.Models.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación en memoria del repositorio de doctores.
 *
 * @author camil
 */
public class DoctorRepositoryMemory implements IAuthenticableRepository, IDoctorRepository {

    private final List<Doctor> doctors = new ArrayList<>();

    @Override
    public Optional<User> searchByUsername(String username) {
        return doctors.stream()
                .filter(Doctor::isCurrentStatus)
                .filter(d -> d.getUsername().equals(username.trim()))
                .map(d -> (User) d)
                .findFirst();
    }

    @Override
    public boolean add(Doctor doctor) {
        return doctors.add(doctor);
    }

    @Override
    public boolean deleteById(String id) {
        return doctors.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .map(doctor -> {
                    doctor.setCurrentStatus(false);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public boolean update(Doctor doctor) {
        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getId().equals(doctor.getId())) {
                doctors.set(i, doctor);
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<Doctor> searchById(String id) {
        return doctors.stream()
                .filter(Doctor::isCurrentStatus)
                .filter(d -> d.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Doctor> searchBySpecialty(Specialty specialty) {
        return doctors.stream()
                .filter(Doctor::isCurrentStatus)
                .filter(d -> specialty.equals(d.getMedicalSpecialty()))
                .toList();
    }

    @Override
    public List<Doctor> listAll() {
        return doctors.stream()
                .filter(Doctor::isCurrentStatus)
                .toList();
    }

    @Override
    public List<Doctor> listAllIncludingInactiveDoctors() {
        return this.doctors;
    }
}
