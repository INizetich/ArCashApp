package com.EDJ.ArCash.DTO.AuthDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Solicitud para calcular impuestos sobre un monto en dólares")
public class TaxUsdRequest {

    @NotNull(message = "el monto en USD debe ser obligatorio")
    @Min(value = 0, message = "El monto debe ser mayor a 0")
    @Schema(description = "Monto en dólares estadounidenses a calcular", example = "100.00")
    private double montoUSD;
}