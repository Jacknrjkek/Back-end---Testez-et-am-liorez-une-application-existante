package com.openclassrooms.etudiant.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ----------------------------------------------------------------------------
 * DTO : LoginRequestDTO
 * ----------------------------------------------------------------------------
 * Rôle :
 * - Représente les données envoyées par le frontend lors de la connexion.
 * - Utilisé dans UserController pour valider et traiter la demande de login.
 *
 * Contraintes :
 * - @NotBlank : empêche les champs vides ou nulls
 * ----------------------------------------------------------------------------
 */
@Data
public class LoginRequestDTO {

    @NotBlank(message = "Login is required")
    private String login;

    @NotBlank(message = "Password is required")
    private String password;
}
