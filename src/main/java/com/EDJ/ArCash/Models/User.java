package com.EDJ.ArCash.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;

    @OneToOne(cascade = CascadeType.ALL)
    private Credentials credentials;

    @NotBlank( message = "el nombre no puede estar vacio")
    private String nombre;

    @NotBlank( message = "el apellido no puede estar vacio")
    private String apellido;

    @NotBlank( message = "el dni no puede estar vacio")
    @Column(unique = true)

    private String dni;
    @NotBlank(message = "el email no puede estar vacio")
    @Email(message = "El email debe tener formato email")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "La fecha no puede estar vacia")
    private String fecha_Creacion;

    @NotBlank(message = "El alias no puede estar vacio")
    @Column(unique = true)
    private String alias;
}
