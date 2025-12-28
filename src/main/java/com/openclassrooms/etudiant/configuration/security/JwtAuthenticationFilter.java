package com.openclassrooms.etudiant.configuration.security;

import com.openclassrooms.etudiant.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * ----------------------------------------------------------------------------
 * FILTRE D’AUTHENTIFICATION JWT
 * ----------------------------------------------------------------------------
 * Rôle :
 * - Intercepter chaque requête HTTP (OncePerRequestFilter = 1 seule exécution)
 * - Vérifier la présence d’un token JWT dans l’en-tête Authorization
 * - Extraire le username du token
 * - Charger l'utilisateur depuis la base (UserDetails)
 * - Valider le token
 * - Si tout est correct : créer une authentification Spring Security
 *   et l'insérer dans le SecurityContext
 *
 * Importance :
 * - C’est ce filtre qui permet d’accéder aux routes sécurisées
 *   sans faire de login manuel sur chaque requête.
 * ----------------------------------------------------------------------------
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // ---------------------------------------------------------------------
        // 1. RÉCUPERATION DE L’HEADER "Authorization"
        // ---------------------------------------------------------------------
        final String authHeader = request.getHeader("Authorization");

        // Si pas d'en-tête ou format incorrect → on passe au filtre suivant
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ---------------------------------------------------------------------
        // 2. EXTRACTION DU TOKEN JWT
        // ---------------------------------------------------------------------
        final String jwt = authHeader.substring(7); // enlève "Bearer "
        final String username = jwtService.extractUsername(jwt);

        // ---------------------------------------------------------------------
        // 3. VALIDATION : SEULEMENT SI AUCUNE AUTHENTIFICATION N'EST DÉJÀ PRÉSENTE
        // ---------------------------------------------------------------------
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // On charge l'utilisateur depuis la base
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Vérification du token (signature + expiration + username)
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // -----------------------------------------------------------------
                // 4. CONSTRUCTION D’UN OBJET AUTHENTIFIÉ POUR SPRING SECURITY
                // -----------------------------------------------------------------
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,                        // pas de mot de passe
                                userDetails.getAuthorities() // rôle(s)
                        );

                // Ajoute les détails de la requête (adresse IP, session…)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // -----------------------------------------------------------------
                // 5. ENREGISTREMENT DE L’UTILISATEUR COMME AUTHENTIFIÉ
                // -----------------------------------------------------------------
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // ---------------------------------------------------------------------
        // 6. CONTINUE LE CHAÎNAGE DES FILTRES
        // ---------------------------------------------------------------------
        filterChain.doFilter(request, response);
    }
}
