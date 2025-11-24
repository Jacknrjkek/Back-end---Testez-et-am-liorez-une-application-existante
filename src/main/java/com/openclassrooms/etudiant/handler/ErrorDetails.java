package com.openclassrooms.etudiant.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ----------------------------------------------------------------------------
 * OBJET DE STRUCTURATION DES ERREURS
 * ----------------------------------------------------------------------------
 * Cette classe représente le format standard des réponses d'erreur renvoyées
 * par l'API. Elle permet de structurer les messages envoyés au frontend
 * lorsqu'une exception survient.
 *
 * Champs :
 * - timestamp : date et heure de l'erreur
 * - message   : message technique ou fonctionnel
 * - details   : informations supplémentaires (URL, contexte)
 * ----------------------------------------------------------------------------
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {

    private LocalDateTime timestamp;
    private String message;
    private String details;
}
