
package com.EDJ.ArCash.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Solicitud para calcular impuestos sobre un monto en pesos")
public class TaxPesosRequest {

    @NotNull(message = "el monto en pesos es obligatorio")
    @Min(value = 0, message = "El monto debe ser mayor que cero")
    @Schema(description = "Monto en pesos argentinos a calcular", example = "10000.00")
    private double montoARS;
}