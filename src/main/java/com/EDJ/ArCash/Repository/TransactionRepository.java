package com.EDJ.ArCash.Repository;

import com.EDJ.ArCash.Models.Account;
import com.EDJ.ArCash.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByIdOriginOrIdDestination(Account acc1, Account acc2);
    /// aca no agregamos nada, JPA maneja las operaciones CRUD con metodos predefinidos

    

}
