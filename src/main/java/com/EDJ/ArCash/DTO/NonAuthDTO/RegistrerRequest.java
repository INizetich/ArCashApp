package com.EDJ.ArCash.DTO.NonAuthDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Solicitud para registrar un nuevo usuario")
public class RegistrerRequest {
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String name;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String lastName;

    @Schema(description = "DNI del usuario", example = "12345678")
    private String dni;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@email.com")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;

    @Schema(description = "Alias de la cuenta", example = "juan.perez.01")
    private String alias;
}