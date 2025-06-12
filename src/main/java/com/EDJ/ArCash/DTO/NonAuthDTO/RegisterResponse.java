package com.EDJ.ArCash.DTO.NonAuthDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Respuesta al registrar un nuevo usuario")
public class RegisterResponse {
    @Schema(description = "Mensaje de la operación de registro", example = "Usuario registrado correctamente")
    private String mensaje;

    @Schema(description = "Indica si el registro fue exitoso", example = "true")
    private boolean success;

    public RegisterResponse(boolean success, String mensaje) {
        this.success = success;
        this.mensaje = mensaje;
    }
}