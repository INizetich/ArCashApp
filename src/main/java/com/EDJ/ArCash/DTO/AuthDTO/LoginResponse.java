
        package com.EDJ.ArCash.DTO.AuthDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Respuesta al intentar iniciar sesión")
public class LoginResponse {
    @Schema(description = "Indica si el inicio de sesión fue exitoso", example = "true")
    private boolean success;

    @Schema(description = "Mensaje de respuesta", example = "Inicio de sesión exitoso")
    private String message;

    @Schema(description = "Token de acceso JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String AccessToken;

    @Schema(description = "Token de refresco JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String RefreshToken;

    @Schema(description = "ID de la cuenta asociada", example = "123")
    private Long accountId;

    @Schema (description = "Rol de la cuenta asociada", example = "ADMIN")
    private String role;


}