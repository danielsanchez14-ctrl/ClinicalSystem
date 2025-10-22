/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Services;

import com.mycompany.Interfaces.IAuthentication;
import com.mycompany.Interfaces.IAuthenticableRepository;
import com.mycompany.Models.Patient;
import com.mycompany.Models.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author camil
 */
public class AuthenticationService implements IAuthentication {

    private final List<IAuthenticableRepository> repositories;
    private User currentUser;

    public AuthenticationService(List<IAuthenticableRepository> repositories) {
        this.repositories = repositories != null ? repositories : new ArrayList<>();
        this.currentUser = null;
    }

    @Override
    public Optional<User> login(String userName, String password) {
        for (IAuthenticableRepository repo : repositories) {
            Optional<User> userOpt = repo.searchByUsername(userName);

            if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
                currentUser = userOpt.get();
                currentUser.setCurrentStatus(true);
                return Optional.of(currentUser);
            }
        }
        return Optional.empty();
    }

    @Override
    public void logout() {
        if (currentUser != null) {
            currentUser.setCurrentStatus(false);
            currentUser = null;
        }
    }

    @Override
    public Optional<Patient> registerPatient(Patient patient) {
        for (IAuthenticableRepository repo : repositories){
           // TODO: activar cuando IPatientRepository esté implementado
            /*if (repo instanceof Interfaces.IPatientRepository patientRepo){
                boolean added = patientRepo.addPatient(patient);
                if (added) return Optional.of(patient);
            }*/
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

}
