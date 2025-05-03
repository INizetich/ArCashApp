package com.EDJ.ArCash.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id_user;

    /// un usuario puede tener una credencial, una credencial corresponde a un usuario
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Credentials credentials;

     /// un usuario puede tener muchas cuentas (caja en pesos y en dolares)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accounts;


    @NotBlank( message = "el nombre no puede estar vacio")
    @Column(name = "name")
    private String name;

    @NotBlank( message = "el apellido no puede estar vacio")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank( message = "el dni no puede estar vacio")
    @Column(unique = true, name = "dni")
    private String dni;

    @NotBlank(message = "el email no puede estar vacio")
    @Email(message = "El email debe tener formato email")
    @Column(unique = true, name = "email")
    private String email;

    @Column(name = "creation_date")
    private String creationDate;

    @NotBlank(message = "El alias no puede estar vacio")
    @Column(unique = true, name = "alias")
    private String alias;

    public User (String name,String lastName,String dni,String email,String alias){
        this.name = name;
        this.lastName = lastName;
        this.dni = dni;
        this.email = email;
        this.alias = alias;
    }

    @PrePersist
    private void PrePersist(){
       GenerateCreationDate();
    }


    private void GenerateCreationDate(){
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime fechaActual = LocalDateTime.now();
        this.creationDate = fechaActual.format(formateador);
    }
}
