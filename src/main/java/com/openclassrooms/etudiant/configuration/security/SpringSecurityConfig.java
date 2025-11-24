package com.openclassrooms.etudiant.configuration.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * ----------------------------------------------------------------------------
 * CONFIGURATION SPRING SECURITY (Spring Security 6 + JWT)
 * ----------------------------------------------------------------------------
 * Rôle :
 * - Configurer les règles d’accès HTTP
 * - Intégrer le filtre JWT (JwtAuthenticationFilter)
 * - Désactiver la session : API 100% stateless (JWT)
 * - Définir les endpoints publics et sécurisés
 * - Définir l’AuthenticationProvider basé sur UserDetailsService + BCrypt
 *
 * Architecture :
 * → Le client s’authentifie via /api/login → reçoit un JWT
 * → Chaque requête suivante contient Authorization: Bearer <token>
 * → Le filtre JWT valide le token et insère l’utilisateur dans le SecurityContext
 * ----------------------------------------------------------------------------
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    // Injection du filtre JWT et du service UserDetails personnalisé
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailService userDetailService;

    /**
     * ------------------------------------------------------------------------
     * CHAÎNE DE FILTRES DE SÉCURITÉ
     * Définition complète des règles HTTP et des filtres utilisés par Spring.
     * ------------------------------------------------------------------------
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ---------------------------------------------------------------------
                // 1. Désactivation CSRF (API REST stateless → pas de formulaire HTML)
                // ---------------------------------------------------------------------
                .csrf(AbstractHttpConfigurer::disable)

                // ---------------------------------------------------------------------
                // 2. Désactivation du CORS (géré ailleurs si besoin)
                // ---------------------------------------------------------------------
                .cors(AbstractHttpConfigurer::disable)

                // ---------------------------------------------------------------------
                // 3. Mode API : aucune session, entièrement stateless avec JWT
                // ---------------------------------------------------------------------
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // ---------------------------------------------------------------------
                // 4. ROUTES PUBLIQUES ET PRIVÉES
                //    - /api/login et /api/register sont accessibles sans token
                //    - Toutes les autres routes nécessitent un JWT valide
                // ---------------------------------------------------------------------
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login", "/api/register").permitAll()
                        .anyRequest().authenticated()
                )

                // ---------------------------------------------------------------------
                // 5. GESTION DES ERREURS D’AUTHENTIFICATION
                //    Si un utilisateur non authentifié tente d’accéder à une route
                //    protégée → renvoie HTTP 401
                // ---------------------------------------------------------------------
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(
                                (req, res, ex) ->
                                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage())
                        )
                )

                // ---------------------------------------------------------------------
                // 6. AUTHENTICATION PROVIDER
                //    (basé sur CustomUserDetailService + BCrypt)
                // ---------------------------------------------------------------------
                .authenticationProvider(authProvider())

                // ---------------------------------------------------------------------
                // 7. AJOUT DU FILTRE JWT AVANT UsernamePasswordAuthenticationFilter
                //    → Permet d’intercepter toutes les requêtes HTTP
                // ---------------------------------------------------------------------
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * ------------------------------------------------------------------------
     * AuthenticationProvider :
     * - Utilise le UserDetailsService personnalisé pour charger l'utilisateur
     * - Utilise BCrypt pour vérifier le mot de passe
     * ------------------------------------------------------------------------
     */
    @Bean
    public AuthenticationProvider authProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailService);  // Charge le user depuis la DB
        provider.setPasswordEncoder(passwordEncoder());     // Vérifie le password hashé

        return provider;
    }

    /**
     * ------------------------------------------------------------------------
     * PasswordEncoder :
     * - BCrypt est le standard recommandé pour hasher les mots de passe
     * - Résistant au rainbow table et au brute-force
     * ------------------------------------------------------------------------
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
