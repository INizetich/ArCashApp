
package com.EDJ.ArCash.DTO.AuthDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Solicitud para ingresar dinero en una cuenta")
public class AccountRequest {
 @Schema(description = "Saldo inicial o monto a modificar", example = "1000.50")
 private double balance;
}