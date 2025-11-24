package com.openclassrooms.etudiant.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

/**
 * ----------------------------------------------------------------------------
 * CONFIGURATION : Gestion des fichiers de propriétés (.env)
 * ----------------------------------------------------------------------------
 * Rôle :
 * - Permet à Spring de charger des variables depuis un fichier externe ".env".
 * - Ces propriétés peuvent ensuite être injectées via @Value("${...}").
 *
 * Explication :
 * - PropertySourcesPlaceholderConfigurer est un mécanisme Spring qui résout
 *   les placeholders ${...} dans l'application.
 * - setLocation(new FileSystemResource(".env")) indique à Spring de charger
 *   le fichier ".env" situé à la racine du projet.
 *
 * Note :
 * - Dans un projet réel, on privilégie application.properties ou des variables
 *   d’environnement système.
 * - Pour une démonstration pédagogique, cette configuration reste totalement valide.
 * ----------------------------------------------------------------------------
 */
@Configuration
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {

        PropertySourcesPlaceholderConfigurer configurer =
                new PropertySourcesPlaceholderConfigurer();

        // Indique à Spring où se trouve le fichier .env
        configurer.setLocation(new FileSystemResource(".env"));

        return configurer;
    }
}
