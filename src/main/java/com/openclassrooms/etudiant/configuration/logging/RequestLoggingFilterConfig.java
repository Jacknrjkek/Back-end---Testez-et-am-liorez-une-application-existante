package com.openclassrooms.etudiant.configuration.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * ----------------------------------------------------------------------------
 * CONFIGURATION : Logging des requêtes HTTP
 * ----------------------------------------------------------------------------
 * Rôle :
 * - Activer un filtre Spring permettant de logguer automatiquement les requêtes
 *   HTTP entrantes.
 * - Très utile pour le debugging ou l’observation du comportement du frontend.
 *
 * Fonctionnement :
 * - CommonsRequestLoggingFilter intercepte chaque requête
 * - Peut enregistrer : query string, payload, headers, etc.
 *
 * Configuration :
 * - includeQueryString(true)  → log l’URL et ses paramètres
 * - includePayload(true)      → log le corps de la requête (ex: JSON)
 * - maxPayloadLength(10000)   → limite la taille maximale à 10 KB
 * - includeHeaders(false)     → n’affiche pas les headers pour éviter
 *                                l’exposition accidentelle de tokens
 * - afterMessagePrefix(...)   → préfixe lisible dans les logs
 * ----------------------------------------------------------------------------
 */
@Configuration
public class RequestLoggingFilterConfig {

    @Bean
    public CommonsRequestLoggingFilter commonsRequestLoggingFilter() {

        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();

        // Log la query string de la requête (ex: ?page=1&size=10)
        filter.setIncludeQueryString(true);

        // Log le contenu du body (ex: JSON envoyé par Angular)
        filter.setIncludePayload(true);

        // Limite la taille du payload loggé pour éviter de polluer les logs
        filter.setMaxPayloadLength(10000);

        // Désactivé pour éviter de logguer des données sensibles (Authorization, Tokens…)
        filter.setIncludeHeaders(false);

        // Préfixe des logs, plus lisible dans la console
        filter.setAfterMessagePrefix("REQUEST DATA: ");

        return filter;
    }
}
