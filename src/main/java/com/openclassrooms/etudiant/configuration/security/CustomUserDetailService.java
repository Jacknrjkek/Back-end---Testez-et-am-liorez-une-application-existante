package com.openclassrooms.etudiant.configuration.security;

import com.openclassrooms.etudiant.entities.User;
import com.openclassrooms.etudiant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * ----------------------------------------------------------------------------
 * SERVICE : CustomUserDetailService
 * ----------------------------------------------------------------------------
 * Rôle :
 * - Implémentation de UserDetailsService, interface obligatoire pour Spring Security.
 * - Chargé de récupérer un utilisateur depuis la base de données lors de
 *   l’authentification (ex. validation du JWT ou login).
 *
 * Fonctionnement :
 * - Lorsqu'un token JWT est reçu, ou lorsqu'un login/mot de passe est vérifié,
 *   Spring Security invoque loadUserByUsername().
 *
 * - Si l'utilisateur existe : construction d'un objet UserDetails compatible
 *   avec Spring Security.
 *
 * - Si l'utilisateur n'existe pas : exception UsernameNotFoundException.
 *
 * Importance :
 * - C’est un élément clé du pipeline JWT + Spring Security.
 * ----------------------------------------------------------------------------
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * ------------------------------------------------------------------------
     * CHARGEMENT D’UN UTILISATEUR PAR LE LOGIN (username)
     * ------------------------------------------------------------------------
     * Appelé automatiquement par :
     * - le filtre JWT (JwtAuthenticationFilter)
     * - AuthenticationManager lors du login
     *
     * Étapes :
     * 1. Recherche de l’utilisateur dans la base via UserRepository
     * 2. Si absent → exception (HTTP 401)
     * 3. Si présent → conversion en objet UserDetails Spring Security
     * ------------------------------------------------------------------------
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        // Recherche de l'utilisateur en base via le login
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with login: " + login));

        // Construction d'un UserDetails Spring Security
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getLogin())     // identifiant unique
                .password(user.getPassword())      // mot de passe déjà hashé
                .authorities("USER")               // rôle simple pour cette application
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
