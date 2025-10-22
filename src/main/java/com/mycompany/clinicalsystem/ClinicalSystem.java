/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.clinicalsystem;

import com.mycompany.Interfaces.IConsultationRepository;
import com.mycompany.Models.Appointment;
import com.mycompany.Models.Consultation;
import com.mycompany.Persistance.ConsultationRepositoryMemory;
import com.mycompany.Services.ConsultationService;

/**
 *
 * @author kosmo
 */
public class ClinicalSystem {

    public static void main(String[] args) {
        
        //Crea el repositorio en memoria
        IConsultationRepository repo = new ConsultationRepositoryMemory();
        //Crea el servicio
        ConsultationService service = new ConsultationService(repo);
        //Crea una cita falsa
        Appointment ap1 = new Appointment();
        /*
        Consultation co1 = new Consultation(ap1, "Gripe", "Dolex");
        
        //Registrar consulta
        boolean result1 = service.createConsultation(co1);
        System.out.println("Consulta 1 creada: " + result1);
        
        //Duplicar la misma cita
        Consultation co2 = new Consultation(ap1, "Otra vez", "C murió");
        boolean result2 = service.createConsultation(co2);
        System.out.println("Consulta 2 duplicada creada: " + result2);
        
        //Crear otra cita diferente
        Appointment ap2 = new Appointment();
        Consultation co3 = new Consultation(ap2, "Dolor de cabeza", "Reposo");
        
        boolean result3 = service.createConsultation(co3);
        
        System.out.println("Consulta 3 creada " + result3);
        
        //Consultas no válidas
        Consultation co4 = new Consultation();
        
        boolean result4 = service.createConsultation(co4);
        System.out.println("Consulta 4 creada: "+ result4);
        */
        
        Consultation c = new Consultation(ap1, "d", "");
        boolean result = service.createConsultation(c);
        System.out.println(result);
    }
}
