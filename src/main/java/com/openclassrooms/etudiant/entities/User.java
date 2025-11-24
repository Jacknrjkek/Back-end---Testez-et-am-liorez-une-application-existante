package com.openclassrooms.etudiant.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User {

    // -------------------------------------------------------------------------
    // IDENTIFIANT
    // - AUTO_INCREMENT
    // - Colonne "id" en base
    // -------------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // -------------------------------------------------------------------------
    // PRÉNOM DE L'UTILISATEUR
    // - @NotBlank : validation côté backend (pas vide)
    // - Nullable = false : contrainte SQL
    // -------------------------------------------------------------------------
    @NotBlank
    @Column(name = "firstName", nullable = false)
    private String firstName;

    // -------------------------------------------------------------------------
    // NOM DE L'UTILISATEUR
    // -------------------------------------------------------------------------
    @NotBlank
    @Column(name = "lastName", nullable = false)
    private String lastName;

    // -------------------------------------------------------------------------
    // LOGIN
    // - Unique en base
    // - Obligatoire
    // -------------------------------------------------------------------------
    @NotBlank
    @Column(name = "login", unique = true, nullable = false)
    private String login;

    // -------------------------------------------------------------------------
    // MOT DE PASSE
    // - Stocké dans la base sous forme HASHÉE (via le service)
    // -------------------------------------------------------------------------
    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    // -------------------------------------------------------------------------
    // TIMESTAMP DE CRÉATION
    // - @CreationTimestamp : géré automatiquement par Hibernate
    // -------------------------------------------------------------------------
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime created_at;

    // -------------------------------------------------------------------------
    // TIMESTAMP DE MISE À JOUR
    // - @UpdateTimestamp : mis à jour automatiquement
    // -------------------------------------------------------------------------
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updated_at;
}
