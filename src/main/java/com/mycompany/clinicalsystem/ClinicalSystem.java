package com.mycompany.clinicalsystem;

import com.mycompany.Interfaces.*;
import com.mycompany.Models.*;
import com.mycompany.Persistance.*;
import com.mycompany.Presentation.FrmDoctorMenu;
import com.mycompany.Services.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClinicalSystem {

    public static void main(String[] args) {

        // ======== REPOSITORIOS EN MEMORIA ========
        IPatientRepository patientRepo = new PatientRepositoryMemory();
        IDoctorRepository doctorRepo = new DoctorRepositoryMemory();
        IAppointmentRepository appointmentRepo = new AppointmentRepositoryMemory();
        IConsultationRepository consultationRepo = new ConsultationRepositoryMemory();

        // ======== DATOS DE PRUEBA ========

        // Especialidades
        Specialty cardio = new Specialty(SpecialtyName.CARDIOLOGIA);
        Specialty peds = new Specialty(SpecialtyName.PEDIATRIA);

        // Doctores
        Doctor doc1 = new Doctor("drJuanse", "3001112233", "123", "Juan Sebastián Maximiliano Pérez de la Vega y Cía.", "11111111", cardio);
        Doctor doc2 = new Doctor("drlee", "3002223344", "123", "Anna Lee", "22222222", peds);
        doctorRepo.add(doc1);
        doctorRepo.add(doc2);

        // Pacientes
        Patient pat1 = new Patient("juanp", "3100000000", "abc123", "Juan Pérez", "1098765432", LocalDate.of(2000, 5, 12));
        Patient pat2 = new Patient("mariag", "3200000000", "maria456", "María Gómez", "1098123456", LocalDate.of(1998, 8, 30));
        Patient pat3 = new Patient("camitorres", "3154445566", "cami789", "Camila Torres", "1056789123", LocalDate.of(2002, 2, 14));
        patientRepo.add(pat1);
        patientRepo.add(pat2);
        patientRepo.add(pat3);

        // Citas (Appointments)
        Appointment a1 = new Appointment(LocalDateTime.of(2025, 10, 24, 10, 30), Duration.ofMinutes(30), pat1, doc1);
        Appointment a2 = new Appointment(LocalDateTime.of(2025, 10, 24, 11, 15), Duration.ofMinutes(45), pat2, doc1);
        Appointment a3 = new Appointment(LocalDateTime.of(2025, 10, 25, 9, 0), Duration.ofMinutes(20), pat3, doc1);

        a1.setStatus(AppointmentStatus.PROGRAMADA);
        a2.setStatus(AppointmentStatus.COMPLETADA);
        a3.setStatus(AppointmentStatus.PROGRAMADA);

        appointmentRepo.add(a1);
        appointmentRepo.add(a2);
        appointmentRepo.add(a3);

        // Consultas (Consultations)
        Consultation c1 = new Consultation(a2, "Chequeo de control cardíaco", "Todo estable. Continuar medicación.");
        Consultation c2 = new Consultation(a3, "Revisión dermatológica", "Recomendar crema hidratante y evitar exposición solar.");
        consultationRepo.add(c1);
        consultationRepo.add(c2);

        // ======== LISTA DE REPOS AUTENTICABLES ========
        List<IAuthenticableRepository> authRepos = new ArrayList<>();
        authRepos.add((IAuthenticableRepository) patientRepo);
        authRepos.add((IAuthenticableRepository) doctorRepo);

        // ======== SERVICIOS ========
        AuthenticationService auth = new AuthenticationService(authRepos);
        GlobalUsernameValidator validator = new GlobalUsernameValidator(authRepos);
        PatientService patientService = new PatientService(patientRepo, validator);
        DoctorService doctorService = new DoctorService(doctorRepo, validator);
        AppointmentService appointmentService = new AppointmentService(appointmentRepo);
        ConsultationService consultationService = new ConsultationService(consultationRepo);

        // ======== INICIALIZAR SERVICE LOCATOR ========
        ServiceLocator.initialize(patientService, doctorService, auth, appointmentService, consultationService);

        // ======== ARRANCAR DIRECTO EL MENÚ DEL DOCTOR ========
        FrmDoctorMenu menu = new FrmDoctorMenu(doc1); // Muestra el menú del Dr. Smith
        menu.setVisible(true);
    }
}
