package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.Models.ValidationToken;
import com.EDJ.ArCash.Repository.UserRepository;
import com.EDJ.ArCash.Repository.ValidationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TokenCleanupService {

    @Autowired
    private ValidationTokenRepository validationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Scheduled(fixedRate = 3600000) //TIEMPO PARA LIMPIAR TOKENS Y USUARIOS NO VALIDADOS
    public void removeExpiredUnvalidatedUsers() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Buscando tokens expirados antes de: " + now);

        List<ValidationToken> expiredTokensAndUsers = validationTokenRepository.findAllByUsedFalseAndExpirationDateBefore(now);
        System.out.println("Tokens encontrados: " + expiredTokensAndUsers.size());
        for (ValidationToken token : expiredTokensAndUsers) {
            userRepository.delete(token.getUser());
        }

        List<ValidationToken> expiredTokens = validationTokenRepository.findAllByUsedTrueAndExpirationDateBefore(now);
        validationTokenRepository.deleteAllInBatch(expiredTokens);
        System.out.println("Token eliminado:" + expiredTokens);
    }
}





