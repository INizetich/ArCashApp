package com.EDJ.ArCash.Models;

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

    @OneToOne(mappedBy = "user")
    @JoinColumn(referencedColumnName = "id_user")
    private User user;

    @NotBlank
    private String username;

    @NotBlank
    private String pass;

    @NotBlank
    private enum permissions{USER, ADMIN};
}
