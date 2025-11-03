package com.mycompany.Persistance;

import com.google.gson.reflect.TypeToken;
import com.mycompany.Interfaces.IAuthenticableRepository;
import com.mycompany.Interfaces.IDoctorRepository;
import com.mycompany.Models.Doctor;
import com.mycompany.Models.Specialty;
import com.mycompany.Models.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio de persistencia para objetos Doctor utilizando un archivo JSON.
 * <p>
 * Implementa operaciones básicas de CRUD, búsqueda por especialidad y soporte
 * para autenticación (búsqueda por username). Utiliza JsonRepository como base
 * para la lectura/escritura del archivo.
 * </p>
 *
 * @author camil
 */
public class DoctorRepositoryJSON extends JsonRepository<Doctor>
        implements IDoctorRepository, IAuthenticableRepository {

    private static final String FILE_NAME = "doctors.json";
    private static final Type LIST_TYPE = new TypeToken<List<Doctor>>() {
    }.getType();

    public DoctorRepositoryJSON() {
        super(FILE_NAME, LIST_TYPE);
    }

    /**
     * Agrega un doctor al repositorio si no existe otro con el mismo username o
     * número de documento.
     *
     * @param doctor doctor a agregar
     * @return true si se agregó correctamente, false si ya existe uno igual
     */
    @Override
    public boolean add(Doctor doctor) {
        boolean added = data.add(doctor);
        if (added)
            save();
        return added;
    }

    /**
     * Realiza un "soft delete" marcando el doctor como inactivo.
     *
     * @param id id del doctor a eliminar
     * @return true si se encontró y marcó como inactivo, false si no existe
     */
    @Override
    public boolean deleteById(String id) {
        return data.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .map(doctor -> {
                    doctor.setCurrentStatus(false);
                    save();
                    return true;
                })
                .orElse(false);
    }

    /**
     * Actualiza los datos de un doctor existente (por id).
     *
     * @param doctor instancia con los nuevos datos
     * @return true si se actualizó correctamente, false si no se encontró
     */
    @Override
    public boolean update(Doctor doctor) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(doctor.getId())) {
                data.set(i, doctor);
                save();
                return true;
            }
        }
        return false;
    }

    /**
     * Busca un doctor por su id.
     *
     * @param id identificador del doctor
     * @return Optional con el doctor si se encontró, vacío en caso contrario
     */
    @Override
    public Optional<Doctor> searchById(String id) {
        return data.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst();
    }

    /**
     * Devuelve la lista de doctores que pertenecen a una especialidad determinada.
     *
     * @param specialty especialidad a filtrar
     * @return lista de doctores con la especialidad indicada
     */
    @Override
    public List<Doctor> searchBySpecialty(Specialty specialty) {
        return data.stream()
                .filter(d -> specialty.equals(d.getMedicalSpecialty()))
                .toList();
    }

    /**
     * Lista todos los doctores activos (no eliminados lógicamente).
     *
     * @return lista de doctores con currentStatus == true
     */
    @Override
    public List<Doctor> listAll() {
        return data.stream()
                .filter(Doctor::isCurrentStatus)
                .toList();
    }

    /**
     * Busca un usuario por su nombre de usuario (username) para autenticación.
     *
     * @param username nombre de usuario
     * @return Optional con el usuario si se encontró, vacío en caso contrario
     */
    @Override
    public Optional<User> searchByUsername(String username) {
        return data.stream()
                .filter(d -> d.getUsername().equals(username))
                .map(d -> (User) d)
                .findFirst();
    }
}
