package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final AccountService accountService;

    private final CredentialsService credentialsService;

    private final EmailService emailService;

    private final ValidationTokenService validationTokenService;

    public UserService(UserRepository userRepository, AccountService accountService, CredentialsService credentialsService, EmailService emailService, ValidationTokenService validationTokenService) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.credentialsService = credentialsService;
        this.emailService = emailService;
        this.validationTokenService = validationTokenService;
    }


    public void insertarUsuario(User user) throws MessagingException, UnsupportedEncodingException {
        user.setName(user.getName().substring(0,1).toUpperCase() + user.getName().substring(1).toLowerCase());
        user.setLastName(user.getLastName().substring(0,1).toUpperCase() + user.getLastName().substring(1).toLowerCase());
        user.setEnabled(false);
        userRepository.save(user);
        credentialsService.createCredentials(user);
        String token = validationTokenService.createValidationToken(user);
        emailService.testEmail(user, token);
    }


    public void validarUsuario(User user){
        accountService.createAccount(user, "PESOS"); ///UNA VEZ VALIDE SU CUENTA
        user.setEnabled(true);
        userRepository.save(user);
        validationTokenService.usedToken(user);
    }



}
