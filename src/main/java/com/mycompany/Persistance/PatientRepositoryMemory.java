package com.mycompany.Persistance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mycompany.Interfaces.IAuthenticableRepository;
import com.mycompany.Interfaces.IPatientRepository;
import com.mycompany.Models.Patient;
import com.mycompany.Models.User;

/**
 * Implementación en memoria del repositorio de pacientes.
 * <p>
 * Esta implementación utiliza una lista interna para almacenar instancias de {@link Patient}.
 */
public class PatientRepositoryMemory implements IAuthenticableRepository, IPatientRepository {
    List<Patient> patients = new ArrayList<>();

    /**
     * Agrega un paciente a la lista en memoria.
     *
     * @param patient paciente a agregar
     * @return true si se añadió; false si el paciente es nulo o ya existe según su id
     */
    @Override
    public boolean add(Patient patient) {
        if (patient==null||searchById(patient.getId()).isPresent()){
            return false;
        }
        return patients.add(patient);
    }

    /**
     * Elimina pacientes cuyo id coincida con el proporcionado.
     *
     * @param Id identificador del paciente a eliminar
     * @return true si se eliminó al menos un elemento; false si no se encontró
     */
    @Override
    public boolean deleteById(String Id) {
        return patients.removeIf(p -> p.getId().equals(Id));
    }

    /**
     * Actualiza un paciente existente reemplazando el elemento en la lista.
     *
     * @param patient paciente con los datos actualizados
     * @return true si se encontró y actualizó; false si no existe el id
     */
    @Override
    public boolean update(Patient patient) {
        for(int i = 0; i < patients.size();i++){
            if (patients.get(i).getId().equals(patient.getId())){
                patients.set(i, patient);
                return true;
            }
        }
        return false;
    }

    /**
     * Busca un paciente por id usando stream.
     *
     * @param Id identificador a buscar
     * @return Optional con el paciente si se encuentra; Optional.empty() si no
     */
    @Override
    public Optional<Patient> searchById(String Id) {
        return patients.stream().filter(p -> p.getId().equals(Id)).findFirst();
    }

    /**
     * Devuelve una copia de la lista de pacientes para evitar exposición del almacenamiento interno.
     *
     * @return nueva lista con los pacientes actuales
     */
    @Override
    public List<Patient> listAll() {
        return new ArrayList<>(patients);
    }

    /**
     * Busca un usuario (autenticable) por nombre de usuario.
     * <p>
     * Esta implementación filtra sobre la lista de pacientes, compara el username
     * ignorando mayúsculas/minúsculas y devuelve el primer elemento convertido a User.
     * </p>
     *
     * @param username nombre de usuario a buscar
     * @return Optional con el usuario si se encuentra; Optional.empty() si no
     */
    @Override
    public Optional<User> searchByUsername(String username) {
        return patients.stream()
                .filter(p -> p.getUsername().equalsIgnoreCase(username))
                .map(p -> (User) p)
                .findFirst();
    }


}
