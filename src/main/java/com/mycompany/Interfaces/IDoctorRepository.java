/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.Interfaces;

import com.mycompany.Models.Doctor;
import com.mycompany.Models.Specialty;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author camil
 */
public interface IDoctorRepository {
    
    boolean add(Doctor doctor);
    boolean delete(String id);
    boolean update(Doctor doctor);

    Optional<Doctor> searchById(String id);
    List<Doctor> searchBySpecialty(Specialty specialty);
    List<Doctor> listAll();
}
