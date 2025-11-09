package com.mycompany.Persistance;

import com.google.gson.reflect.TypeToken;
import com.mycompany.Interfaces.ISpecialtyRepository;
import com.mycompany.Models.Specialty;
import com.mycompany.Models.SpecialtyName;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la persistencia de especialidades médicas utilizando almacenamiento en JSON.
 * <p>
 * Esta clase maneja operaciones CRUD básicas sobre el archivo specialties.json,
 * extendiendo {@link JsonRepository} para heredar la lógica de carga y guardado.
 * </p>
 * 
 * @author camil
 */
public class SpecialtyRepositoryJSON extends JsonRepository<Specialty> implements ISpecialtyRepository {

    private static final String FILE_PATH = "specialties.json";
    private static final Type LIST_TYPE = new TypeToken<List<Specialty>>() {}.getType();

    /**
     * Constructor que inicializa el repositorio y carga los datos desde el archivo JSON.
     */
    public SpecialtyRepositoryJSON() {
        super(FILE_PATH, LIST_TYPE);
    }

     /**
     * Agrega una nueva especialidad al repositorio.
     *
     * @param specialty Especialidad a agregar. No puede ser nula.
     * @return true si la operación fue exitosa.
     * @throws IllegalArgumentException si {@code specialty} es nulo.
     */
    @Override
    public boolean add(Specialty specialty){
        if (specialty == null) {
            throw new IllegalArgumentException("La especialidad no puede ser nula");
        }
        try {
            data.add(specialty);
            save();
            return true;
        } catch (RuntimeException e) {
            System.err.println("Error al agregar la especialidad: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza una especialidad existente.
     *
     * @param specialty Especialidad con los datos actualizados. No puede ser nula.
     * @return true si se encontró y actualizó; false en caso contrario.
     * @throws IllegalArgumentException si {@code specialty} es nulo.
     */
    @Override
    public boolean update(Specialty specialty) {
        if (specialty == null) {
            throw new IllegalArgumentException("La especialidad no puede ser nula");
        }

        try {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getId().equals(specialty.getId())) {
                    data.set(i, specialty);
                    save();
                    return true;
                }
            }
            return false;
        } catch (RuntimeException e) {
            System.err.println("Error al actualizar la especialidad: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca una especialidad por su nombre.
     *
     * @param name Nombre de la especialidad. No puede ser nulo.
     * @return Un {@link Optional} que contiene la especialidad encontrada, o vacío si no existe.
     * @throws IllegalArgumentException si {@code name} es nulo.
     */
    @Override
    public Optional<Specialty> searchByName(SpecialtyName name) {
        if (name == null) {
            throw new IllegalArgumentException("El nombre de la especialidad no puede ser nulo");
        }
        try {
            return data.stream()
                    .filter(s -> s.getSpecialtyName() == name) // ok porque es enum
                    .findFirst();
        } catch (RuntimeException e) {
            System.err.println("Error al buscar especialidad por nombre: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Lista todas las especialidades registradas.
     *
     * @return Lista de especialidades.
     */
    @Override
    public List<Specialty> listAll() {
        try {
            return new ArrayList<>(data);
        } catch (RuntimeException e) {
            System.err.println("Error al listar las especialidades: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
