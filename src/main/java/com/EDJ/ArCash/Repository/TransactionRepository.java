package com.EDJ.ArCash.Repository;

import com.EDJ.ArCash.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /// aca no agregamos nada, JPA maneja las operaciones CRUD con metodos predefinidos
}
