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
 * Servicio para gestionar operaciones relacionadas con doctores.
 * @author camil
 */
public class DoctorService {

    private IDoctorRepository repository;

    /**
     * Constructor del servicio de doctores.
     * @param repository El repositorio de doctores a utilizar.
     */
    public DoctorService(IDoctorRepository repository) {
        this.repository = repository;
    }

    /**
     * Registra un nuevo doctor.
     * @param doctor El doctor a registrar.
     * @return {@code true} si el doctor fue registrado exitosamente; {@code false} en caso contrario.
     */
    public boolean registerDoctor(Doctor doctor) {
        if (doctor == null) {
            return false;
        }
        if (repository.searchById(doctor.getId()).isPresent()) {
            return false;
        }
        return repository.add(doctor);
    }

    /**
     * Elimina un doctor por su ID.
     * @param id El ID del doctor a eliminar.
     * @return {@code true} si el doctor fue eliminado exitosamente; {@code false} en caso contrario.
     */
    public boolean removeDoctor(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        return repository.deleteById(id);
    }

    /**
     * Actualiza la información de un doctor.
     * @param doctor El doctor con la información actualizada.
     * @return {@code true} si el doctor fue actualizado exitosamente; {@code false} en caso contrario.
     */
    public boolean updateDoctor(Doctor doctor) {
        if (doctor == null) {
            return false;
        }
        if (repository.searchById(doctor.getId()).isEmpty()) {
            return false;
        }
        return repository.update(doctor);
    }

    /**
     * Busca un doctor por su ID.
     * @param id El ID del doctor a buscar.
     * @return Un {@code Optional} que contiene el doctor si se encuentra; de lo contrario, un {@code Optional.empty()}.
     */
    public Optional<Doctor> searchById(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }
        return repository.searchById(id);
    }

    /**
     * Lista todos los doctores.
     * @return Una lista de todos los doctores.
     */
    public List<Doctor> listAllDoctors() {
        return repository.listAll();
    }

    /**
     * Asigna una especialidad médica a un doctor.
     * @param id El ID del doctor.
     * @param specialty La especialidad médica a asignar.
     * @return {@code true} si la especialidad fue asignada exitosamente; {@code false} en caso contrario.
     */
    public boolean assignSpecialty(String id, Specialty specialty) {
        if (id == null || id.isEmpty() || specialty == null) {
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

    /**
     * Busca doctores por su especialidad médica.
     * @param specialty La especialidad médica a buscar.
     * @return Una lista de doctores que tienen la especialidad especificada.
     */
    public List<Doctor> searchBySpecialty(Specialty specialty) {
        return repository.searchBySpecialty(specialty);
    }
}
