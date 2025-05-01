package com.EDJ.ArCash.Repository;

import com.EDJ.ArCash.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    /*ACA NO AGREGAMOS NADA, JPA MANEJA LAS OPERACIONES CRUD CON METODOS PREDEFINIDOS
    *
    * SI NECESITAMOS UN METODO QUE NO ESTA EN JPAREPOSITORY, PODEMOS CREAR NUESTRO METODO PROPIO
    * */

    /// este metodo nos va a servir para verificar si el cvu existe en la base de datos antes de que se cargue una nueva cuenta
    boolean existsByAccountCvu(String accountCvu);
}
