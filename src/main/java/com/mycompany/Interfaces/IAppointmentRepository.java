/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.Interfaces;

import com.mycompany.Models.Appointment;
import com.mycompany.Models.AppointmentStatus;
import com.mycompany.Models.Doctor;
import com.mycompany.Models.Patient;
import java.util.List;

/**
 *
 * @author mateo
 */
public interface IAppointmentRepository {
    public boolean add(Appointment appointment);
    
    public boolean updateState(String appointmentID, AppointmentStatus newState);
    
    public Appointment searchById(String id);
    
    public List<Appointment> searchByPatient(Patient patient);
    
    public List<Appointment> searchByDoctor(Doctor doctor);
    
    public List<Appointment> listAll();
}
