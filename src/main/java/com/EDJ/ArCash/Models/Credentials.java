package com.EDJ.ArCash.Models;

import com.EDJ.ArCash.Models.Imp.Permissions;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "credentials")
public class Credentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_credential")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message = "El nombre de usuario no puede estar vacio")
    @Column(unique = true, name = "username")
    private String username;

    @NotBlank(message = "La password no puede estar vacia")
    @Column(name = "pass")
    private String pass;




    public Credentials(User user,String username, String pass) {
        this.user = user;
        this.username = username;
        this.pass = pass;
    }


}
