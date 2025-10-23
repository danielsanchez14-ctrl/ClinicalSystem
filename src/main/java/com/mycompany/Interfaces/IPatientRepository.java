package com.mycompany.Interfaces;

import java.util.List;
import java.util.Optional;

import com.mycompany.Models.Patient;

/**
 * Interfaz de repositorio para operaciones CRUD sobre pacientes.
 * <p>
 * Implementaciones concretas pueden almacenar en memoria, base de datos, etc.
 * </p>
 */
public interface IPatientRepository {

    /**
     * Agrega un paciente.
     *
     * @param patient paciente a agregar
     * @return true si se agregó correctamente; false si el paciente es nulo o
     * ya existe
     */
    boolean add(Patient patient);

    /**
     * Elimina un paciente por su identificador.
     *
     * @param id identificador del paciente a eliminar
     * @return true si se eliminó al menos un registro; false si no existe
     */
    boolean deleteById(String id);

    /**
     * Actualiza un paciente existente.
     *
     * @param patient paciente con datos actualizados
     * @return true si la actualización fue exitosa; false si no existe el
     * paciente
     */
    boolean update(Patient patient);

    /**
     * Busca un paciente por su identificador.
     *
     * @param id identificador a buscar
     * @return Optional con el paciente si se encontró; Optional.empty() si no
     */
    Optional<Patient> searchById(String id);

    /**
     * Devuelve todos los pacientes.
     *
     * @return lista de pacientes (nunca null; implementaciones pueden devolver
     * copia)
     */
    List<Patient> listAll();
}
