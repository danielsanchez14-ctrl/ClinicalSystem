package com.mycompany.Services;

import com.mycompany.Interfaces.IAuthenticableRepository;
import java.util.List;
import java.util.Optional;

import com.mycompany.Interfaces.IPatientRepository;
import com.mycompany.Models.Patient;
import java.util.Objects;

/**
 * Servicio de negocio para operaciones relacionadas con pacientes.
 * <p>
 * Encapsula las validaciones básicas y delega la persistencia al repositorio
 * inyectado {@link IPatientRepository}.
 * </p>
 */
public class PatientService {
    
    private final IPatientRepository repository;
    private final GlobalUsernameValidator globalValidator;

    /**
     * Crea una instancia del servicio con el repositorio especificado.
     *
     * @param patientRepository repositorio usado para persistencia; no se
     * valida nulo aquí.
     * @param globalValidator encargado de garantizar la unicidad de los nombres
     * de usuario.
     */
    public PatientService(IPatientRepository patientRepository,
            GlobalUsernameValidator globalValidator) {
        this.repository = patientRepository;
        this.globalValidator = globalValidator;
    }

    /**
     * Agrega un nuevo paciente.
     * <p>
     * No permite añadir un paciente nulo ni uno cuyo id o username ya exista en
     * el repositorio. Además valida el nombre, el id y el username del
     * paciente.
     * </p>
     *
     * @param patient paciente a agregar
     * @return true si el paciente se añadió correctamente; false en caso
     * contrario
     */
    public boolean addPatient(Patient patient) {
        if (!validatePatient(patient, false)) {
            return false;
        }
        return repository.add(patient);
    }

    /**
     * Valida un paciente antes de crear o actualizar.
     *
     * @param patient El paciente a validar
     * @param isUpdate true si es una actualización, false si es creación
     * @return true si el paciente es válido, false en caso contrario
     */
    private boolean validatePatient(Patient patient, boolean isUpdate) {
        if (patient == null) {
            return false;
        }
        if (patient.getId() == null || patient.getId().trim().isEmpty()) {
            return false;
        }
        if (patient.getFullName() == null || patient.getFullName().trim().isEmpty()) {
            return false;
        }
        
        if (patient.getUsername() == null || patient.getUsername().trim().isEmpty()
                || patient.getUsername().equals("admin")) {
            return false;
        }
        
        if (patient.getPassword() == null || patient.getPassword().trim().isEmpty()) {
            return false;
        }
        
        if (patient.getPhoneNumber() == null
                || !patient.getPhoneNumber().matches("^(?:\\+57|57)?3\\d{9}$")) {
            return false;
        }
        
        if (patient.getDocumentNumber() == null
                || !patient.getDocumentNumber().matches("^\\d+$")) {
            return false;
        }

        // Para register: id no debe existir ya; para update: debe existir previamente
        boolean exists = repository.searchById(patient.getId()).isPresent();
        if (!isUpdate && exists) {
            return false;
        }
        if (isUpdate && !exists) {
            return false;
        }

        // Validación de username duplicado (case-sensitive).
        String username = patient.getUsername().trim();
        if (repository instanceof IAuthenticableRepository) {
            IAuthenticableRepository authRepo = (IAuthenticableRepository) repository;
            var found = authRepo.searchByUsername(username);
            if (found.isPresent()) {
                // Si el username existe y pertenece a otro paciente -> inválido
                if (!Objects.equals(found.get().getId(), patient.getId())) {
                    return false;
                }
            }
        }

        //Validación global del userName duplicado
        if (globalValidator != null && globalValidator.usernameExists(username,
                patient.getId())) {
            return false;
        }
        return true;
    }

    /**
     * Elimina un paciente por su ID.
     *
     * @param id El ID del paciente a eliminar.
     * @return {@code true} si el paciente fue eliminado exitosamente;
     * {@code false} en caso contrario.
     */
    public boolean removePatient(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return repository.deleteById(id);
    }

    /**
     * Actualiza la información de un paciente.
     *
     * @param patient El paciente con la información actualizada.
     * @return {@code true} si el paciente fue actualizado exitosamente;
     * {@code false} en caso contrario.
     */
    public boolean updatePatient(Patient patient) {
        if (!validatePatient(patient, true)) {
            return false;
        }
        return repository.update(patient);
    }

    /**
     * Busca un paciente por su ID.
     *
     * @param id El ID del paciente a buscar.
     * @return Un {@code Optional} que contiene el paciente si se encuentra; de
     * lo contrario, un {@code Optional.empty()}.
     */
    public Optional<Patient> searchById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        return repository.searchById(id);
    }

    /**
     * Lista todos los pacientes.
     *
     * @return Una lista de todos los pacientes.
     */
    public List<Patient> listAllPatients() {
        return repository.listAll();
    }
    
}
