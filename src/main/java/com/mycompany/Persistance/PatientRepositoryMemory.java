package com.mycompany.Persistance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mycompany.Interfaces.IAuthenticableRepository;
import com.mycompany.Interfaces.IPatientRepository;
import com.mycompany.Models.Patient;
import com.mycompany.Models.User;

public class PatientRepositoryMemory implements IAuthenticableRepository, IPatientRepository {
    List<Patient> patients = new ArrayList<>();

    @Override
    public boolean add(Patient patient) {
        if (patient==null||searchById(patient.getId()).isPresent()){
            return false;
        }
        return patients.add(patient);
    }

    @Override
    public boolean deleteById(String Id) {
        return patients.removeIf(p -> p.getId().equals(Id));
    }

    @Override
    public boolean update(Patient patient) {
        for(int i = 0; i < patients.size();i++){
            if (patients.get(i).getId().equals(patient.getId())){
                patients.set(i, patient);
                return true;
            }
        }
        return false;}

    @Override
    public Optional<Patient> searchById(String Id) {
        return patients.stream().filter(p -> p.getId().equals(Id)).findFirst();
    }

    @Override
    public List<Patient> listAll() {
        return new ArrayList<>(patients);
    }

    @Override
    public Optional<User> searchByUsername(String username) {
        return patients.stream()
                .filter(p -> p.getUsername().equalsIgnoreCase(username))
                .map(p -> (User) p)
                .findFirst();
    }


}
