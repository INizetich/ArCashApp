
package com.EDJ.ArCash.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Respuesta con el cálculo de impuestos sobre un monto en pesos")
public class TaxPesosResponse {
    @Schema(description = "Monto original en pesos", example = "10000.00")
    private double montoOriginal;

    @Schema(description = "Moneda utilizada", example = "ARS")
    private String moneda;

    @Schema(description = "IVA calculado", example = "2100.00")
    private double IVA;

    @Schema(description = "Total final con impuestos", example = "12100.00")
    private double totalFinal;
}