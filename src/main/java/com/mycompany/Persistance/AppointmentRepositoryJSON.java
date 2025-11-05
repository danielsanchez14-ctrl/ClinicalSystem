package com.mycompany.Persistance;

import com.google.gson.reflect.TypeToken;
import java.util.Optional;

import com.mycompany.Interfaces.IAppointmentRepository;
import com.mycompany.Models.Appointment;
import com.mycompany.Models.AppointmentStatus;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repositorio persistente para citas ({@link Appointment}) utilizando almacenamiento JSON.
 *
 * <p>
 * Esta clase extiende {@link JsonRepository} y proporciona métodos para agregar,
 * actualizar el estado, buscar por ID, paciente o doctor, y listar todas las citas.
 * Maneja la persistencia en el archivo {@code appointments.json}.
 * </p>
 *
 * <p>
 * Manejo de excepciones:
 * <ul>
 *     <li>{@link IllegalArgumentException} si algún parámetro de entrada es nulo o inválido.</li>
 *     <li>{@link RuntimeException} o subclases si ocurre un error de lectura/escritura de archivos.</li>
 * </ul>
 * </p>
 * 
 * @author camil
 */
public class AppointmentRepositoryJSON extends JsonRepository<Appointment> implements IAppointmentRepository{
    private static final String FILE_NAME = "appointments.json";
    private static final Type LIST_TYPE = new TypeToken<List<Appointment>>() {}.getType();

    /**
     * Constructor que inicializa el repositorio cargando las citas desde el archivo JSON.
     */
    public AppointmentRepositoryJSON() {
        super(FILE_NAME, LIST_TYPE);
    }

    /**
     * Agrega una cita al repositorio y persiste los cambios en JSON.
     *
     * @param appointment cita a agregar (no puede ser nula)
     * @return {@code true} si se agregó correctamente, {@code false} si hubo error
     * @throws IllegalArgumentException si {@code appointment} es nulo
     * @throws RuntimeException si ocurre un error de persistencia
     */
    @Override
    public boolean add(Appointment appointment) {
        if (appointment == null){
            throw new IllegalArgumentException("La cita (appointment) no puede ser nula");
        }

        try {
            data.add(appointment);
            save();
            return true;
        } catch (RuntimeException e) {
            System.err.println("Error al agregar la cita: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza el estado de una cita por su ID y persiste los cambios.
     *
     * @param appointmentID ID de la cita a actualizar (no nulo ni vacío)
     * @param newStatus     nuevo estado de la cita (no nulo)
     * @return {@code true} si se actualizó correctamente, {@code false} si no se encontró
     * @throws IllegalArgumentException si {@code appointmentID} o {@code newStatus} son nulos o inválidos
     */
    @Override
    public boolean updateState(String appointmentID, AppointmentStatus newStatus){
        if (appointmentID == null || appointmentID.isBlank()){
            throw new IllegalArgumentException("El ID de la cita no puede ser nulo o vacío");
        }
        if (newStatus == null){
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo");
        }
        try {
            Optional<Appointment> appt = data.stream()
                    .filter(app -> app.getId().equals(appointmentID))
                    .findFirst();
            if (appt.isPresent()) {
                appt.get().setStatus(newStatus);
                save();
                return true;
            } else {
                System.err.println("Cita no encontrada con ID: " + appointmentID);
                return false;
            }
        } catch (RuntimeException e) {
            System.err.println("Error al actualizar el estado de la cita: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca una cita por su ID.
     *
     * @param id ID de la cita (no nulo ni vacío)
     * @return {@link Optional} con la cita si se encontró, vacío en caso contrario
     * @throws IllegalArgumentException si {@code id} es nulo o vacío
     */
    @Override
    public Optional<Appointment> searchById(String id) {
        if (id == null || id.isBlank()){
            throw new IllegalArgumentException("El ID no puede ser nulo o vacío");
        }
        try{
            return data.stream()
                    .filter(app -> app.getId().equals(id))
                    .findFirst();
        } catch (RuntimeException e){
            System.err.println("Error al buscar la cita por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Busca todas las citas asociadas a un paciente específico.
     *
     * @param patientId ID del paciente (no nulo ni vacío)
     * @return lista de citas asociadas al paciente; vacía si no se encuentran
     * @throws IllegalArgumentException si {@code patientId} es nulo o vacío
     */
    @Override
    public List<Appointment> searchByPatient(String patientId) {
        if (patientId == null || patientId.isBlank()){
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo o vacío");
        }
        try{
            return data.stream()
                    .filter(app -> app.getPatient().getId().equals(patientId))
                    .collect(Collectors.toList());
        } catch (RuntimeException e){
            System.err.println("Error al buscar citas por paciente: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Busca todas las citas asociadas a un doctor específico.
     *
     * @param doctorId ID del doctor (no nulo ni vacío)
     * @return lista de citas asociadas al doctor; vacía si no se encuentran
     * @throws IllegalArgumentException si {@code doctorId} es nulo o vacío
     */
    @Override
    public List<Appointment> searchByDoctor(String doctorId) {
        if (doctorId == null || doctorId.isBlank()){
            throw new IllegalArgumentException("El ID del doctor no puede ser nulo o vacío");
        }
        try{
            return data.stream()
                    .filter(app -> app.getDoctor().getId().equals(doctorId))
                    .collect(Collectors.toList());
        } catch (RuntimeException e){
            System.err.println("Error al buscar citas por doctor: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Devuelve todas las citas almacenadas.
     *
     * @return nueva lista con todas las citas
     */
    @Override
    public List<Appointment> listAll() {
        return new ArrayList<>(data);
    }
}
