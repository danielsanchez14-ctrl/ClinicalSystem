package com.mycompany.Persistance;

import com.google.gson.reflect.TypeToken;
import com.mycompany.Interfaces.IConsultationRepository;
import com.mycompany.Models.Consultation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositorio persistente para consultas ({@link Consultation}) utilizando almacenamiento JSON.
 *
 * <p>
 * Esta clase extiende {@link JsonRepository} y proporciona métodos para agregar consultas,
 * buscar por paciente, doctor o cita asociada, y listar todas las consultas.
 * Persiste los datos en el archivo {@code consultations.json}.
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
public class ConsultationRepositoryJSON extends JsonRepository<Consultation> implements IConsultationRepository {

    private static final String FILE_NAME = "consultations.json";
    private static final Type LIST_TYPE = new TypeToken<List<Consultation>>() {}.getType();

    /**
     * Constructor que inicializa el repositorio cargando las consultas desde el archivo JSON.
     */
    public ConsultationRepositoryJSON() {
        super(FILE_NAME, LIST_TYPE);
    }

    /**
     * Agrega una consulta al repositorio y persiste los cambios en JSON.
     *
     * @param consultation consulta a agregar (no puede ser nula)
     * @return {@code true} si se agregó correctamente, {@code false} si ocurrió un error
     * @throws IllegalArgumentException si {@code consultation} es nula
     * @throws RuntimeException si ocurre un error de persistencia
     */
    @Override
    public boolean add(Consultation consultation) {
        if (consultation == null) {
            throw new IllegalArgumentException("La consulta no puede ser nula");
        }
        try{
            data.add(consultation);
            save();
            return true;
        } catch (RuntimeException e){
            System.err.println("Error al agregar la consulta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca todas las consultas asociadas a un paciente específico.
     *
     * @param patientID ID del paciente (no nulo ni vacío)
     * @return lista de consultas asociadas al paciente; vacía si no se encuentran
     * @throws IllegalArgumentException si {@code patientID} es nulo o vacío
     */
    @Override
    public List<Consultation> searchByPatient(String patientID) {
        if (patientID == null || patientID.isEmpty()) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo o vacío");
        }
        try{
            return data.stream().
                    filter(c -> c.getAppointment().getPatient().getId().equals(patientID))
                    .collect(Collectors.toList());
        } catch (RuntimeException e){
            System.err.println("Error al buscar consultas por paciente: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Busca todas las consultas asociadas a un doctor específico.
     *
     * @param doctorID ID del doctor (no nulo ni vacío)
     * @return lista de consultas asociadas al doctor; vacía si no se encuentran
     * @throws IllegalArgumentException si {@code doctorID} es nulo o vacío
     */
    @Override
    public List<Consultation> searchByDoctor(String doctorID) {
        if (doctorID == null || doctorID.isEmpty()) {
            throw new IllegalArgumentException("El ID del doctor no puede ser nulo o vacío");
        }
        try{
            return data.stream().
                    filter(c -> c.getAppointment().getDoctor().getId().equals(doctorID))
                    .collect(Collectors.toList());
        } catch (RuntimeException e){
            System.err.println("Error al buscar consultas por doctor: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Devuelve todas las consultas almacenadas.
     *
     * @return nueva lista con todas las consultas
     */
    @Override
    public List<Consultation> listAll() {
        try {
            return new ArrayList<>(data);
        } catch (RuntimeException e) {
            System.err.println("Error al listar todas las consultas: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Busca una consulta asociada a una cita específica.
     *
     * @param appointmentID ID de la cita (no nulo ni vacío)
     * @return {@link Optional} con la consulta si se encontró, vacío en caso contrario
     * @throws IllegalArgumentException si {@code appointmentID} es nulo o vacío
     */
    @Override
    public Optional<Consultation> searchByAppointment(String appointmentID) {
        if (appointmentID == null || appointmentID.isEmpty()) {
            throw new IllegalArgumentException("El ID de la cita no puede ser nulo o vacío");
        }
        try{
            return data.stream().
                    filter(c -> c.getAppointment() != null).
                    filter(c -> c.getAppointment().getId().equals(appointmentID)).findFirst();
        } catch (RuntimeException e){
            System.err.println("Error al buscar consulta por cita: " + e.getMessage());
            return Optional.empty();
        }
    }
}
