package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    private final AccountService accountService;

    private final CredentialsService credentialsService;

    public UserService(UserRepository userRepository, AccountService accountService, CredentialsService credentialsService) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.credentialsService = credentialsService;
    }


    public void insertarUsuario(User user){
        user.setName(user.getName().substring(0,1).toUpperCase() + user.getName().substring(1).toLowerCase());
        user.setLastName(user.getLastName().substring(0,1).toUpperCase() + user.getLastName().substring(1).toLowerCase());
        userRepository.save(user);
        credentialsService.createCredentials(user);
        accountService.createAccount(user, "PESOS");
    }






    /// -------------METODOS PRIVATE DE VERIFICACION DE CAMPOS EN USER-----------------


}
