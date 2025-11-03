package com.mycompany.Persistance;

import com.google.gson.reflect.TypeToken;
import com.mycompany.Interfaces.IConsultationRepository;
import com.mycompany.Models.Consultation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConsultationRepositoryJSON extends JsonRepository<Consultation> implements IConsultationRepository {
    private static final String FILE_NAME = "consultations.json";
    private static final Type LIST_TYPE = new TypeToken<List<Consultation>>() {
    }.getType();

    public ConsultationRepositoryJSON() {
        super(FILE_NAME, LIST_TYPE);
    }

    @Override
    public boolean add(Consultation consultation) {
        data.add(consultation);
        save();
        return true;
    }

    @Override
    public List<Consultation> searchByPatient(String patientID) {
        return data.stream().
                filter(c -> c.getAppointment().getPatient().getId().equals(patientID))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> searchByDoctor(String doctorID) {
        return data.stream().
                filter(c -> c.getAppointment().getDoctor().getId().equals(doctorID))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consultation> listAll() {
        return data;
    }

    @Override
    public Optional<Consultation> searchByAppointment(String appointmentID) {
        return data.stream().
                filter(c -> c.getAppointment() != null).
                filter(c -> c.getAppointment().getId().equals(appointmentID)).findFirst();
    }
}
