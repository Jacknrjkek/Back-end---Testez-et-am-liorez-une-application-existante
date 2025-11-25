package com.openclassrooms.etudiant.service;

import com.openclassrooms.etudiant.entities.Student;
import com.openclassrooms.etudiant.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ============================================================================
 * TEST UNITAIRES – StudentService
 * ----------------------------------------------------------------------------
 * Objectif :
 * - Tester la logique métier SANS interagir avec la base de données.
 * - Mock du StudentRepository pour isoler le service.
 *
 * Cas testés :
 * - Récupération de tous les étudiants
 * - Récupération par ID (cas normal)
 * - Création d’un étudiant
 * - Mise à jour
 * - Suppression (cas normal)
 *
 * IMPORTANT :
 * - Comme demandé dans l’exercice, on ne teste PAS les cas d’erreur.
 * ============================================================================
 */
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------------------------------------------------------------
    // GET ALL
    // -------------------------------------------------------------------------
    @Test
    void getAll_shouldReturnList() {
        when(studentRepository.findAll())
                .thenReturn(Arrays.asList(new Student(), new Student()));

        var result = studentService.getAll();

        assertEquals(2, result.size());
    }

    // -------------------------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------------------------
    @Test
    void getById_shouldReturnStudent() {
        Student student = new Student();
        student.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getById(1L);

        assertEquals(1L, result.getId());
    }

    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------
    @Test
    void create_shouldSaveStudent() {
        Student st = new Student();
        when(studentRepository.save(st)).thenReturn(st);

        Student result = studentService.create(st);

        assertEquals(st, result);
    }

    // -------------------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------------------
    @Test
    void update_shouldModifyFields() {
        Student existing = new Student();
        existing.setId(1L);

        Student newData = new Student();
        newData.setFirstName("John");
        newData.setLastName("Doe");
        newData.setEmail("john@test.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.save(existing)).thenReturn(existing);

        Student result = studentService.update(1L, newData);

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john@test.com", result.getEmail());
    }

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------
    @Test
    void delete_shouldCallRepositoryDelete() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        studentService.delete(1L);

        verify(studentRepository).deleteById(1L);
    }
}
