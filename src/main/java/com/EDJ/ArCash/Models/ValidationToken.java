package com.EDJ.ArCash.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "verification_token")
public class ValidationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true, name = "token")
    private String token;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false, name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(nullable = false, name = "used")
    private boolean used;

    public ValidationToken(User user) {
        this.user = user;
        this.token = UUID.randomUUID().toString().substring(0,20);
        this.expirationDate = LocalDateTime.now().plusHours(1); // TIEMPO QUE DURA EL TOKEN
        this.used = false;
    }

}