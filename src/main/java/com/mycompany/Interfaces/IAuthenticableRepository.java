/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.Interfaces;

import com.mycompany.Models.User;
import java.util.Optional;

/**
 *
 * @author camil
 */
public interface IAuthenticableRepository {
    public Optional<User> searchByUsername(String username);
}
