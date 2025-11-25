package com.openclassrooms.etudiant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;
    private org.springframework.security.core.userdetails.UserDetails userDetails;

    @BeforeEach
    void setup() {
        jwtService = new JwtService();

        userDetails = User.withUsername("john")
                .password("password")
                .authorities("USER")
                .build();
    }

    @Test
    void testGenerateToken_shouldReturnNonNullToken() {
        // WHEN
        String token = jwtService.generateToken(userDetails);

        // THEN
        assertNotNull(token);
    }

    @Test
    void testExtractUsername_shouldReturnCorrectUsername() {
        // GIVEN
        String token = jwtService.generateToken(userDetails);

        // WHEN
        String username = jwtService.extractUsername(token);

        // THEN
        assertEquals("john", username);
    }

    @Test
    void testIsTokenValid_shouldReturnTrueForValidToken() {
        // GIVEN
        String token = jwtService.generateToken(userDetails);

        // WHEN & THEN
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }
}
