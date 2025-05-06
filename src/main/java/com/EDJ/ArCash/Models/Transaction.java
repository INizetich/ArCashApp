package com.EDJ.ArCash.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_transaction;

    private String id_operation;


    @ManyToOne
    @JoinColumn(name = "id_Origin")
    private Account id_origin;

    @ManyToOne
    @JoinColumn(name = "id_Destination")
    private Account id_destination;

    @NotBlank(message = "El monto no puede estar vacio ni tampoco puede ser negativo")
    private Double balance;

    @NotBlank(message = "El estado de la transaccion no puede estar vacia")
    private String state;


  private String transaction_date;


    @PrePersist
    private void prePersist(){
        generateUUID();
        verifyAmount();
        createDate();
    }





    //-------------------METODOS PRIVATE DE VALIDACION Y CREACION DE VALORES DE FORMA AUTOMATICA---------------------
    private void generateUUID() {
        if (id_operation == null) {
            this.id_operation = UUID.randomUUID().toString().replace("-", "").substring(0, 22);
        }
    }

    private void verifyAmount(){
        if(balance < 0){
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
    }

    private void createDate(){
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime fechaActual = LocalDateTime.now();
        this.transaction_date = fechaActual.format(formateador);

    }
}
