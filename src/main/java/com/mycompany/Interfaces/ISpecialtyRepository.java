/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.Interfaces;

import com.mycompany.Models.Specialty;
import com.mycompany.Models.SpecialtyName;
import java.util.List;
import java.util.Optional;

/**
 * Esta interfaz define las operaciones CRUD para el manejo de las
 * especialidades médicas en el sistema.
 *
 * <p>
 * Siguiendo el principio de Inversión de Dependencias (DIP), las clases de
 * servicio no dependen directamente de una implementación concreta, sino de
 * esta abstracción.</p>
 *
 * @author David
 */
public interface ISpecialtyRepository {

    /**
     * Permite agregar una nueva especialidad.
     *
     * @param specialty la especialidad que se desea agregar.
     * @return {@code true} si la especialidad fue añadida correctamente,
     * {@code false} en caso contrario.
     */
    boolean add(Specialty specialty);

    /**
     * Actualiza los datos de una especialidad existente.
     *
     * @param specialty la especialidad con los datos actualizados.
     * @return {@code true} si la especialidad fue actualizada correctamente,
     * {@code false} en caso contrario.
     */
    boolean update(Specialty specialty);

    /**
     * Busca una especialidad según su nombre.
     *
     * @param name el nombre de la especialidad (enum {@link SpecialtyName}).
     * @return un {@code Optional} que contiene la especialidad si fue
     * encontrada, o vacío si no existe.
     */
    Optional<Specialty> searchByName(SpecialtyName name);

    /**
     * Lista todas las especialidades almacenadas en el sistema.
     *
     * @return una lista con todas las especialidades registradas.
     */
    List<Specialty> listAll();
}
