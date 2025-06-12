package com.EDJ.ArCash.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Respuesta al realizar una transacción")
public class TransactionResponse {
    @Schema(description = "Indica si la transacción fue exitosa", example = "true")
    private boolean success;

    @Schema(description = "Mensaje descriptivo del resultado de la transacción", example = "Transferencia realizada correctamente")
    private String message;
}