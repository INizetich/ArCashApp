package com.EDJ.ArCash.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String id_operation;


    @ManyToOne
    @JoinColumn(name = "id_origin")
    private Account idOrigin;

    @ManyToOne
    @JoinColumn(name = "id_destination")
    private Account idDestination;

    @NotNull("El monto no puede estar vacío")
    @Positive(message = "El monto debe ser positivo")
    private Double balance;

    @NotNull("El estado de la transacción no puede estar vacío")
    private String state;


    private String transaction_date;


    @PrePersist
    private void prePersist() {
        generateUUID();
        verifyAmount();
        createDate();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id_transaction=" + id +
                ", id_operation='" + id_operation + '\'' +
                ", idOrigin=" + idOrigin +
                ", idDestination=" + idDestination +
                ", balance=" + balance +
                ", state='" + state + '\'' +
                ", transaction_date='" + transaction_date + '\'' +
                '}';
    }

    //-------------------METODOS PRIVATE DE VALIDACION Y CREACION DE VALORES DE FORMA AUTOMATICA---------------------
    private void generateUUID() {
        if (id_operation == null) {
            this.id_operation = UUID.randomUUID().toString().replace("-", "").substring(0, 22);
        }
    }

    private void verifyAmount() {
        if (balance < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
    }

    private void createDate() {
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime fechaActual = LocalDateTime.now();
        this.transaction_date = fechaActual.format(formateador);

    }
}
