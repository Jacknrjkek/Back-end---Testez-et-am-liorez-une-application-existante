package com.openclassrooms.etudiant.mapper;

import com.openclassrooms.etudiant.dto.RegisterDTO;
import com.openclassrooms.etudiant.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * ----------------------------------------------------------------------------
 * MAPPER MAPSTRUCT : RegisterDTO -> User (ENTITÉ)
 * ----------------------------------------------------------------------------
 * MapStruct est un générateur de code permettant de convertir automatiquement
 * des objets (DTO <-> Entity) sans écrire manuellement les setters.
 *
 * Avantages :
 * - Code plus propre
 * - Conversion fiable et compilée (donc sans réflexion)
 * - Meilleure maintenabilité
 *
 * Options configurées :
 * - componentModel = "spring" : MapStruct génère un bean Spring injectable
 * - unmappedTargetPolicy = ERROR :
 *      oblige à mapper explicitement chaque champ pour éviter les oublis
 * ----------------------------------------------------------------------------
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserDtoMapper {

    /**
     * ------------------------------------------------------------------------
     * MÉTHODE : toEntity
     * Convertit un RegisterDTO en entité User.
     *
     * Champs ignorés :
     * - id : généré automatiquement par la base de données
     * - created_at : géré automatiquement par Hibernate (@CreationTimestamp)
     * - updated_at : géré automatiquement (@UpdateTimestamp)
     *
     * Les autres champs (firstName, lastName, login, password)
     * sont mappés automatiquement par MapStruct car ils ont le même nom.
     * ------------------------------------------------------------------------
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    User toEntity(RegisterDTO registerDTO);
}
