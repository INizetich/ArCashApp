
package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Models.ValidationToken;
import com.EDJ.ArCash.Repository.ValidationTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ValidationTokenService {

    private final ValidationTokenRepository validationTokenRepository;

    public ValidationTokenService(ValidationTokenRepository validationTokenRepository) {
        this.validationTokenRepository = validationTokenRepository;
    }

    @Transactional
    public String createValidationToken(User user){
        // Elimina el token anterior si existe
        validationTokenRepository.deleteByUser_Id(user.getId());
        ValidationToken validationToken = new ValidationToken(user);
        validationTokenRepository.save(validationToken);
        return validationToken.getToken();
    }

    @Transactional
    public void usedToken(User user){
        ValidationToken token = validationTokenRepository.findByUser(user);
        if (token != null) {
            token.setUsed(true);
            validationTokenRepository.save(token);
        }
    }
}