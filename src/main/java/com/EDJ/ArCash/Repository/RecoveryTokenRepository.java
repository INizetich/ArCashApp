package com.EDJ.ArCash.Repository;

import com.EDJ.ArCash.Models.RecoveryToken;
import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Models.ValidationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecoveryTokenRepository extends JpaRepository<RecoveryToken,Long> {

        RecoveryToken findByUser(User user);

        Optional<RecoveryToken> findByToken(String token);

        List<RecoveryToken> findAllByUsedTrue();

        void deleteByUser_Id(Long id);

}
