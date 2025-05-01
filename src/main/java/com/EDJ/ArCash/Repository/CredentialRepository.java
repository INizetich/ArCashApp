package com.EDJ.ArCash.Repository;

import com.EDJ.ArCash.Models.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends JpaRepository<Credentials, Long> {
    /// aca no agregamos nada, JPA maneja las operaciones CRUD con metodos predefinidos
}
