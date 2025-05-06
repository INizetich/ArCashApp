package com.EDJ.ArCash.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


public class RegisterResponse {
    private String mensaje;

    private boolean success;



    public RegisterResponse(boolean success, String mensaje) {
        this.success = success;
        this.mensaje = mensaje;

    }
}
