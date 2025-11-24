package com.openclassrooms.etudiant.service;

import com.openclassrooms.etudiant.entities.Student;
import com.openclassrooms.etudiant.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ----------------------------------------------------------------------------
 * SERVICE MÉTIER : StudentService
 * ----------------------------------------------------------------------------
 * Rôle :
 * - Contient toute la logique métier liée aux étudiants
 * - Sert d’intermédiaire entre les contrôleurs REST et la couche Repository
 *
 * Bonnes pratiques respectées :
 * - Injection par constructeur (via @RequiredArgsConstructor)
 * - Gestion des erreurs via EntityNotFoundException
 * - Pas de logique dans le controller : tout est centralisé ici.
 * ----------------------------------------------------------------------------
 */
@Service
@RequiredArgsConstructor
public class StudentService {

    // Repository injecté automatiquement grâce à Lombok + Spring
    private final StudentRepository studentRepository;

    // -------------------------------------------------------------------------
    // RÉCUPÉRATION DE TOUS LES ÉTUDIANTS
    // -------------------------------------------------------------------------
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    // -------------------------------------------------------------------------
    // RÉCUPÉRATION D’UN ÉTUDIANT PAR ID
    // - Retourne l’étudiant s’il existe
    // - Sinon : lance une EntityNotFoundException (gérée par le handler global)
    // -------------------------------------------------------------------------
    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student " + id + " not found"));
    }

    // -------------------------------------------------------------------------
    // CRÉATION D’UN ÉTUDIANT
    // 'save()' sert à la fois pour la création et la mise à jour
    // -------------------------------------------------------------------------
    public Student create(Student student) {
        return studentRepository.save(student);
    }

    // -------------------------------------------------------------------------
    // MISE À JOUR D’UN ÉTUDIANT
    // Étapes :
    // 1. Vérifier que l’étudiant existe
    // 2. Mettre à jour les champs modifiés
    // 3. Sauvegarder les modifications
    // -------------------------------------------------------------------------
    public Student update(Long id, Student newData) {

        // Vérification et récupération de l'entité existante
        Student st = getById(id);

        // Mise à jour des champs
        st.setFirstName(newData.getFirstName());
        st.setLastName(newData.getLastName());
        st.setEmail(newData.getEmail());

        // Persistance en base
        return studentRepository.save(st);
    }

    // -------------------------------------------------------------------------
    // SUPPRESSION D’UN ÉTUDIANT
    // - Vérifie d'abord l'existence
    // - Supprime ensuite par ID
    // -------------------------------------------------------------------------
    public void delete(Long id) {

        // Vérification
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Student " + id + " not found");
        }

        // Suppression
        studentRepository.deleteById(id);
    }
}
