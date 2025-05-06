package com.EDJ.ArCash.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private boolean success;
    private String message;
    private String token;

    public LoginResponse(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }


}
