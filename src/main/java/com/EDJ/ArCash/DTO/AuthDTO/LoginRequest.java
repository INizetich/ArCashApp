
package com.EDJ.ArCash.DTO.AuthDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos de la solicitud de inicio de sesión")
public class LoginRequest {
    @Schema(description = "Nombre de usuario o email", example = "usuario123")
    private String username;

    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;

    @Schema(description = "Campo de habilitación (opcional)", example = "true")
    private String enable;
}