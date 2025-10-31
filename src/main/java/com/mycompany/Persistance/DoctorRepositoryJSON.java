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

public class DoctorRepositoryJSON extends JsonRepository<Doctor> implements IDoctorRepository, IAuthenticableRepository {

    private static final String FILE_PATH = "data/doctors.json";
    private static final Type LIST_TYPE = new TypeToken<List<Doctor>>() {}.getType();

    public DoctorRepositoryJSON() {
        super(FILE_PATH, LIST_TYPE);
    }

    @Override
    public boolean add(Doctor doctor) {
        boolean added = data.add(doctor);
        if (added) save();
        return added;
    }

    @Override
    public boolean deleteById(String id) {
        Optional<Doctor> doctorOpt = searchById(id);
        if (doctorOpt.isPresent()) {
            data.remove(doctorOpt.get());
            save();
            return true;
        }
        return false;
    }

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

    @Override
    public Optional<Doctor> searchById(String id) {
        return data.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Doctor> searchBySpecialty(Specialty specialty) {
        return data.stream()
                .filter(d -> specialty.equals(d.getMedicalSpecialty()))
                .toList();
    }

    @Override
    public List<Doctor> listAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Optional<User> searchByUsername(String username) {
        return data.stream()
                .filter(d -> d.getUsername().equals(username))
                .map(d -> (User) d)
                .findFirst();
    }
}
