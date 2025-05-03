package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    private final AccountService accountService;

    public UserService(UserRepository userRepository, AccountService accountService) {
        this.userRepository = userRepository;
        this.accountService = accountService;
    }


    public void insertarUsuario(User user){
        userRepository.save(user);
        accountService.createAccount(user, "PESOS");
    }






    /// -------------METODOS PRIVATE DE VERIFICACION DE CAMPOS EN USER-----------------


}
