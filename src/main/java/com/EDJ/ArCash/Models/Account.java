package com.EDJ.ArCash.Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
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

    @Column(name = "balance")
    private double balance;

    /// SE GENERA UN CVU PARA LA CUENTA DEL USUARIO EL CUAL NO VA A SER MODIFICABLE(verificar si se repite en services)
    @Column(unique = true, name = "account_cvu")
    private String accountCvu;


    /// SE SETEA POR DEFECTO LA CUENTA EN TIPO PESOS, LA DE DOLARES LA ABRE EL USUARIO SI ASI LO QUIERA
    @Column(name = "type")
    private String accountType;

    @Column(name = "creation_date")
    private String creationDate;


    public Account (User user){
        this.user = user;
    }


    @PrePersist
    private void GenerateCreationDate(){
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime fechaActual = LocalDateTime.now();
        this.creationDate = fechaActual.format(formateador);
    }




}
