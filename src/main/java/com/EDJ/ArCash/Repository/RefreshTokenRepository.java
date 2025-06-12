package com.EDJ.ArCash.Repository;


import com.EDJ.ArCash.Models.RefreshToken;
import com.EDJ.ArCash.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long>{

    Optional<RefreshToken> findByUserAndRevokedFalse(User user);

    List<RefreshToken> findAllByUserAndRevokedFalse(User user);

    boolean existsByUser_IdAndRevokedFalse(Long id);

    int deleteByRevokedTrueOrExpiresAtBefore(LocalDateTime dateTime);


    Optional<RefreshToken> findByRefreshTokenAndRevokedFalse(String refreshToken);
}
