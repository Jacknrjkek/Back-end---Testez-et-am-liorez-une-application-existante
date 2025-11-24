package com.openclassrooms.etudiant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ----------------------------------------------------------------------------
 * DTO : LoginResponseDTO
 * ----------------------------------------------------------------------------
 * Rôle :
 * - Représente la réponse renvoyée après un login réussi.
 * - Contient uniquement le token JWT généré par JwtService.
 *
 * Le frontend Angular récupère ce token et le stocke en localStorage.
 * ----------------------------------------------------------------------------
 */
@Data
@AllArgsConstructor
public class LoginResponseDTO {

    // Token JWT signé envoyé au frontend
    private String token;
}
