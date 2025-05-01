package com.EDJ.ArCash.Repository;

import com.EDJ.ArCash.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /// aca no agregamos nada, JPA maneja las operaciones CRUD con metodos predefinidos
}
