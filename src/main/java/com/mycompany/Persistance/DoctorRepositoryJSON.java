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
 * Repositorio persistente para objetos {@link Doctor} utilizando almacenamiento JSON.
 *
 * <p>
 * Esta clase extiende {@link JsonRepository} y proporciona métodos para agregar, actualizar,
 * eliminar (soft delete), buscar por id, buscar por especialidad, listar todos los doctores activos,
 * y búsqueda de usuarios por username para autenticación.
 * </p>
 *
 * <p>
 * Manejo de excepciones:
 * <ul>
 *     <li>{@link IllegalArgumentException} si algún parámetro de entrada es nulo o inválido.</li>
 *     <li>{@link RuntimeException} o subclases si ocurre un error de persistencia en lectura/escritura de archivos.</li>
 * </ul>
 * </p>
 *
 * @author camil
 */
public class DoctorRepositoryJSON extends JsonRepository<Doctor>
        implements IDoctorRepository, IAuthenticableRepository {

    private static final String FILE_NAME = "doctors.json";
    private static final Type LIST_TYPE = new TypeToken<List<Doctor>>() {
    }.getType();

    /**
     * Constructor que inicializa el repositorio cargando los doctores desde el archivo JSON.
     */
    public DoctorRepositoryJSON() {
        super(FILE_NAME, LIST_TYPE);
    }

    /**
     * Agrega un doctor al repositorio si no existe otro con el mismo username o
     * número de documento.
     *
     * @param doctor doctor a agregar (no puede ser nulo)
     * @return {@code true} si se agregó correctamente, {@code false} si ya existe uno igual o ocurre un error
     * @throws IllegalArgumentException si {@code doctor} es nulo
     */
    @Override
    public boolean add(Doctor doctor) {
        if (doctor == null){
            throw new IllegalArgumentException("El doctor no puede ser nulo");
        }
        try{
            boolean exists = data.stream().anyMatch(d ->
                    d.getUsername().equals(doctor.getUsername()) ||
                            d.getDocumentNumber().equals(doctor.getDocumentNumber())
            );
            if (exists) {
                System.err.println("El doctor con el mismo username o número de documento ya existe.");
                return false;
            }
            data.add(doctor);
            save();
            return true;
        } catch (RuntimeException e){
            System.err.println("Error al agregar el doctor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Realiza un "soft delete" marcando el doctor como inactivo.
     *
     * @param id ID del doctor a eliminar (no puede ser nulo o vacío)
     * @return {@code true} si se encontró y marcó como inactivo, {@code false} si no existe o ocurre un error
     * @throws IllegalArgumentException si {@code id} es nulo o vacío
     */
    @Override
    public boolean deleteById(String id) {
        if (id == null || id.isEmpty()){
            throw new IllegalArgumentException("El id no puede ser nulo o vacío");
        }
        try{
            Optional<Doctor> doctorOpt = searchById(id);
            if (doctorOpt.isPresent()){
                doctorOpt.get().setCurrentStatus(false);
                save();
                return true;
            }
            
            System.err.println("No se encontró el doctor con id: " + id);
            return false;
        } catch (RuntimeException e){
            System.err.println("Error al eliminar el doctor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza los datos de un doctor existente (por id).
     *
     * @param doctor instancia con los nuevos datos (no puede ser nula)
     * @return {@code true} si se actualizó correctamente, {@code false} si no se encontró o ocurre un error
     * @throws IllegalArgumentException si {@code doctor} es nulo
     */
    @Override
    public boolean update(Doctor doctor) {
        if (doctor == null){
            throw new IllegalArgumentException("El doctor no puede ser nulo");
        }
        try{
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getId().equals(doctor.getId())) {
                    data.set(i, doctor);
                    save();
                    return true;
                }
            }

            System.err.println("No se encontró el doctor a actualizar con id: " + doctor.getId());
            return false;
        } catch (RuntimeException e){
            System.err.println("Error al actualizar el doctor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un doctor por su id.
     *
     * @param id identificador del doctor (no puede ser nulo o vacío)
     * @return {@link Optional} con el doctor si se encontró, vacío en caso contrario
     * @throws IllegalArgumentException si {@code id} es nulo o vacío
     */
    @Override
    public Optional<Doctor> searchById(String id) {
        if (id == null || id.isEmpty()){
            throw new IllegalArgumentException("El id no puede ser nulo o vacío");
        }
        try{
            return data.stream()
                    .filter(d -> d.getId().equals(id))
                    .findFirst();
        } catch (RuntimeException e){
            System.err.println("Error al buscar el doctor por id: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Devuelve la lista de doctores que pertenecen a una especialidad determinada.
     *
     * @param specialty especialidad a filtrar (no puede ser nula)
     * @return lista de doctores con la especialidad indicada, vacía si no se encuentran
     * @throws IllegalArgumentException si {@code specialty} es nulo
     */
    @Override
    public List<Doctor> searchBySpecialty(Specialty specialty) {
        if (specialty == null){
            throw new IllegalArgumentException("La especialidad no puede ser nula");
        }
        try{
            return data.stream()
                    .filter(d -> specialty.equals(d.getMedicalSpecialty()))
                    .toList();
        } catch (RuntimeException e){
            System.err.println("Error al buscar doctores por especialidad: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Lista todos los doctores activos (no eliminados lógicamente).
     *
     * @return lista de doctores con {@code currentStatus == true}, vacía si ocurre un error
     */
    @Override
    public List<Doctor> listAll() {
        try{
            return data.stream()
                    .filter(Doctor::isCurrentStatus)
                    .toList();
        } catch (RuntimeException e){
            System.err.println("Error al listar los doctores: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Busca un usuario por su nombre de usuario (username) para autenticación.
     *
     * @param username nombre de usuario (no puede ser nulo o vacío)
     * @return {@link Optional} con el usuario si se encontró, vacío en caso contrario
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
}
