package com.mycompany.Interfaces;

import java.util.List;
import java.util.Optional;

import com.mycompany.Models.Patient;

public interface IPatientRepository {
    boolean add(Patient patient);
    boolean deleteById(String Id);
    boolean update(Patient patient);
    Optional<Patient> searchById(String Id);
    List<Patient> listAll();
}
