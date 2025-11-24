package com.openclassrooms.etudiant.controller;

import com.openclassrooms.etudiant.dto.LoginRequestDTO;
import com.openclassrooms.etudiant.dto.LoginResponseDTO;
import com.openclassrooms.etudiant.dto.RegisterDTO;
import com.openclassrooms.etudiant.mapper.UserDtoMapper;
import com.openclassrooms.etudiant.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    // -------------------------------------------------------------------------
    // INJECTIONS DE DÉPENDANCES
    // - UserService : contient la logique métier (inscription, login)
    // - UserDtoMapper : convertit les DTO en entités (et inversement)
    // -------------------------------------------------------------------------
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    // -------------------------------------------------------------------------
    // ROUTE : POST /api/register
    // Rôle :
    // - Recevoir les données du formulaire d'inscription
    // - Convertir le DTO en entité User
    // - Appeler le service pour enregistrer l’utilisateur
    //
    // Retour :
    // - 201 CREATED en cas de succès
    // -------------------------------------------------------------------------
    @PostMapping("/api/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {

        // Conversion DTO -> Entité
        userService.register(userDtoMapper.toEntity(registerDTO));

        // Aucun contenu, seulement le statut HTTP
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // -------------------------------------------------------------------------
    // ROUTE : POST /api/login
    // Rôle :
    // - Vérifier les identifiants de l'utilisateur
    // - Retourner un LoginResponseDTO contenant le token JWT
    // -------------------------------------------------------------------------
    @PostMapping("/api/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {

        // Le service renvoie un DTO déjà formaté pour Angular (token + user info)
        LoginResponseDTO responseDTO = userService.login(loginRequestDTO);

        return ResponseEntity.ok(responseDTO);
    }
}
