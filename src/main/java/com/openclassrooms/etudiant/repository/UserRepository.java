package com.openclassrooms.etudiant.repository;

import com.openclassrooms.etudiant.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ----------------------------------------------------------------------------
 * REPOSITORY : Accès à la base de données pour l'entité User
 * ----------------------------------------------------------------------------
 * Hérite de JpaRepository pour disposer automatiquement de toutes les
 * opérations CRUD nécessaires.
 *
 * Méthode personnalisée :
 * - findByLogin(String login)
 *     -> Spring Data génère automatiquement la requête SQL correspondante.
 *     -> Renvoie un Optional<User> :
 *          - Optional.empty() si aucun utilisateur trouvé
 *          - Optional.of(user) si trouvé
 *
 * Utilisé dans :
 * - l'authentification (login)
 * - la vérification d'unicité du login
 * ----------------------------------------------------------------------------
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Recherche un utilisateur via son login (unique)
    Optional<User> findByLogin(String login);
}
