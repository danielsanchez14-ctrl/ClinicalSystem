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

public class DoctorRepositoryJSON implements IAuthenticableRepository, IDoctorRepository{
    private final String filePath = "data/doctors.json";
    private final JsonStore jsonStore;
    private final Type listType = new TypeToken<List<Doctor>>() {}.getType();

    private final List<Doctor> doctors;

    public DoctorRepositoryJSON() {
        this.jsonStore = new JsonStore();
        this.doctors = jsonStore.readFromFile(filePath, listType, new ArrayList<>());
    }

    private void saveChanges() {
        jsonStore.writeToFile(filePath, doctors);
    }

    @Override
    public Optional<User> searchByUsername(String username) {
        return doctors.stream()
                .filter(Doctor::isCurrentStatus)
                .filter(d -> d.getUsername().equals(username.trim()))
                .map(d -> (User) d)
                .findFirst();
    }

    @Override
    public boolean add(Doctor doctor) {
        boolean added = doctors.add(doctor);
        if (added) {
            saveChanges();
        }
        return added;
    }

    @Override
    public boolean deleteById(String id) {
        Optional<Doctor> doctorOpt = doctors.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst();

        if (doctorOpt.isPresent()) {
            doctorOpt.get().setCurrentStatus(false);
            saveChanges();
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Doctor doctor) {
        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getId().equals(doctor.getId())) {
                doctors.set(i, doctor);
                saveChanges();
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<Doctor> searchById(String id) {
        return doctors.stream()
                .filter(Doctor::isCurrentStatus)
                .filter(d -> d.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Doctor> searchBySpecialty(Specialty specialty) {
        return doctors.stream()
                .filter(Doctor::isCurrentStatus)
                .filter(d -> specialty.equals(d.getMedicalSpecialty()))
                .toList();
    }

    @Override
    public List<Doctor> listAll() {
        return doctors.stream()
                .filter(Doctor::isCurrentStatus)
                .toList();
    }
}
