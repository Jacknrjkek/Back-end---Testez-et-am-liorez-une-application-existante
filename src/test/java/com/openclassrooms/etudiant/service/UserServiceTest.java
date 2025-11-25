package com.openclassrooms.etudiant.service;

import com.openclassrooms.etudiant.entities.User;
import com.openclassrooms.etudiant.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ---------------------------------------------------------------------------
 * TESTS UNITAIRES : register() du UserService
 * ---------------------------------------------------------------------------
 * Cas testés :
 *  - user = null → IllegalArgumentException
 *  - login déjà existant → IllegalArgumentException
 *  - inscription valide → user sauvegardé + mot de passe hashé
 * ---------------------------------------------------------------------------
 */
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    // -----------------------------------------------------------------------
    // CAS 1 : user == null
    // -----------------------------------------------------------------------
    @Test
    void register_shouldThrow_whenUserIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.register(null));
    }

    // -----------------------------------------------------------------------
    // CAS 2 : login déjà existant
    // -----------------------------------------------------------------------
    @Test
    void register_shouldThrow_whenLoginAlreadyExists() {

        User existing = new User();
        existing.setLogin("john");

        when(userRepository.findByLogin("john"))
                .thenReturn(Optional.of(existing));

        User newUser = new User();
        newUser.setLogin("john");
        newUser.setPassword("pass");

        assertThrows(IllegalArgumentException.class,
                () -> userService.register(newUser));
    }

    // -----------------------------------------------------------------------
    // CAS 3 : inscription valide
    // -----------------------------------------------------------------------
    @Test
    void register_shouldSaveUser_whenValid() {

        User user = new User();
        user.setLogin("john");
        user.setPassword("pass");

        when(userRepository.findByLogin("john"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("pass"))
                .thenReturn("ENCODED");

        userService.register(user);

        verify(userRepository).save(user);
        assertEquals("ENCODED", user.getPassword());
    }
}
