package com.EDJ.ArCash.Repository;

import com.EDJ.ArCash.Models.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credentials, Long> {

    Optional<Credentials> findByUsername(String username);
    /// aca no agregamos nada, JPA maneja las operaciones CRUD con metodos predefinidos
}
