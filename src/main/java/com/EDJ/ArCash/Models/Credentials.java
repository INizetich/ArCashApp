package com.EDJ.ArCash.Models;

import com.EDJ.ArCash.Models.Imp.Permissions;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Credentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;

    @NotBlank(message = "El nombre de usuario no puede estar vacio")
    private String username;

    @NotBlank(message = "La password no puede estar vacia")
    private String pass;


    private String permissions;


    @PrePersist
    private void PrePersist(){
        InitializePermission();
    }

    private void InitializePermission(){
        permissions = Permissions.USER.toString();
    }
}
