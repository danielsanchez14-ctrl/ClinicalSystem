package com.mycompany;

import com.mycompany.Models.Doctor;
import com.mycompany.Models.Specialty;
import com.mycompany.Models.SpecialtyName;
import com.mycompany.Persistance.DoctorRepositoryJSON;

import java.util.Optional;

public class TestPersistenceFull {
    public static void main(String[] args) {

        DoctorRepositoryJSON repo = new DoctorRepositoryJSON();

        System.out.println("=== 💾 Prueba de persistencia JSON ===");
        System.out.println("Doctores cargados actualmente: " + repo.listAll().size());

        // Buscar al doctor por username
        String username = "dr.houser";
        Optional<Doctor> existingDoctor = repo.searchByUsername(username)
                .map(u -> (Doctor) u);

        if (existingDoctor.isEmpty()) {
            System.out.println("🆕 No existe el doctor, creando uno nuevo...");

            Specialty specialty = new Specialty(SpecialtyName.CARDIOLOGIA);
            Doctor doctor = new Doctor(
                    username,
                    "3205559988",
                    "vicodin123",
                    "Gregory House",
                    "12345678",
                    specialty
            );

            repo.add(doctor);
            System.out.println("✅ Doctor creado y guardado en data/doctors.json");

        } else {
            Doctor doctor = existingDoctor.get();
            System.out.println("✅ Doctor encontrado: " + doctor.getFullName());

            // Simula una actualización
            System.out.println("✏️  Actualizando número de teléfono...");
            doctor.setPhoneNumber("3200000000");
            repo.update(doctor);
            System.out.println("📞 Nuevo número guardado: " + doctor.getPhoneNumber());
        }

        // Mostrar doctores actuales
        System.out.println("\n📋 Lista actual de doctores:");
        repo.listAll().forEach(d ->
                System.out.println("- " + d.getFullName() + " (" +
                        d.getMedicalSpecialty().getSpecialtyName() + ")")
        );

        System.out.println("\n✅ Fin del programa. Vuelve a ejecutarlo para comprobar persistencia.");
    }
}
