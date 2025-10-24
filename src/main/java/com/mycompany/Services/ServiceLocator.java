/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Services;

/**
 * Esta clase se encarga de facilitar la inyección de servicios en las clases
 * tipo Frame.
 *
 * @author kosmo
 */
public class ServiceLocator { //this.service = ServiceLocator.instance.getPatientService();
    //Atributos

    /**
     * Este es un atributo estático que almacena la instancia de la clase
     */
    private static ServiceLocator instance; //Instancia de la clase

    //Servicios que guarda (atributos de instancia)
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AuthenticationService authenticationService;
    private final AppointmentService appointmentService;
    private final ConsultationService consultationService;
    //private SpecialtyService specialtyService; (PENDIENTE)

    /**
     * Constructor. En este caso es privado, ya que el método initialize hace
     * uso de él.
     */
    private ServiceLocator(PatientService patientService,
            DoctorService doctorService,
            AuthenticationService authenticationService,
            AppointmentService appointmentService,
            ConsultationService consultationService
    ) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.authenticationService = authenticationService;
        this.appointmentService = appointmentService;
        this.consultationService = consultationService;
        //this.specialtyService = specialtyService;
    }

    /**
     * Este es el método que inicializa el servico, recibe como parámetros los
     * servicios e instancia un objeto ServiceLocator, que se almacena en el
     * atributo estático.
     *
     * @param patientService el servicio de pacientes.
     * @param doctorService el servicio de doctores.
     * @param authenticationService el servicio de autenticación.
     * @param appointmentService el servicio de citas.
     * @param consultationService el servicio de consultas.
     */
    public static void initialize(PatientService patientService,
            DoctorService doctorService,
            AuthenticationService authenticationService,
            AppointmentService appointmentService,
            ConsultationService consultationService
    ) {
        if (instance == null) {
            instance = new ServiceLocator(patientService, doctorService,
                    authenticationService, appointmentService, consultationService);
        }
    }

    public static ServiceLocator getInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    /**
     * @return the patientService
     */
    public PatientService getPatientService() {
        return patientService;
    }

    /**
     * @return the doctorService
     */
    public DoctorService getDoctorService() {
        return doctorService;
    }

    /**
     * @return the authenticationService
     */
    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    /**
     * @return the appointmentService
     */
    public AppointmentService getAppointmentService() {
        return appointmentService;
    }

    /**
     * @return the consultationService
     */
    public ConsultationService getConsultationService() {
        return consultationService;
    }

}
