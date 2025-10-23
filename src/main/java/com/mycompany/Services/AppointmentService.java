/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Services;

import com.mycompany.Interfaces.IAppointmentRepository;
import com.mycompany.Models.Appointment;
import com.mycompany.Models.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 *
 * @author mateo
 */
public class AppointmentService {
    private final IAppointmentRepository repo;
    
    public AppointmentService(IAppointmentRepository repo){
        this.repo = repo;
    }
    
    /*
      Permite agendar citas y aplicar las validaciones.
    
      @param appointment es la cita a agendar.
      @return un boolean que indica si la cita se agendó correctamente.
    */
    public boolean scheduleAppointment(Appointment appointment){
        try{
            if (appointment == null){
                return false;
            } else if(appointment.getId() == null || appointment.getId().isBlank()){
                return false;
            } else if (appointment.getDuration() == null || appointment.getDurationAsString().isBlank()){
                return false;
            } else if (appointment.getPatient() == null){
                return false;
            } else if (appointment.getDoctor() == null){
                return false;
            } else if (appointment.getScheduledAt() == null || appointment.getScheduledAtAsString().isBlank()){
                return false; 
            }
            
            //Verificar que la fecha no sea pasada.
            if (appointment.getScheduledAt().isBefore(LocalDateTime.now())){
                return false;
            }
            var appointmentID = appointment.getId();
            Optional<Appointment> duplicatedAppointment = this.repo.searchById(appointmentID);
            if (duplicatedAppointment.isPresent()){
                return false;
            }
            
            //Se añade la cita.
            return this.repo.add(appointment);
            
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    /*
      Permite actualizar el estado de las citas y aplicar las validaciones.
    
      @param id es el id de la cita.
      @param status es el nuevo estado que se le asginará a la cita.
      @return un boolean que indica si se actualizo correctamente el estado de la cita.
    */
    public boolean updateAppointmentStatus(String id, AppointmentStatus status){
        try {
            if(id == null || id.isBlank() || status == null){
                return false;
            }
            
            Optional<Appointment> appointmentFound = this.repo.searchById(id); //Se almacena en una variable la cita ingresada.
            if (appointmentFound.isEmpty()){
                return false;
            }
            
            //Se actualiza el estado de la cita.
            return this.repo.updateState(id, status);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    
    /*
      Retorna la lista de citas de un doctor en específico.
    
      @param id es el id del doctor.
      @param status es el estado de la cita.
      @return una lista con las citas asociadas al id y el estado ingresados. 
              Si el id ingresado es incorrecto o el doctor no tiene citas asociadas con el estado ingresado,
              o directamente no tiene ninguna cita en su historial. Se retornará una lista vacía.
    */
    public List<Appointment> getAppointmentsByDoctor(String id, AppointmentStatus status){
        try {
            if (id == null || id.isBlank()){
                return List.of(); //Retorna lista vacía.
            }
            
            //Almacenar en una variable una lista con las citas asociadas al id ingresado.
            List<Appointment> doctorAppointments = this.repo.searchByDoctor(id);
            
            if (doctorAppointments.isEmpty()){
                return List.of(); //Retorna lista vacía.
            }
            
            //Filtrar citas por estado
            return doctorAppointments.stream().
                    filter(a -> a.getStatus().equals(status)).
                    collect(Collectors.toList());
            
        } catch (Exception e){
            System.out.println(e.getMessage());
            return List.of(); //Retorna lista vacía.
        }
    }
    
    
    /*
      Retorna la lista de citas de un paciente en específico.
    
      @param id es el id del paciente.
      @param status es el estado de la cita.
      @return una lista con las citas asociadas al id y el estado ingresados. 
              Si el id ingresado es incorrecto o el paciente no tiene citas asociadas con el estado ingresado,
              o directamente no tiene ninguna cita en su historial. Se retornará una lista vacía.
    */
    
    public List<Appointment> getAppointmentsByPatient(String id, AppointmentStatus status){
        try {
            if (id == null || id.isBlank()){
                return List.of(); //Retorna lista vacía.
            }
            
            //Almacenar en una variable una lista con las citas asociadas al id ingresado
            List<Appointment> patientAppointments = this.repo.searchByPatient(id);
            if (patientAppointments.isEmpty()){
                return List.of(); //Retorna lista vacía.
            }
            
            //Filtrar citas por estado
            return patientAppointments.stream().
                    filter(a -> a.getStatus().equals(status)).
                    collect(Collectors.toList());
            
        } catch (Exception e){
            System.out.println(e.getMessage());
            return List.of(); //Retorna lista vacía.
        }
    }
    
    
    /*
      Retorna la lista de citas de una fecha en específico.
    
      @param date es la fecha.
      @return una lista con las citas asociadas a la fecha ingresada. 
    */
    
    public List<Appointment> getAppointmentByDate(LocalDateTime date){
        try {
            if (date == null){
                return List.of();//Retorna lista vacía.
            }
            
            //Filtrar citas según la fecha ingresada.
            return (List<Appointment>) this.repo.listAll().stream().
                    filter(a -> a.getScheduledAt().equals(date));
        } catch (Exception e){
            System.out.println(e.getMessage());
            return List.of();//Retorna lista vacía.
        }
    }
}
