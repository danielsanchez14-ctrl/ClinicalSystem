package com.mycompany.Services;

import com.mycompany.Interfaces.ISpecialtyRepository;
import com.mycompany.Models.Specialty;
import com.mycompany.Models.SpecialtyName;
import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con especialidades médicas.
 * Aplica principios SOLID: SRP, OCP y DIP.
 * @author David
 */
public class SpecialtyService {

    private final ISpecialtyRepository repository;

    public SpecialtyService(ISpecialtyRepository repository) {
        this.repository = repository;
    }

    /**
     * Registra una nueva especialidad.
     * @param specialty La especialidad a registrar.
     * @return true si se registró correctamente, false si ya existe o es inválida.
     */
    public boolean registerSpecialty(Specialty specialty) {
        if (specialty == null 
                || specialty.getSpecialtyName() == null 
                || specialty.getDescription() == null 
                || specialty.getDescription().trim().isEmpty()) {
            return false;
        }
        // Evita duplicados
        if (repository.searchByName(specialty.getSpecialtyName()).isPresent()) {
            return false;
        }
        return repository.add(specialty);
    }

    /**
     * Elimina una especialidad.
     * @param specialty La especialidad a eliminar.
     * @return true si se eliminó correctamente, false si no existe.
     */
    public boolean removeSpecialty(Specialty specialty) {
        if (specialty == null || specialty.getId() == null) {
            return false;
        }
        return repository.deleteById(specialty.getId());
    }

    /**
     * Actualiza los datos de una especialidad existente.
     * @param id Identificador de la especialidad.
     * @param name Nuevo nombre (enum) de la especialidad.
     * @param description Nueva descripción.
     * @return true si se actualizó correctamente.
     */
    public boolean updateSpecialty(String id, SpecialtyName name, String description) {
        if (id == null || id.trim().isEmpty() || name == null) {
            return false;
        }
        var existing = repository.listAll()
                .stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();

        if (existing.isEmpty()) return false;

        Specialty s = existing.get();
        s.setSpecialtyName(name);
        s.setDescription(description != null ? description.trim() : "");
        return repository.update(s);
    }

    /**
     * Lista todas las especialidades registradas.
     * @return Lista de especialidades.
     */
    public List<Specialty> listAllSpecialties() {
        return repository.listAll();
    }
}
