/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Services;

import com.mycompany.Interfaces.IAuthentication;
import com.mycompany.Interfaces.IAuthenticableRepository;
import com.mycompany.Models.Patient;
import com.mycompany.Models.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Sercicio encargado de manejar la autenticación de usuarios, incluyendo
 * session mannagement.
 *
 * @author camil
 */
public class AuthenticationService implements IAuthentication {

    private final List<IAuthenticableRepository> repositories;
    private User currentUser;

    /**
     * Constructor de la clase AuthenticationService.
     *
     * @param repositories Lista de repositorios autenticables.
     */
    public AuthenticationService(List<IAuthenticableRepository> repositories) {
        this.repositories = repositories != null ? repositories : new ArrayList<>();
        this.currentUser = null;
    }

    /**
     * Permite a un usuario iniciar sesión si es un usuario activo y las
     * credenciales son correctas.
     *
     * @param userName Nombre de usuario.
     * @param password Contraseña del usuario.
     * @return Un {@code Optional} que contiene el usuario autenticado si las
     * credenciales son válidas; de lo contrario, un {@code Optional.empty()}.
     */
    @Override
    public Optional<User> login(String userName, String password) {
        if (userName == null || userName.trim().isEmpty()) {
            return Optional.empty();
        }

        if (password == null || password.trim().isEmpty()) {
            return Optional.empty();
        }

        for (IAuthenticableRepository repo : repositories) {
            Optional<User> userOpt = repo.searchByUsername(userName.trim());

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (!user.isCurrentStatus()) {
                    continue;
                }
                if (user.getPassword().equals(password)) {
                    currentUser = user;
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Permite a un usuario cerrar sesión.
     */
    @Override
    public void logout() {
        if (currentUser != null) {
            currentUser = null;
        }
    }

    /**
     * Retorna el usuario actualmente autenticado.
     *
     * @return Un {@code Optional} que contiene el usuario actual si hay una
     * sesión activa; de lo contrario, un {@code Optional.empty()}.
     */
    @Override
    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

}
