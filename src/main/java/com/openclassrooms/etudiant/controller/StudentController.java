package com.openclassrooms.etudiant.controller;

import com.openclassrooms.etudiant.entities.Student;
import com.openclassrooms.etudiant.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:4200") // Autorise Angular à appeler l’API
public class StudentController {

    // -------------------------------------------------------------------------
    // INJECTION DU SERVICE
    // StudentService contient la logique métier liée aux étudiants
    // -------------------------------------------------------------------------
    private final StudentService studentService;

    // -------------------------------------------------------------------------
    // GET /api/students
    // Récupère la liste complète des étudiants
    // -------------------------------------------------------------------------
    @GetMapping
    public List<Student> getAll() {
        return studentService.getAll();
    }

    // -------------------------------------------------------------------------
    // GET /api/students/{id}
    // Retourne un étudiant spécifique selon son ID
    // -------------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getById(id));
    }

    // -------------------------------------------------------------------------
    // POST /api/students
    // Création d'un nouvel étudiant
    // Retourne 201 + l'étudiant créé
    // -------------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {
        Student created = studentService.create(student);
        return ResponseEntity.status(201).body(created);
    }

    // -------------------------------------------------------------------------
    // PUT /api/students/{id}
    // Mise à jour d'un étudiant existant
    // -------------------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student s) {
        return ResponseEntity.ok(studentService.update(id, s));
    }

    // -------------------------------------------------------------------------
    // DELETE /api/students/{id}
    // Suppression d'un étudiant
    // Retourne HTTP 204 No Content
    // -------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
