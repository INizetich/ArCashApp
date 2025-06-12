package com.EDJ.ArCash.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminRequest {
    private long id;
    private String name;
    private String lastName;
    private String dni;
    private String email;
    private String username;
    private Long idAccount;
    private boolean enabled;
    private String password;
}
