package com.EDJ.ArCash.Repository;

import com.EDJ.ArCash.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByEnabledTrue();
    /// aca no agregamos nada, JPA maneja las operaciones CRUD con metodos predefinidos
}
