/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Persistance;

import com.google.gson.reflect.TypeToken;
import com.mycompany.Interfaces.IAuthenticableRepository;
import com.mycompany.Interfaces.IPatientRepository;
import com.mycompany.Models.Doctor;
import com.mycompany.Models.Patient;
import com.mycompany.Models.User;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jess
 */
public class PatientRepositoryJSON extends JsonRepository<Patient> implements IAuthenticableRepository, IPatientRepository {
    
    private static final String FILE_NAME = "patient.json";
    private static final Type LIST_TYPE = new TypeToken<List<Doctor>>() {}.getType();

    public PatientRepositoryJSON() {
        super(FILE_NAME, LIST_TYPE);
    }  

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

    @Override
    public boolean add(Patient patient) {
        data.add(patient);
        save();
        return true;
    }

    @Override
    public boolean deleteById(String id) {
        boolean found = (boolean) data.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(patient -> {
                    patient.setCurrentStatus(false);
                    save();
                    return true;
                })
                .orElse(false);
        return found;
    }

    @Override
    public boolean update(Patient patient) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(patient.getId())) {
                data.set(i, patient);
                save();
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<Patient> searchById(String id) {
        return data.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Patient> listAll() {
        try{
            return data.stream()
                    .filter(Patient::isCurrentStatus)
                    .toList();
        } catch (RuntimeException e){
            System.err.println("Error al listar los doctores: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
}
