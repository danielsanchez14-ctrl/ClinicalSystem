/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Persistance;

import com.mycompany.Interfaces.IAppointmentRepository;
import com.mycompany.Models.Appointment;
import com.mycompany.Models.AppointmentStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author mateo
 */
public class AppointmentRepositoryMemory implements IAppointmentRepository{
    private final List<Appointment> appointment;
    
    public AppointmentRepositoryMemory(){ //Crea una lista nueva cada vez que se crea el repositorio
        this.appointment = new ArrayList<>();
    }
    
    public AppointmentRepositoryMemory(List<Appointment> appointment){ //Usa una lista ya existente la cual es enviada a través de un argumento
        this.appointment = appointment;
    }

    @Override
    public boolean add(Appointment appointment) {
        this.appointment.add(appointment); //Si el valor ingresado en el parámetro es diferente de null, se añade la cita ingresada a la lista appointment
        return true;
    }

    @Override
    public boolean updateState(String appointmentID, AppointmentStatus newState) {
        Optional<Appointment> appt = this.appointment.stream().
                filter(a -> a.getId().equals(appointmentID)). //Selecciona la cita que coincide con el id ingresado
                findFirst(); //Selecciona el primer elemento de los seleccionados por filter, en este caso habrá un solo elemento ya que el id es único.
        if (appt.isPresent()){ 
            appt.get().setStatus(newState); //Se le asigna el nuevo estado a la cita con el id previamente ingresado
            return true;
        } 
        
        return false; //Retorna false en caso de no haber encontrado la cita
        
    }

    @Override
    public Optional<Appointment> searchById(String id) {
        return this.appointment.stream(). //appointment -> a
                filter(a -> a.getId().equals(id)). // Selecciona la cita
                findFirst(); //Selecciona el primer elemento de los seleccionados por filter, en este caso habrá un solo elemento ya que el id es único.
    }

    @Override
    public List<Appointment> searchByPatient(String patientId) {
        return this.appointment.stream().
                filter(a -> a.getPatient().getId().equals(patientId)). //Selecciona las citas que coinciden con el id del paciente ingresado
                collect(Collectors.toList()); //Almacena las citas que tiene el paciente en una lista
    }

    @Override
    public List<Appointment> searchByDoctor(String doctorId) {
        return this.appointment.stream().
                filter(a -> a.getDoctor().getId().equals(doctorId)). //Selecciona las citas que coinciden con el id del doctor ingresado
                collect(Collectors.toList()); //Almacena las citas que tiene el doctor en una lista
    }
    
    @Override
    public List<Appointment> listAll(){
        return this.appointment; //Returna la lista de citas
    }
} 