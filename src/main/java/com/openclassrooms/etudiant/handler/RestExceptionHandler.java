package com.openclassrooms.etudiant.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

/**
 * ----------------------------------------------------------------------------
 * GESTION GLOBALE DES EXCEPTIONS (Spring Boot)
 * ----------------------------------------------------------------------------
 * @RestControllerAdvice permet d'intercepter toutes les exceptions lancées par
 * les contrôleurs REST et de renvoyer une réponse propre et standardisée.
 *
 * Ce handler centralise :
 * - Les erreurs de validation ou d'arguments invalides (400)
 * - Les erreurs d'authentification (401)
 * - Les accès interdits (403)
 * - Les erreurs serveur génériques (500)
 *
 * Chaque erreur renvoie un objet ErrorDetails structuré pour le frontend.
 * ----------------------------------------------------------------------------
 */
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    // -------------------------------------------------------------------------
    // 400 BAD REQUEST
    // Erreurs typiques :
    // - IllegalArgumentException
    // - IllegalStateException
    // Utilisé quand l'entrée utilisateur est invalide.
    // -------------------------------------------------------------------------
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException runtimeException, WebRequest request) {

        logError(runtimeException);

        return handleExceptionInternal(
                runtimeException,
                getErrorDetails(runtimeException, request),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    // -------------------------------------------------------------------------
    // 401 UNAUTHORIZED
    // Erreur typique :
    // - BadCredentialsException (login / mot de passe incorrect)
    // -------------------------------------------------------------------------
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {BadCredentialsException.class})
    protected ResponseEntity<Object> handleBadCredentialsException(
            BadCredentialsException badCredentialsException,
            WebRequest request
    ) {
        logError(badCredentialsException);

        return handleExceptionInternal(
                badCredentialsException,
                getErrorDetails(badCredentialsException, request),
                new HttpHeaders(),
                HttpStatus.UNAUTHORIZED,
                request
        );
    }

    // -------------------------------------------------------------------------
    // 403 FORBIDDEN
    // Erreur typique :
    // - AccessDeniedException (utilisateur authentifié mais sans droit)
    // -------------------------------------------------------------------------
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity<Object> handleForbiddenException(
            AccessDeniedException accessDeniedException,
            WebRequest request
    ) {
        logError(accessDeniedException);

        return handleExceptionInternal(
                accessDeniedException,
                getErrorDetails(accessDeniedException, request),
                new HttpHeaders(),
                HttpStatus.FORBIDDEN,
                request
        );
    }

    // -------------------------------------------------------------------------
    // 500 INTERNAL SERVER ERROR
    // Toutes les exceptions non gérées explicitement arrivent ici.
    // -------------------------------------------------------------------------
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleException(RuntimeException runtimeException, WebRequest request) {

        logError(runtimeException);

        // Message générique (évite de divulguer des infos sensibles)
        return handleExceptionInternal(
                runtimeException,
                "Internal Server error",
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    // -------------------------------------------------------------------------
    // LOGGING CENTRALISÉ
    // -------------------------------------------------------------------------
    private void logError(Exception exception) {
        logger.error(exception.getMessage(), exception);
    }

    // -------------------------------------------------------------------------
    // GÉNÉRATION D'UN OBJET ERROR DETAILS STRUCTURÉ
    // -------------------------------------------------------------------------
    private ErrorDetails getErrorDetails(Exception exception, WebRequest request) {
        return new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                request.getDescription(false)
        );
    }
}
