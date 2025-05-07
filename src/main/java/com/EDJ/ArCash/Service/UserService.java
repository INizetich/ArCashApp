package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class UserService {

    private UserRepository userRepository;

    private final AccountService accountService;

    private final CredentialsService credentialsService;

    private final EmailService emailService;

    public UserService(UserRepository userRepository, AccountService accountService, CredentialsService credentialsService, EmailService emailService) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.credentialsService = credentialsService;
        this.emailService = emailService;
    }


    public void insertarUsuario(User user) throws MessagingException, UnsupportedEncodingException {
        user.setName(user.getName().substring(0,1).toUpperCase() + user.getName().substring(1).toLowerCase());
        user.setLastName(user.getLastName().substring(0,1).toUpperCase() + user.getLastName().substring(1).toLowerCase());
        userRepository.save(user);
        credentialsService.createCredentials(user);
        accountService.createAccount(user, "PESOS");
        emailService.testEmail(user);
    }






    /// -------------METODOS PRIVATE DE VERIFICACION DE CAMPOS EN USER-----------------


}
