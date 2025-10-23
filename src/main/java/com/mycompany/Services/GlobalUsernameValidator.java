/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Services;

import com.mycompany.Interfaces.IAuthenticableRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase se encarga de garantizar la unicidad de los nombres de usuario. De
 * esta manera, se evita que pacientes tengan nombres de usuarios que coincidan
 * con los de médicos.
 *
 * @author kosmo
 */
public class GlobalUsernameValidator {

    private final List<IAuthenticableRepository> repositories;

    /**
     * Constructor de la clase.
     *
     * @param repositories Lista de repositorios autenticables (pacientes y
     * doctores).
     */
    public GlobalUsernameValidator(List<IAuthenticableRepository> repositories) {
        this.repositories = repositories != null ? repositories : new ArrayList<>();
    }

    /**
     * Verifica si el nombre de usuario ya existe en cualquiera de los
     * repositorios
     *
     * @param username el nombre de usuario que se desea verificar.
     * @param excludeUserId id del usuario a excluir de la búsqueda (puede ser
     * null)
     * @return true si dicho nombre ya está en uso.
     */
    public boolean usernameExists(String username, String excludeUserId) {

        String trimmedUsername = username.trim();
        for (IAuthenticableRepository repo : repositories) {
            var found = repo.searchByUsername(trimmedUsername);
            if (found.isPresent()) {

                if (excludeUserId != null && found.get().getId().equals(excludeUserId)) {
                    continue; //En caso de que el usuario que encontró es el exluido
                }
                return true; //Ya existe ese usuario.
            }
        }

        return false; //Si el ciclo termina sin encontrar nada
    }
}
