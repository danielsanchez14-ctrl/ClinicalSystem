/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.Interfaces;

import com.mycompany.Models.Consultation;
import java.util.List;

/**
 *
 * @author kosmo
 */
public interface IConsultationRepository {

    public boolean add(Consultation consultation);

    public List<Consultation> searchByPatient(String patientID);

    public List<Consultation> searchByDoctor(String doctorID);

    public List<Consultation> listAll();

}
