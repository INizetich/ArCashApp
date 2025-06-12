
package com.EDJ.ArCash.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Respuesta de operación sobre la cuenta")
public class AccountResponse {
    @Schema(description = "Indica si la operación fue exitosa", example = "true")
    private boolean success;

    @Schema(description = "Mensaje de la operación", example = "Saldo agregado correctamente")
    private String message;

    @Schema(description = "Nuevo saldo de la cuenta", example = "1500.75")
    private double newBalance;
}