/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Services;

import com.mycompany.Interfaces.IAuthenticableRepository;
import com.mycompany.Interfaces.IDoctorRepository;
import com.mycompany.Models.Doctor;
import com.mycompany.Models.Specialty;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones relacionadas con doctores.
 * @author camil
 */
public class DoctorService {

    private final IDoctorRepository repository;

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
        if (!validateDoctor(doctor, false)) return false;
        return repository.add(doctor);
    }

    /**
     * Elimina un doctor por su ID.
     * @param id El ID del doctor a eliminar.
     * @return {@code true} si el doctor fue eliminado exitosamente; {@code false} en caso contrario.
     */
    public boolean removeDoctor(String id) {
        if (id == null || id.trim().isEmpty()) {
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
        if (!validateDoctor(doctor, true)) return false;
        return repository.update(doctor);
    }

    /**
     * Valida un doctor antes de crear o actualizar.
     * @param doctor El doctor a validar
     * @param isUpdate true si es una actualización, false si es creación
     * @return true si el doctor es válido, false en caso contrario
     */
    private boolean validateDoctor(Doctor doctor, boolean isUpdate) {
        if (doctor == null) return false;
        if (doctor.getId() == null || doctor.getId().trim().isEmpty()) {
            return false;
        }
        if (doctor.getFullName() == null || doctor.getFullName().trim().isEmpty()) return false;
        if (doctor.getMedicalSpecialty() == null) return false;
        if (doctor.getUsername() == null || doctor.getUsername().trim().isEmpty()) return false;

        // Para register: id no debe existir ya; para update: debe existir previamente
        boolean exists = repository.searchById(doctor.getId()).isPresent();
        if (!isUpdate && exists) return false;
        if (isUpdate && !exists) return false;

        // Validación de username duplicado (case-sensitive).
        String username = doctor.getUsername().trim();
        if (repository instanceof IAuthenticableRepository) {
            IAuthenticableRepository authRepo = (IAuthenticableRepository) repository;
            var found = authRepo.searchByUsername(username);
            if (found.isPresent()) {
                // Si el username existe y pertenece a otro doctor -> inválido
                if (!Objects.equals(found.get().getId(), doctor.getId())) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Busca un doctor por su ID.
     * @param id El ID del doctor a buscar.
     * @return Un {@code Optional} que contiene el doctor si se encuentra; de lo contrario, un {@code Optional.empty()}.
     */
    public Optional<Doctor> searchById(String id) {
        if (id == null || id.trim().isEmpty()) {
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
        if (id == null || id.trim().isEmpty() || specialty == null) {
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
        if (specialty == null) {
            return List.of();
        }
        return repository.searchBySpecialty(specialty);
    }
}
