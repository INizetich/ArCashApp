package com.EDJ.ArCash.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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
    private String name;

    @NotBlank( message = "el apellido no puede estar vacio")
    private String lastName;

    @NotBlank( message = "el dni no puede estar vacio")
    @Column(unique = true)

    private String dni;
    @NotBlank(message = "el email no puede estar vacio")
    @Email(message = "El email debe tener formato email")
    @Column(unique = true)
    private String email;


    private String creationDate;

    @NotBlank(message = "El alias no puede estar vacio")
    @Column(unique = true)
    private String alias;

    @PrePersist
    private void generarFechaCreacion(){
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime fechaActual = LocalDateTime.now();
        this.creationDate = fechaActual.format(formateador);
    }
}
