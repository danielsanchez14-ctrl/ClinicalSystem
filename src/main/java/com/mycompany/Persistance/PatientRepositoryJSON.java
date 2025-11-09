/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Persistance;

import com.google.gson.reflect.TypeToken;
import com.mycompany.Interfaces.IAuthenticableRepository;
import com.mycompany.Interfaces.IPatientRepository;
import com.mycompany.Models.Doctor;
import com.mycompany.Models.Patient;
import com.mycompany.Models.User;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio persistente para objetos {@link Patient} utilizando almacenamiento JSON.
 *
 * <p>
 * Extiende {@link JsonRepository} para aprovechar la carga, persistencia y manejo
 * de archivos JSON. Implementa búsqueda por username para autenticación y
 * operaciones CRUD básicas para pacientes.
 * </p>
 *
 * <p><b>Manejo de excepciones:</b></p>
 * <ul>
 *     <li>{@link IllegalArgumentException} si se recibe un parámetro nulo o inválido.</li>
 *     <li>{@link RuntimeException} si ocurre un error inesperado al leer o escribir el archivo JSON.</li>
 * </ul>
 *
 * @author Jess
 */
public class PatientRepositoryJSON extends JsonRepository<Patient> implements IAuthenticableRepository, IPatientRepository {
    
    private static final String FILE_NAME = "patients.json";
    private static final Type LIST_TYPE = new TypeToken<List<Patient>>() {}.getType();

    /**
     * Crea un repositorio de pacientes cargando automáticamente los datos desde JSON.
     */
    public PatientRepositoryJSON() {
        super(FILE_NAME, LIST_TYPE);
    }  

    /**
     * Busca un paciente por su username (para procesos de autenticación).
     *
     * @param username usuario a buscar (no puede ser nulo o vacío)
     * @return Optional con el usuario si existe, vacío si no
     * @throws IllegalArgumentException si {@code username} es nulo o vacío
     */
    @Override
    public Optional<User> searchByUsername(String username) {
        if (username == null || username.isEmpty()){
            throw new IllegalArgumentException("El username no puede ser nulo o vacío");
        }
        try{
            return data.stream()
                    .filter(d -> d.getUsername().equals(username))
                    .map(d -> (User) d)
                    .findFirst();
        } catch (RuntimeException e){
            System.err.println("Error al buscar el usuario por username: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Agrega un nuevo paciente al repositorio.
     *
     * @param patient paciente a agregar (no puede ser nulo)
     * @return true si se agregó correctamente; false si ocurre un error
     * @throws IllegalArgumentException si {@code patient} es nulo
     */
    @Override
    public boolean add(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("El paciente no puede ser nulo");
        }
        try {
            data.add(patient);
            save();
            return true;
        } catch (RuntimeException e) {
            System.err.println("Error al agregar paciente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Realiza un borrado lógico del paciente (soft delete).
     *
     * @param id identificador único del paciente (no puede ser nulo o vacío)
     * @return true si se marcó como inactivo; false si no existe o hay error
     * @throws IllegalArgumentException si {@code id} es nulo o vacío
     */
    @Override
    public boolean deleteById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id no puede ser nulo o vacío");
        }
        try {
            return data.stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .map(patient -> {
                        patient.setCurrentStatus(false);
                        save();
                        return true;
                    })
                    .orElse(false);
        } catch (RuntimeException e) {
            System.err.println("Error al eliminar paciente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza los datos de un paciente existente.
     *
     * @param patient instancia con los datos actualizados (no puede ser nulo)
     * @return true si se actualizó; false si no existe el paciente
     * @throws IllegalArgumentException si {@code patient} es nulo
     */
    @Override
    public boolean update(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("El paciente no puede ser nulo");
        }
        try {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getId().equals(patient.getId())) {
                    data.set(i, patient);
                    save();
                    return true;
                }
            }
            return false;
        } catch (RuntimeException e) {
            System.err.println("Error al actualizar paciente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un paciente por su id.
     *
     * @param id identificador del paciente (no puede ser nulo o vacío)
     * @return Optional con el paciente si existe, vacío si no
     * @throws IllegalArgumentException si {@code id} es nulo o vacío
     */
    @Override
    public Optional<Patient> searchById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id no puede ser nulo o vacío");
        }
        try {
            return data.stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst();
        } catch (RuntimeException e) {
            System.err.println("Error al buscar paciente por id: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Lista todos los pacientes activos.
     *
     * @return lista de pacientes activos; lista vacía si ocurre un error
     */
    @Override
    public List<Patient> listAll() {
        try {
            return data.stream()
                    .filter(Patient::isCurrentStatus)
                    .toList();
        } catch (RuntimeException e) {
            System.err.println("Error al listar pacientes: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
}
