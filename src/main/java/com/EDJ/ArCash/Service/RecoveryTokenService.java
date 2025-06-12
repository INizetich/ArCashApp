package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.Models.RecoveryToken;
import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Repository.RecoveryTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecoveryTokenService {
    @Autowired
    private RecoveryTokenRepository recoveryTokenRepository;


    @Transactional
    public String createRecoveryToken(User user){
        recoveryTokenRepository.deleteByUser_Id(user.getId());
        RecoveryToken recoveryToken = new RecoveryToken(user);
        recoveryTokenRepository.save(recoveryToken);
        return recoveryToken.getToken();
    }
}
