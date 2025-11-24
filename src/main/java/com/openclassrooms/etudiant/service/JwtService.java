package com.openclassrooms.etudiant.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * ----------------------------------------------------------------------------
 * SERVICE : Gestion des Tokens JWT
 * ----------------------------------------------------------------------------
 * Rôle :
 * - Générer un token JWT signé
 * - Extraire des informations du token (username, expiration…)
 * - Vérifier la validité du token
 *
 * Technologies :
 * - Bibliothèque jjwt (io.jsonwebtoken)
 * - Signature HS256 avec une clé secrète
 *
 * Ce service est utilisé dans :
 * - UserService (génération du token à la connexion)
 * - JwtAuthFilter (validation du token sur chaque requête sécurisée)
 * ----------------------------------------------------------------------------
 */
@Service
public class JwtService {

    /**
     * ------------------------------------------------------------------------
     * CLÉ SECRÈTE DE SIGNATURE
     * ------------------------------------------------------------------------
     * - La clé doit faire au minimum 32 caractères pour l’algorithme HS256.
     * - Ici elle est définie en dur, mais en production on utilise
     *   une variable d’environnement ou Vault.
     */
    private final Key key =
            Keys.hmacShaKeyFor("ma-cle-super-secrete-qui-fait-au-moins-32-caracteres".getBytes());

    /**
     * Durée de validité du token : 1 heure (en millisecondes)
     */
    private final long expirationMs = 3600000;

    // -------------------------------------------------------------------------
    // EXTRACTION DU USERNAME (subject) À PARTIR DU TOKEN
    // -------------------------------------------------------------------------
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // -------------------------------------------------------------------------
    // VALIDATION DU TOKEN
    // - Vérifie que le username du token correspond à l'utilisateur
    // - Vérifie aussi que le token n'est pas expiré
    // -------------------------------------------------------------------------
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Vérifie si la date d’expiration est passée
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Récupère la date d’expiration
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * ------------------------------------------------------------------------
     * MÉTHODE GÉNÉRIQUE D’EXTRACTION DE CLAIM
     * ------------------------------------------------------------------------
     * - Les Claims sont les "données embarquées" dans le token JWT
     * - Le resolver permet de choisir quel champ lire (subject, expiration…)
     * ------------------------------------------------------------------------
     */
    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)          // clé de vérification
                .build()
                .parseClaimsJws(token)       // vérifie la signature + parse
                .getBody();                  // extrait les claims

        return resolver.apply(claims);
    }

    // -------------------------------------------------------------------------
    // GÉNÉRATION D’UN TOKEN JWT
    // -------------------------------------------------------------------------
    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .setSubject(userDetails.getUsername())              // identifiant principal
                .setIssuedAt(new Date())                            // date de création
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs)) // expiration
                .signWith(key, SignatureAlgorithm.HS256)             // signature
                .compact();                                          // génération finale
    }
}
