package com.openclassrooms.etudiant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.etudiant.entities.Student;
import com.openclassrooms.etudiant.repository.StudentRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.springframework.test.web.servlet.MockMvc;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
public class StudentControllerTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private StudentRepository studentRepository;

    @DynamicPropertySource
    static void config(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @AfterEach
    void cleanup() {
        studentRepository.deleteAll();
    }

    @Test
    void getAllStudents_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void createStudent_shouldReturn201() throws Exception {
        Student s = new Student();
        s.setFirstName("John");
        s.setLastName("Doe");
        s.setEmail("john.doe@test.com");

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(s)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void getStudentById_shouldReturnStudent() throws Exception {
        Student s = new Student();
        s.setFirstName("Jane");
        s.setLastName("Doe");
        s.setEmail("jane@test.com");
        Student saved = studentRepository.save(s);

        mockMvc.perform(get("/api/students/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.email").value("jane@test.com"));
    }

    @Test
    void updateStudent_shouldReturnUpdatedStudent() throws Exception {

        Student s = new Student();
        s.setFirstName("Old");
        s.setLastName("Name");
        s.setEmail("old@test.com");
        Student saved = studentRepository.save(s);

        Student updated = new Student();
        updated.setFirstName("New");
        updated.setLastName("Name");
        updated.setEmail("new@test.com");

        mockMvc.perform(put("/api/students/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("New"));
    }

    @Test
    void deleteStudent_shouldReturn204() throws Exception {

        Student s = new Student();
        s.setFirstName("Del");
        s.setLastName("Me");
        s.setEmail("del@test.com");
        Student saved = studentRepository.save(s);

        mockMvc.perform(delete("/api/students/" + saved.getId()))
                .andExpect(status().isNoContent());
    }
}
