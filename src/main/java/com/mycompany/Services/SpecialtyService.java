package com.mycompany.Services;

import com.mycompany.Interfaces.ISpecialtyRepository;
import com.mycompany.Models.Specialty;
import com.mycompany.Models.SpecialtyName;
import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con especialidades médicas.
 * Aplica principios SOLID: SRP, OCP y DIP.
 *
 * @author David
 */
public class SpecialtyService {

    private final ISpecialtyRepository repository;

    public SpecialtyService(ISpecialtyRepository repository) {
        this.repository = repository;
    }

    /**
     * Registra una nueva especialidad.
     *
     * @param specialty La especialidad a registrar.
     * @return true si se registró correctamente, false si ya existe o es
     * inválida.
     */
    public boolean registerSpecialty(Specialty specialty) {
        if (specialty == null
                || specialty.getSpecialtyName() == null) {
            return false;
        }
        // Evita duplicados
        if (repository.searchByName(specialty.getSpecialtyName()).isPresent()) {
            return false;
        }
        return repository.add(specialty);
    }

    /**
     * Actualiza los datos de una especialidad existente.
     *
     * @param id Identificador de la especialidad.
     * @param name Nuevo nombre (enum) de la especialidad.
     *
     * @return true si se actualizó correctamente.
     */
    public boolean updateSpecialty(String id, SpecialtyName name) {
        if (id == null || id.trim().isEmpty() || name == null) {
            return false;
        }
        var existing = repository.listAll()
                .stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();

        if (existing.isEmpty()) {
            return false;
        }

        Specialty s = existing.get();
        s.setSpecialtyName(name);
        return repository.update(s);
    }

    /**
     * Lista todas las especialidades registradas.
     *
     * @return Lista de especialidades.
     */
    public List<Specialty> listAllSpecialties() {
        return repository.listAll();
    }
}
