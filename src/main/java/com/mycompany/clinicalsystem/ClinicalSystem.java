package com.mycompany.clinicalsystem;

import com.mycompany.Interfaces.*;
import com.mycompany.Models.*;
import com.mycompany.Persistance.*;
import com.mycompany.Presentation.FrmLogin;
import com.mycompany.Services.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClinicalSystem {

        public static void main(String[] args) {

                // ======== REPOSITORIOS ========
                IPatientRepository patientRepo = new PatientRepositoryJSON();
                IDoctorRepository doctorRepo = new DoctorRepositoryJSON();
                IAppointmentRepository appointmentRepo = new AppointmentRepositoryJSON();
                IConsultationRepository consultationRepo = new ConsultationRepositoryJSON();
                ISpecialtyRepository specialtyRepo = new SpecialtyRepositoryJSON();

                // ======== CARGAR DATOS DE PRUEBA SOLO SI ESTÁ VACÍO ========
                if (doctorRepo.listAll().isEmpty()
                                && patientRepo.listAll().isEmpty()
                                && appointmentRepo.listAll().isEmpty()
                                && consultationRepo.listAll().isEmpty()
                                && specialtyRepo.listAll().isEmpty()) {

                        System.out.println("Inicializando datos de prueba (archivos vacíos)...");

                        // --- ESPECIALIDADES ---
                        Specialty cardio = new Specialty(SpecialtyName.CARDIOLOGIA);
                        Specialty peds = new Specialty(SpecialtyName.PEDIATRIA);
                        Specialty general = new Specialty(SpecialtyName.GENERAL);
                        Specialty internista = new Specialty(SpecialtyName.INTERNISTA);

                        specialtyRepo.add(new Specialty(SpecialtyName.CARDIOLOGIA));
                        specialtyRepo.add(new Specialty(SpecialtyName.CIRUJANO));
                        specialtyRepo.add(new Specialty(SpecialtyName.GENERAL));
                        specialtyRepo.add(new Specialty(SpecialtyName.INTERNISTA));
                        specialtyRepo.add(new Specialty(SpecialtyName.ONCOLOGIA));

                        // --- DOCTORES ---
                        Doctor doc1 = new Doctor("drJuanse", "3001112233", "123",
                                        "Juan Sebastián Maximiliano Pérez de la Vega", "11111111", cardio);
                        Doctor doc2 = new Doctor("drLee", "3002223344", "123",
                                        "Anna Lee", "22222222", peds);
                        Doctor doc3 = new Doctor("drRojas", "3003334455", "123",
                                        "Andrés Rojas", "33333333", internista);
                        Doctor doc4 = new Doctor("drSmith", "3007344089", "123",
                                        "John Smith", "44444444", general);

                        doctorRepo.add(doc1);
                        doctorRepo.add(doc2);
                        doctorRepo.add(doc3);
                        doctorRepo.add(doc4);

                        // --- PACIENTES ---
                        Patient pat1 = new Patient("juanp", "3100000000", "abc123",
                                        "Juan Pérez", "1098765432", LocalDate.of(2000, 5, 12));
                        Patient pat2 = new Patient("mariag", "3200000000", "maria456",
                                        "María Gómez", "1098123456", LocalDate.of(1998, 8, 30));
                        Patient pat3 = new Patient("camitorres", "3154445566", "cami789",
                                        "Camila Torres", "1056789123", LocalDate.of(2002, 2, 14));
                        Patient pat4 = new Patient("pedro", "3115556677", "pedro123",
                                        "Pedro Morales", "1078945612", LocalDate.of(1995, 1, 9));

                        patientRepo.add(pat1);
                        patientRepo.add(pat2);
                        patientRepo.add(pat3);
                        patientRepo.add(pat4);

                        // --- APPOINTMENTS ---
                        Appointment a1 = new Appointment(
                                        LocalDateTime.of(2025, 10, 24, 10, 30), Duration.ofMinutes(30), pat1, doc1);
                        Appointment a2 = new Appointment(
                                        LocalDateTime.of(2025, 10, 24, 11, 15), Duration.ofMinutes(45), pat2, doc1);
                        Appointment a3 = new Appointment(
                                        LocalDateTime.of(2025, 10, 25, 9, 0), Duration.ofMinutes(20), pat3, doc1);
                        Appointment a4 = new Appointment(
                                        LocalDateTime.of(2025, 10, 26, 14, 0), Duration.ofMinutes(40), pat4, doc1);
                        Appointment a5 = new Appointment(
                                        LocalDateTime.of(2025, 10, 27, 15, 0), Duration.ofMinutes(30), pat3, doc2);

                        a1.setStatus(AppointmentStatus.PROGRAMADA);
                        a2.setStatus(AppointmentStatus.COMPLETADA);
                        a3.setStatus(AppointmentStatus.PROGRAMADA);
                        a4.setStatus(AppointmentStatus.CANCELADA);
                        a5.setStatus(AppointmentStatus.PROGRAMADA);

                        appointmentRepo.add(a1);
                        appointmentRepo.add(a2);
                        appointmentRepo.add(a3);
                        appointmentRepo.add(a4);
                        appointmentRepo.add(a5);

                        // --- CONSULTAS ---
                        Consultation c1 = new Consultation(
                                        a2,
                                        "Chequeo de control cardíaco",
                                        "Todo estable. Continuar medicación.");
                        consultationRepo.add(c1);

                        Consultation c2 = new Consultation(
                                        new Appointment(LocalDateTime.of(2025, 8, 10, 9, 0),
                                                        Duration.ofMinutes(30), pat1, doc1),
                                        "Evaluación postoperatoria",
                                        "Sin complicaciones. Revisión en 6 meses.");
                        c2.getAppointment().setStatus(AppointmentStatus.COMPLETADA);
                        consultationRepo.add(c2);

                        System.out.println("Datos de prueba cargados.");
                }

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
                SpecialtyService specialtyService = new SpecialtyService(specialtyRepo);

                // ======== SERVICE LOCATOR ========
                ServiceLocator.initialize(
                                patientService,
                                doctorService,
                                auth,
                                appointmentService,
                                consultationService,
                                specialtyService);

                // ======== INICIO ========
                FrmLogin login = new FrmLogin(ServiceLocator.getInstance().getAuthenticationService());
                login.setVisible(true);
        }
}
