
        package com.EDJ.ArCash.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Solicitud para cambiar el alias de la cuenta")
public class AliasRequest {
    @Schema(description = "Nuevo alias para la cuenta", example = "mi.nuevo.alias")
    private String newAlias;
}