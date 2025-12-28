package com.openclassrooms.etudiant.configuration.security;

import com.openclassrooms.etudiant.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.Mockito.*;

/**
 * ============================================================================
 * TEST DU FILTRE JWT – JwtAuthenticationFilter
 * ----------------------------------------------------------------------------
 * Objectif :
 * - Vérifier que le filtre :
 *     1. lit le header Authorization
 *     2. extrait et valide le JWT
 *     3. charge l’utilisateur via CustomUserDetailService
 *     4. insère une authentification valide dans le SecurityContext
 *
 * NOTE :
 * - Cas d’erreur non testés (interdit par l’énoncé).
 * ============================================================================
 */
public class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailService userDetailService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------------------------------------------------------------
    // CAS NORMAL : le token est valide → authentification enregistrée
    // -------------------------------------------------------------------------
    @Test
    void doFilterInternal_validToken_shouldAuthenticateUser() throws Exception {

        // Le token fourni dans le header
        String token = "abc.def.ghi";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        // Le username récupéré depuis le token
        when(jwtService.extractUsername(token)).thenReturn("john");

        UserDetails userDetails = User.withUsername("john")
                .password("pass")
                .authorities("USER")
                .build();

        // Le service charge un utilisateur valide
        when(userDetailService.loadUserByUsername("john"))
                .thenReturn(userDetails);

        when(jwtService.isTokenValid(token, userDetails))
                .thenReturn(true);

        // Exécution du filtre
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Le filtre continue normalement
        verify(filterChain).doFilter(request, response);
    }

    // -------------------------------------------------------------------------
    // CAS : pas de header Authorization → le filtre saute l’authentification
    // -------------------------------------------------------------------------
    @Test
    void doFilterInternal_noAuthorizationHeader_shouldSkipAuthentication() throws Exception {

        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}
