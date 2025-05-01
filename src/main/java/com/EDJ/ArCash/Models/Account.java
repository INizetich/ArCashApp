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
    private long id_account;


    /// Muchas cuentas(caja en pesos y dolares) pertenece a un usuario
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;


    /// SE GENERA UN ALIAS POR DEFECTO QUE LUEGO EL USUARIO PUEDE EDITAR(verificar si se repite en services)
    @Column(unique = true)
    private String account_nickname;


    private double balance;

    /// SE GENERA UN CVU PARA LA CUENTA DEL USUARIO EL CUAL NO VA A SER MODIFICABLE(verificar si se repite en services)
    @Column(unique = true)
    private String account_cvu;


    /// SE SETEA POR DEFECTO LA CUENTA EN TIPO PESOS, LA DE DOLARES LA ABRE EL USUARIO SI ASI LO QUIERA
    private String account_type;



    @PrePersist
    private void PrePersist(){
        defaultNickName();
        defaultAccountType();
        generateCvu();
        iniciateBalance();
    }




    /// ---------------------------- SETEAMOS VALORES POR DEFECTO ------------------------------



    private void defaultNickName() {
        final String[] ADJECTIVES = {
                "happy", "brave", "fast", "calm", "smart", "silly", "cool", "kind", "wild", "bold"
        };

        final String[] ANIMALS = {
                "tiger", "lion", "panda", "eagle", "fox", "whale", "zebra", "wolf", "rabbit", "koala"
        };

        Random rand = new Random();
        String adjective = ADJECTIVES[rand.nextInt(ADJECTIVES.length)];
        String animal = ANIMALS[rand.nextInt(ANIMALS.length)];

        // Agregar sufijo aleatorio de 2 letras para minimizar colisiones
        String suffix = String.valueOf((char)(rand.nextInt(26) + 'A')) +
                String.valueOf((char)(rand.nextInt(26) + 'A'));

        String alias = (adjective + animal + suffix).toUpperCase();

        // Truncar si excede 15 caracteres
        if (alias.length() > 15) {
            alias = alias.substring(0, 15);
        }

        this.account_nickname = alias;
    }

    private void iniciateBalance(){
        balance = 0.0;
    }


    private void defaultAccountType(){
        account_type = AccountTypes.PESOS.toString();
    }


    /// GENERA EL CVU PARA LA CUENTA (esto no determina al 100% de que no se generen cvus iguales, vamos a manejar eso con un try/catch en services)
    private  void generateCvu() {
        String entidad = "00002001"; // código de entidad ficticio
        String cuenta = generateAccountNumber(13);
        String baseCvu = entidad + cuenta;
        int digitoVerificador = calculateValidatorDigit(baseCvu);
        account_cvu = baseCvu + digitoVerificador;
    }


    private  String generateAccountNumber(int longitud) {
        Random rand = new Random();
        StringBuilder cuenta = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            cuenta.append(rand.nextInt(10));
        }
        return cuenta.toString();
    }

    private  int calculateValidatorDigit(String base) {
        int[] pesos = {3, 1}; // Alternancia
        int suma = 0;
        for (int i = 0; i < base.length(); i++) {
            int digito = Character.getNumericValue(base.charAt(i));
            suma += digito * pesos[i % 2];
        }
        int resto = suma % 10;
        return resto == 0 ? 0 : 10 - resto;
    }


}
