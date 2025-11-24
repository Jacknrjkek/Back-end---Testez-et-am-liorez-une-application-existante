package com.openclassrooms.etudiant.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ----------------------------------------------------------------------------
 * DTO : RegisterDTO
 * ----------------------------------------------------------------------------
 * Rôle :
 * - Représente les données nécessaires à la création d’un nouvel utilisateur.
 * - Utilisé dans UserController lors d’un appel POST /api/register.
 *
 * Contraintes :
 * - @NotBlank : tous les champs sont obligatoires
 *
 * Note :
 * - Ce DTO est converti en entité User via UserDtoMapper (MapStruct).
 * ----------------------------------------------------------------------------
 */
@Data
public class RegisterDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
