package com.mycompany.Services;

import java.util.List;
import java.util.Optional;

import com.mycompany.Interfaces.IPatientRepository;
import com.mycompany.Models.Patient;

/**
 * Servicio de negocio para operaciones relacionadas con pacientes.
 * <p>
 * Encapsula las validaciones básicas y delega la persistencia al
 * repositorio inyectado {@link IPatientRepository}.
 * </p>
 */
public class PatientService {
    private final IPatientRepository repository;

    /**
     * Crea una instancia del servicio con el repositorio especificado.
     *
     * @param patientRepository repositorio usado para persistencia; no se valida nulo aquí.
     */
    public PatientService(IPatientRepository patientRepository) {
        this.repository = patientRepository;
    }

    /**
     * Valida el nombre del paciente.
     * <p>
     * Reglas:
     * - No puede ser null.
     * - No puede ser vacío ni contener solo espacios.
     * - Solo se permiten letras (incluyendo acentos), espacios, guiones y apóstrofes.
     * </p>
     *
     * @param name nombre a validar
     * @return true si el nombre cumple las reglas; false en caso contrario
     */
    private boolean isValidName(String name) {
        if (name == null) {
            return false;
        }
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            return false;
        }
        // Permite letras latinas (con acentos), espacios, guiones y apóstrofes
        return trimmed.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñüÜ'\\- ]+$");
    }

    /**
     * Valida el identificador del paciente.
     * <p>
     * Reglas:
     * - No nulo, no vacío, no solo espacios.
     * - No contiene espacios internos.
     * - Solo letras, números y guiones permitidos.
     * </p>
     *
     * @param id id a validar
     * @return true si el id es válido; false en caso contrario
     */
    private boolean isValidId(String id) {
        if (id == null) return false;
        String trimmed = id.trim();
        if (trimmed.isEmpty()) return false;
        // No espacios internos, solo letras, números y guiones
        return trimmed.matches("^[A-Za-z0-9\\-]+$");
    }

    /**
     * Valida el username del paciente.
     * <p>
     * Reglas:
     * - No nulo, no vacío, no solo espacios.
     * - No contiene espacios internos.
     * - Longitud máxima: 50 caracteres.
     * - Solo letras, números, guiones y guion bajo.
     * </p>
     *
     * @param username username a validar
     * @return true si el username es válido; false en caso contrario
     */
    private boolean isValidUsername(String username) {
        if (username == null) return false;
        String trimmed = username.trim();
        if (trimmed.isEmpty() || trimmed.length() > 50) return false;
        // No espacios internos, solo letras, números, guiones y guion bajo
        return trimmed.matches("^[A-Za-z0-9\\-_]+$");
    }

    /**
     * Verifica si el username ya existe en la lista de pacientes.
     */
    private boolean isUsernameTaken(String username) {
        return repository.listAll().stream()
            .anyMatch(p -> p.getUsername().equalsIgnoreCase(username));
    }

    /**
     * Agrega un nuevo paciente.
     * <p>
     * No permite añadir un paciente nulo ni uno cuyo id o username ya exista en el repositorio.
     * Además valida el nombre, el id y el username del paciente.
     * </p>
     *
     * @param patient paciente a agregar
     * @return true si el paciente se añadió correctamente; false en caso de entrada inválida o si ya existe
     */
    public boolean addPatient(Patient patient) {
        if (patient == null) {
            return false;
        }
        if (!isValidId(patient.getId())) {
            return false;
        }
        if (!isValidName(patient.getUsername())) {
            return false;
        }
        if (!isValidUsername(patient.getUsername())) {
            return false;
        }
        if (repository.searchById(patient.getId()).isPresent()) {
            return false;
        }
        if (isUsernameTaken(patient.getUsername())) {
            return false;
        }
        return repository.add(patient);
    }

    /**
     * Elimina un paciente por su identificador.
     *
     * @param id identificador del paciente
     * @return true si se eliminó el paciente; false si el id es inválido o no existe
     */
    public boolean deletePatient(String id) {
        if (!isValidId(id)) {
            return false;
        }
        if (repository.searchById(id).isPresent()){
            return repository.deleteById(id);
        }
        return false;
    }

    /**
     * Actualiza los datos de un paciente existente.
     * <p>
     * Solo se permite actualizar si el paciente no es nulo y su id ya existe en el repositorio.
     * Además valida el nombre, el id y el username del paciente.
     * </p>
     *
     * @param patient paciente con los datos actualizados
     * @return true si la actualización fue exitosa; false en caso contrario
     */
    public boolean updatePatient(Patient patient) {
        if (patient == null) {
            return false;
        }
        if (!isValidId(patient.getId())) {
            return false;
        }
        if (!repository.searchById(patient.getId()).isPresent()) {
            return false;
        }
        if (!isValidName(patient.getUsername())) {
            return false;
        }
        if (!isValidUsername(patient.getUsername())) {
            return false;
        }
        // Si el username cambió, verificar que no esté tomado por otro paciente
        Optional<Patient> existing = repository.searchById(patient.getId());
        if (existing.isPresent() && !existing.get().getUsername().equalsIgnoreCase(patient.getUsername())) {
            if (isUsernameTaken(patient.getUsername())) {
                return false;
            }
        }
        return repository.update(patient);
    }

    /**
     * Busca un paciente por su identificador.
     *
     * @param id identificador del paciente
     * @return Optional con el paciente si se encuentra; Optional.empty() si el id es inválido o no existe
     */
    public Optional<Patient> searchById(String id) {
        if (!isValidId(id)) {
            return Optional.empty();
        }
        return repository.searchById(id);
    }

    /**
     * Lista todos los pacientes almacenados.
     *
     * @return lista de pacientes (puede ser vacía pero no nula)
     */
    public List<Patient> listAllPatients() {
        return repository.listAll();
    }
}
