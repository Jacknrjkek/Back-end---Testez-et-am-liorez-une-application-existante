package com.openclassrooms.etudiant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ----------------------------------------------------------------------------
 * POINT D’ENTRÉE DU BACKEND SPRING BOOT
 * ----------------------------------------------------------------------------
 * @SpringBootApplication regroupe :
 *  - @Configuration : indique que cette classe contient des configurations Spring
 *  - @EnableAutoConfiguration : active la configuration automatique de Spring Boot
 *  - @ComponentScan : permet de scanner automatiquement les composants du projet
 *
 * Cette classe contient simplement la méthode main() qui lance l’application
 * via SpringApplication.run().
 * ----------------------------------------------------------------------------
 */
@SpringBootApplication
public class EtudiantBackendApplication {

    public static void main(String[] args) {
        // Lance l'application Spring Boot (serveur embarqué Tomcat)
        SpringApplication.run(EtudiantBackendApplication.class, args);
    }
}
