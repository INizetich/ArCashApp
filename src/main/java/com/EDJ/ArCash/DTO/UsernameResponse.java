package com.EDJ.ArCash.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Respuesta al cambiar el nombre de usuario")
public class UsernameResponse {
    @Schema(description = "Indica si el cambio fue exitoso", example = "true")
    private boolean success;

    @Schema(description = "Mensaje descriptivo del resultado", example = "Nombre de usuario actualizado correctamente")
    private String message;
}