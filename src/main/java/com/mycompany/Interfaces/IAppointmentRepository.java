/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.Interfaces;

import com.mycompany.Models.Appointment;
import com.mycompany.Models.AppointmentStatus;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author mateo
 */
public interface IAppointmentRepository {
    public boolean add(Appointment appointment);
    
    public boolean updateState(String appointmentID, AppointmentStatus newState);
    
    public Optional<Appointment> searchById(String id);
    
    public List<Appointment> searchByPatient(String patientId);
    
    public List<Appointment> searchByDoctor(String doctorId);
    
    public List<Appointment> listAll();
}
