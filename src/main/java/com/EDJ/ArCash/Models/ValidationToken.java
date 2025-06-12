package com.EDJ.ArCash.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "validation_tokens")
public class ValidationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "used", nullable = false)
    private boolean used;

    public ValidationToken(User user) {
        this.user = user;
        this.token = generateToken();
        this.expirationDate = LocalDateTime.now().plusHours(1);
        this.used = false;
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }
}

