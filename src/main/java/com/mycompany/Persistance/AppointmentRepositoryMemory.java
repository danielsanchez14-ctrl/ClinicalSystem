/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Persistance;

import com.mycompany.Interfaces.IAppointmentRepository;
import com.mycompany.Models.Appointment;
import com.mycompany.Models.AppointmentStatus;
import com.mycompany.Models.Doctor;
import com.mycompany.Models.Patient;
import java.util.ArrayList;
import java.util.List;
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
        if (appointment != null){
            this.appointment.add(appointment); //Si el valor ingresado en el parámetro es diferente de null, se añade la cita ingresada a la lista appointment
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateState(String appointmentID, AppointmentStatus newState) {
        Appointment appt = this.appointment.stream().
                filter(a -> a.getId().equals(appointmentID)). //Selecciona la cita que coincide con el id ingresado
                findFirst(). //Selecciona el primer elemento de los seleccionados por filter, en este caso habrá un solo elemento ya que el id es único.
                orElse(null); //Si no se encontro ninguna cita con el id ingresado, asigna el valor null a la variable appt
        if (appt != null){ 
            appt.setStatus(newState); //Se le asigna el nuevo estado a la cita con el id previamente ingresado
            return true;
        } else {
            return false; //Retorna false en caso de no haber encontrado la cita
        }
    }

    @Override
    public Appointment searchById(String id) {
        return this.appointment.stream(). //appointment -> a
                filter(a -> a.getId().equals(id)). // Selecciona la cita
                findFirst(). //Selecciona el primer elemento de los seleccionados por filter, en este caso habrá un solo elemento ya que el id es único.
                orElse(null); //Si no se encontro ninguna cita con el id ingresado, se retornará un valor null
    }

    @Override
    public List<Appointment> searchByPatient(Patient patient) {
        return this.appointment.stream().
                filter(a -> a.getPatient().equals(patient)). //Selecciona las citas que tiene el paciente ingresado
                collect(Collectors.toList()); //Almacena las citas que tiene el paciente ingresado en una lista
    }

    @Override
    public List<Appointment> searchByDoctor(Doctor doctor) {
        return this.appointment.stream().
                filter(a -> a.getDoctor().equals(doctor)). //Selecciona las citas que tiene el doctor ingresado
                collect(Collectors.toList()); //Almacena las citas que tiene el doctor ingresado en una lista
    }
        
    @Override
    public List<Appointment> listAll(){
        return this.appointment; //Returna la lista de citas
    }
} 