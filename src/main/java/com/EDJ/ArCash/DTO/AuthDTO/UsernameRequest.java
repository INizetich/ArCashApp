package com.EDJ.ArCash.DTO.AuthDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Solicitud para cambiar el nombre de usuario")
public class UsernameRequest {
    @Schema(description = "Nuevo nombre de usuario", example = "nuevo.usuario")
    private String newUsername;
}