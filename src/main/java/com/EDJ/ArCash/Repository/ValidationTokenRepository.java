package com.EDJ.ArCash.Repository;

import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Models.ValidationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ValidationTokenRepository extends JpaRepository<ValidationToken, Long> {
    /// HAY QUE CREAR LOS METODOS PARA LAS VALIDACIONES

    List<ValidationToken> findAllByUsedFalseAndExpirationDateBefore(LocalDateTime now);

    ValidationToken findByUser(User user);

    ValidationToken findByToken(String token);

    void deleteByUser_Id(Long id);


    List<ValidationToken> findAllByUsedTrueAndExpirationDateBefore(LocalDateTime now);
}
