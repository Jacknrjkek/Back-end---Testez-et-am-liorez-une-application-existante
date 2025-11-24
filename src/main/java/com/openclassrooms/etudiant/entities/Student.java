package com.openclassrooms.etudiant.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "student")
@Getter @Setter
public class Student {

    // -------------------------------------------------------------------------
    // IDENTIFIANT (PRIMARY KEY)
    // - @Id marque la clé primaire
    // - @GeneratedValue : génération automatique (auto-incrément)
    // -------------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -------------------------------------------------------------------------
    // PRÉNOM DE L'ÉTUDIANT
    // - @Column(nullable = false) : champ obligatoire en base
    // - name = "first_name" : nom de colonne en base
    // -------------------------------------------------------------------------
    @Column(name = "first_name", nullable = false)
    private String firstName;

    // -------------------------------------------------------------------------
    // NOM DE L'ÉTUDIANT
    // -------------------------------------------------------------------------
    @Column(name = "last_name", nullable = false)
    private String lastName;

    // -------------------------------------------------------------------------
    // EMAIL DE L'ÉTUDIANT
    // - Champ obligatoire
    // - Pas de contrainte unique ici, mais pourrait être ajouté si besoin
    // -------------------------------------------------------------------------
    @Column(nullable = false)
    private String email;
}
