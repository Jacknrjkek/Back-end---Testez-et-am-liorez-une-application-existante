package com.openclassrooms.etudiant.service;

import com.openclassrooms.etudiant.dto.LoginRequestDTO;
import com.openclassrooms.etudiant.dto.LoginResponseDTO;
import com.openclassrooms.etudiant.entities.User;
import com.openclassrooms.etudiant.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * ----------------------------------------------------------------------------
 * SERVICE MÉTIER : GESTION DES UTILISATEURS (Inscription + Login + JWT)
 * ----------------------------------------------------------------------------
 * Rôle :
 * - Vérifier l’unicité d’un utilisateur à l’inscription
 * - Hasher les mots de passe (PasswordEncoder)
 * - Vérifier les identifiants au login
 * - Générer un JWT en cas de succès
 *
 * Avantages :
 * - Pas de logique dans le controller : tout est centralisé ici.
 * - Séparation propre des responsabilités.
 * ----------------------------------------------------------------------------
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    // Injection du Repository, PasswordEncoder et JwtService
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // -------------------------------------------------------------------------
    // INSCRIPTION D’UN NOUVEL UTILISATEUR
    // -------------------------------------------------------------------------
    public void register(User user) {

        Assert.notNull(user, "User must not be null");
        log.info("Registering new user");

        // Vérification unicité du login
        Optional<User> optionalUser = userRepository.findByLogin(user.getLogin());
        if (optionalUser.isPresent()) {
            throw new IllegalArgumentException("User with login " + user.getLogin() + " already exists");
        }

        // Hashage du mot de passe avant stockage
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Sauvegarde en base
        userRepository.save(user);
    }

    // -------------------------------------------------------------------------
    // LOGIN D’UN UTILISATEUR + GÉNÉRATION DU TOKEN JWT
    // -------------------------------------------------------------------------
    public LoginResponseDTO login(LoginRequestDTO request) {

        // Vérifications de base (évite des NullPointerException)
        Assert.notNull(request.getLogin(), "Login must not be null");
        Assert.notNull(request.getPassword(), "Password must not be null");

        // Récupération de l’utilisateur par login
        Optional<User> userOptional = userRepository.findByLogin(request.getLogin());

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        User user = userOptional.get();

        // Vérification du mot de passe hashé
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Construction d’un UserDetails pour la génération du JWT
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getLogin())
                .password(user.getPassword())    // mot de passe déjà hashé
                .authorities("USER")             // rôle par défaut
                .build();

        // Génération du token JWT via JwtService
        String token = jwtService.generateToken(userDetails);

        // Retourne un DTO contenant uniquement le token (idéal pour Angular)
        return new LoginResponseDTO(token);
    }
}
