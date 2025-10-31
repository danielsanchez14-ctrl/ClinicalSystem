package com.clinicsystem.tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.Models.Doctor;
import com.mycompany.Models.Specialty;
import com.mycompany.Models.SpecialtyName;
import com.mycompany.Persistance.DoctorRepositoryJSON;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

/**
 * Programa de prueba manual para validar la persistencia completa (CRUD + JSON + soft delete).
 * <p>
 * Ejecuta una serie de operaciones (crear, leer, actualizar, eliminar lógicamente)
 * y muestra por consola el estado y el contenido final del archivo JSON.
 * </p>
 *
 * @author camil
 */
public class TestPersistenceFull {

    public static void main(String[] args) {
        System.out.println("=== 🧪 Iniciando prueba completa de persistencia (CRUD + JSON + Soft Delete) ===");

        // 🧼 Limpiar antes de comenzar (importante: antes de crear el repo)
        File file = new File("data/doctors.json");
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("🧹 Archivo doctors.json eliminado para comenzar desde cero.");
            } else {
                System.out.println("⚠️ No se pudo eliminar doctors.json (verifica permisos o si está abierto).");
            }
        }

        // Crear repo limpio
        DoctorRepositoryJSON repo = new DoctorRepositoryJSON();

        // 1️⃣ CREATE
        System.out.println("\n👉 [CREATE] Creando nuevo doctor...");
        Doctor newDoctor = new Doctor(
                "drHouse",
                "555123456",
                "diagnose123",
                "Gregory House",
                "123456789",
                new Specialty(SpecialtyName.CARDIOLOGIA)
        );
        repo.add(newDoctor);
        System.out.println("✅ Doctor agregado con ID: " + newDoctor.getId());

        // 2️⃣ READ
        System.out.println("\n👉 [READ] Buscando doctor por ID...");
        Optional<Doctor> found = repo.searchById(newDoctor.getId());
        System.out.println(found.isPresent()
                ? "✅ Doctor encontrado: " + found.get().getFullName()
                : "❌ No se encontró el doctor");

        // 3️⃣ UPDATE
        System.out.println("\n👉 [UPDATE] Actualizando número de teléfono...");
        found.ifPresent(doc -> {
            doc.setPhoneNumber("999999999");
            repo.update(doc);
        });
        Optional<Doctor> updated = repo.searchById(newDoctor.getId());
        System.out.println(updated.isPresent()
                ? "✅ Teléfono actualizado: " + updated.get().getPhoneNumber()
                : "❌ No se pudo actualizar el teléfono");

        // 4️⃣ DELETE (Soft delete)
        System.out.println("\n👉 [DELETE] Marcando como inactivo (soft delete)...");
        boolean deleted = repo.deleteById(newDoctor.getId());
        System.out.println(deleted
                ? "✅ Doctor marcado como inactivo"
                : "❌ No se pudo eliminar");

        // 5️⃣ VERIFY SOFT DELETE
        System.out.println("\n👉 [VERIFY] Listando doctores activos...");
        var activeDoctors = repo.listAll();
        System.out.println("Doctores activos: " + activeDoctors.size());
        System.out.println("Debe ser 0 si el único doctor fue inactivado.");

        // 6️⃣ PERSISTENCE CHECK
        System.out.println("\n👉 [PERSISTENCE] Reiniciando repositorio...");
        DoctorRepositoryJSON repoReloaded = new DoctorRepositoryJSON();
        var loaded = repoReloaded.listAll();
        System.out.println("✅ Doctores activos tras recarga: " + loaded.size());
        System.out.println("Si todo está bien, el doctor sigue en el archivo JSON pero no aparece por estar inactivo.");

        // 7️⃣ SHOW JSON CONTENT
        System.out.println("\n📂 [FINAL] Contenido actual del archivo doctors.json:");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileReader reader = new FileReader("data/doctors.json")) {
            Object json = gson.fromJson(reader, Object.class);
            String formatted = gson.toJson(json);
            System.out.println(formatted);
        } catch (IOException e) {
            System.out.println("❌ No se pudo leer el archivo doctors.json: " + e.getMessage());
        }

        System.out.println("\n=== 🧩 Prueba finalizada ===");
    }
}
