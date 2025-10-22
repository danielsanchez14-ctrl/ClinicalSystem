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
    private IPatientRepository repository;

    /**
     * Crea una instancia del servicio con el repositorio especificado.
     *
     * @param patientRepository repositorio usado para persistencia; no se valida nulo aquí.
     */
    public PatientService(IPatientRepository patientRepository) {
        this.repository = patientRepository;
    }

    /**
     * Agrega un nuevo paciente.
     * <p>
     * No permite añadir un paciente nulo ni uno cuyo id ya exista en el repositorio.
     * </p>
     *
     * @param patient paciente a agregar
     * @return true si el paciente se añadió correctamente; false en caso de entrada inválida o si ya existe
     */
    public boolean addPatient(Patient patient) {
        if(patient==null|| repository.searchById(patient.getId()).isPresent()){
            return  false;
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
        if (id==null || id.isEmpty()) {
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
     * </p>
     *
     * @param patient paciente con los datos actualizados
     * @return true si la actualización fue exitosa; false en caso contrario
     */
    public boolean updatePatient(Patient patient) {
        if (patient == null || !repository.searchById(patient.getId()).isPresent()) {   
            return false;
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
        if (id == null || id.isEmpty()) {   
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
