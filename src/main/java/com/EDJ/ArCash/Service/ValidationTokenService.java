package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Models.ValidationToken;
import com.EDJ.ArCash.Repository.ValidationTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class ValidationTokenService {

    private final ValidationTokenRepository validationTokenRepository;

    public ValidationTokenService(ValidationTokenRepository validationTokenRepository) {
        this.validationTokenRepository = validationTokenRepository;
    }

    public String createValidationToken(User user){
        ValidationToken validationToken = new ValidationToken(user);
        validationTokenRepository.save(validationToken);
        return validationToken.getToken();
    }

    public void usedToken(User user){
        validationTokenRepository.findByUser(user).setUsed(true);
        validationTokenRepository.save(validationTokenRepository.findByUser(user));
    }
}
