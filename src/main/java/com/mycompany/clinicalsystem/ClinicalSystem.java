/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.clinicalsystem;

import com.mycompany.Interfaces.IAuthenticableRepository;
import com.mycompany.Interfaces.IConsultationRepository;
import com.mycompany.Interfaces.IPatientRepository;
import com.mycompany.Models.Appointment;
import com.mycompany.Models.Consultation;
import com.mycompany.Models.Doctor;
import com.mycompany.Models.Patient;
import com.mycompany.Models.Specialty;
import com.mycompany.Models.SpecialtyName;
import com.mycompany.Persistance.ConsultationRepositoryMemory;
import com.mycompany.Persistance.PatientRepositoryMemory;
import com.mycompany.Presentation.FrmLogin;
import com.mycompany.Presentation.FrmRegisterPatient;
import com.mycompany.Services.AuthenticationService;
import com.mycompany.Services.ConsultationService;
import com.mycompany.Services.GlobalUsernameValidator;
import com.mycompany.Services.PatientService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kosmo
 */
public class ClinicalSystem {

    public static void main(String[] args) {
        IPatientRepository repo = new PatientRepositoryMemory();
        List<IAuthenticableRepository> list = new ArrayList<>();
        list.add((IAuthenticableRepository) repo);
        AuthenticationService auth = new AuthenticationService(list);
        GlobalUsernameValidator validator = new GlobalUsernameValidator(list);
        PatientService service = new PatientService(repo, validator);
        FrmLogin l = new FrmLogin(service, auth);
        l.setVisible(true);
    }
}
