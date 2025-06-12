package com.EDJ.ArCash.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Respuesta con información de la cotización del dólar")
public class ApiUsdResponse {
    @Schema(description = "Moneda consultada", example = "USD")
    private String moneda;

    @Schema(description = "Casa de cambio", example = "Banco Nación")
    private String casa;

    @Schema(description = "Nombre de la cotización", example = "Dólar Oficial")
    private String nombre;

    @Schema(description = "Valor de compra", example = "900.50")
    private double compra;

    @Schema(description = "Valor de venta", example = "950.75")
    private double venta;

    @Schema(description = "Fecha de última actualización", example = "2024-06-10T15:30:00Z")
    private String fechaActualizacion;
}