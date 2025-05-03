package com.EDJ.ArCash.Models;

import com.EDJ.ArCash.Models.Imp.AccountTypes;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Random;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idAccount;


    /// Muchas cuentas(caja en pesos y dolares) pertenece a un usuario
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;


    /// SE GENERA UN ALIAS POR DEFECTO QUE LUEGO EL USUARIO PUEDE EDITAR(verificar si se repite en services)
    @Column(unique = true, name = "account_nickname")
    private String accountNickname;


    private double balance;

    /// SE GENERA UN CVU PARA LA CUENTA DEL USUARIO EL CUAL NO VA A SER MODIFICABLE(verificar si se repite en services)
    @Column(unique = true, name = "account_cvu")
    private String accountCvu;


    /// SE SETEA POR DEFECTO LA CUENTA EN TIPO PESOS, LA DE DOLARES LA ABRE EL USUARIO SI ASI LO QUIERA
    @Column(name = "type")
    private String accountType;


    public Account (User user){
        this.user = user;
    }




}
