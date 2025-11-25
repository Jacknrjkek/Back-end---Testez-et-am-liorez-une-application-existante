package com.openclassrooms.etudiant.service;

import com.openclassrooms.etudiant.dto.LoginRequestDTO;
import com.openclassrooms.etudiant.dto.LoginResponseDTO;
import com.openclassrooms.etudiant.entities.User;
import com.openclassrooms.etudiant.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ---------------------------------------------------------------------------
 * TESTS UNITAIRES : login() du UserService
 * ---------------------------------------------------------------------------
 * Scénarios :
 *  1. Login inexistant → IllegalArgumentException
 *  2. Mot de passe incorrect → IllegalArgumentException
 *  3. Identifiants valides → renvoie LoginResponseDTO contenant un JWT
 * ---------------------------------------------------------------------------
 */
public class UserServiceLoginTest {

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
    // CAS 1 : L’utilisateur n’existe pas → exception
    // -----------------------------------------------------------------------
    @Test
    void login_shouldThrow_whenUserNotFound() {

        LoginRequestDTO req = new LoginRequestDTO();
        req.setLogin("ghost");
        req.setPassword("123");

        when(userRepository.findByLogin("ghost"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> userService.login(req));
    }

    // -----------------------------------------------------------------------
    // CAS 2 : Mot de passe incorrect
    // -----------------------------------------------------------------------
    @Test
    void login_shouldThrow_whenPasswordIncorrect() {

        User user = new User();
        user.setLogin("john");
        user.setPassword("HASHED");

        LoginRequestDTO req = new LoginRequestDTO();
        req.setLogin("john");
        req.setPassword("wrong");

        when(userRepository.findByLogin("john"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong", "HASHED"))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> userService.login(req));
    }

    // -----------------------------------------------------------------------
    // CAS 3 : Identifiants valides → retourne un JWT
    // -----------------------------------------------------------------------
    @Test
    void login_shouldReturnToken_whenCredentialsValid() {

        User user = new User();
        user.setLogin("john");
        user.setPassword("HASHED");

        LoginRequestDTO req = new LoginRequestDTO();
        req.setLogin("john");
        req.setPassword("correct");

        when(userRepository.findByLogin("john"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("correct", "HASHED"))
                .thenReturn(true);

        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn("FAKE_JWT");

        LoginResponseDTO res = userService.login(req);

        assertNotNull(res);
        assertEquals("FAKE_JWT", res.getToken());
    }
}
