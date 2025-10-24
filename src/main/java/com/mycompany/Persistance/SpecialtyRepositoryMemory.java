/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Persistance;

import com.mycompany.Interfaces.ISpecialtyRepository;
import com.mycompany.Models.Specialty;
import com.mycompany.Models.SpecialtyName;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Esta clase implementa la interfaz {@link ISpecialtyRepository} y gestiona las
 * especialidades médicas en memoria.
 *
 * <p>
 * Utiliza una lista interna para almacenar las especialidades, cumpliendo el
 * principio de Inversión de Dependencias (DIP) al ofrecer una implementación
 * concreta de la abstracción.</p>
 *
 * @author David
 */
public class SpecialtyRepositoryMemory implements ISpecialtyRepository {

    /**
     * Lista interna que almacena las especialidades registradas.
     */
    private final List<Specialty> specialties = new ArrayList<>();

    /**
     * Agrega una nueva especialidad al repositorio.
     *
     * @param specialty la especialidad que se desea agregar.
     * @return {@code true} si se agregó correctamente, {@code false} si ya
     * existe.
     */
    @Override
    public boolean add(Specialty specialty) {

        return specialties.add(specialty);
    }

    /**
     * Actualiza los datos de una especialidad existente.
     *
     * @param specialty la especialidad actualizada.
     * @return {@code true} si se actualizó correctamente, {@code false} si no
     * se encontró.
     */
    @Override
    public boolean update(Specialty specialty) {
        for (int i = 0; i < specialties.size(); i++) {
            if (specialties.get(i).getId().equals(specialty.getId())) {
                specialties.set(i, specialty);
                return true;
            }
        }
        return false;
    }

    /**
     * Busca una especialidad por su nombre (enum {@link SpecialtyName}).
     *
     * @param name el nombre de la especialidad.
     * @return un {@code Optional} con la especialidad encontrada, o vacío si no
     * existe.
     */
    @Override
    public Optional<Specialty> searchByName(SpecialtyName name) {
        return specialties.stream()
                .filter(s -> s.getSpecialtyName() == name)
                .findFirst();
    }

    /**
     * Lista todas las especialidades almacenadas.
     *
     * @return una lista con todas las especialidades registradas.
     */
    @Override
    public List<Specialty> listAll() {
        return new ArrayList<>(specialties);
    }
}
