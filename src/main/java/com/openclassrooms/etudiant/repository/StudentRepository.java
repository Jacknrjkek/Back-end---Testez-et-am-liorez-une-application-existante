package com.openclassrooms.etudiant.repository;

import com.openclassrooms.etudiant.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ----------------------------------------------------------------------------
 * REPOSITORY : Accès à la base de données pour l'entité Student
 * ----------------------------------------------------------------------------
 * JpaRepository fournit automatiquement :
 * - findAll()
 * - findById()
 * - save()
 * - deleteById()
 * - count()
 * - existsById()
 *
 * Il n'est pas nécessaire d'écrire du code SQL ou des requêtes JPQL.
 * Spring Data génère tout automatiquement.
 *
 * StudentRepository gère les opérations CRUD pour l'entité Student.
 * ----------------------------------------------------------------------------
 */
public interface StudentRepository extends JpaRepository<Student, Long> {
}
