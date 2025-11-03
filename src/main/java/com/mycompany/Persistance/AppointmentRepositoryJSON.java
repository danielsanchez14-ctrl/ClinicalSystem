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
 * Repositorio persistente para las citas (Appointment) utilizando almacenamiento JSON.
 * Extiende de JsonRepository para reutilizar la lectura y escritura de archivos.
 *
 * @author camil
 */
public class AppointmentRepositoryJSON extends JsonRepository<Appointment> implements IAppointmentRepository{
    private static final String FILE_NAME = "appointments.json";
    private static final Type LIST_TYPE = new TypeToken<List<Appointment>>() {}.getType();

    public AppointmentRepositoryJSON() {
        super(FILE_NAME, LIST_TYPE);
    }

    @Override
    public boolean add(Appointment appointment) {
        data.add(appointment);
        save();
        return true;
    }

    @Override
    public boolean updateState(String appointmentID, AppointmentStatus newStatus){
        Optional<Appointment> appt = data.stream()
                .filter(app -> app.getId().equals(appointmentID))
                .findFirst();
        if (appt.isPresent()) {
            appt.get().setStatus(newStatus);
            save();
            return true;
        }
        return false;
    }

    @Override
    public Optional<Appointment> searchById(String id) {
        return data.stream()
                .filter(app -> app.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Appointment> searchByPatient(String patientId) {
        return data.stream()
                .filter(app -> app.getPatient().getId().equals(patientId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> searchByDoctor(String doctorId) {
        return data.stream()
                .filter(app -> app.getDoctor().getId().equals(doctorId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> listAll() {
        return new ArrayList<>(data);
    }
}
