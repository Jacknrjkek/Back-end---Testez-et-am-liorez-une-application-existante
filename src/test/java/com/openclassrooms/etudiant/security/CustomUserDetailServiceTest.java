package com.openclassrooms.etudiant.configuration.security;

import com.openclassrooms.etudiant.entities.User;
import com.openclassrooms.etudiant.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ============================================================================
 * TEST UNITAIRE – CustomUserDetailService
 * ----------------------------------------------------------------------------
 * Objectif :
 * - Vérifier que loadUserByUsername() renvoie bien un UserDetails lorsque
 *   l'utilisateur existe en base.
 *
 * Remarques :
 * - Conformément aux consignes de l’exercice : on NE teste PAS les cas d’erreurs.
 *   (donc pas de test sur UsernameNotFoundException)
 * ============================================================================
 */
class CustomUserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------------------------------------------------------------
    // Cas nominal : l'utilisateur existe → un UserDetails correctement construit
    // -------------------------------------------------------------------------
    @Test
    void loadUserByUsername_shouldReturnUserDetails() {

        // GIVEN – un utilisateur stocké en base
        User u = new User();
        u.setLogin("john");
        u.setPassword("hashed-password");

        when(userRepository.findByLogin("john"))
                .thenReturn(Optional.of(u));

        // WHEN – on charge l’utilisateur
        UserDetails details = customUserDetailService.loadUserByUsername("john");

        // THEN – les données doivent correspondre
        assertEquals("john", details.getUsername());
        assertEquals("hashed-password", details.getPassword());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("USER"))
        );
    }
}
