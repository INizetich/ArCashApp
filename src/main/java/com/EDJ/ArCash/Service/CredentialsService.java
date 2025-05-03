package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.Models.Credentials;
import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredentialsService {

    @Autowired
    private final CredentialRepository credentialRepository;

    public CredentialsService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    public Credentials createCredentials(User user){
        Credentials credentials = new Credentials();
        credentials.setUsername(generateUniqueNickname(user));
        credentials.setPass(user.getDni());
        credentials.setUser(user);
        credentials.setPermissions("USER");
        return credentialRepository.save(credentials);
    }


    /// METODOS PRIVADOS PARA LA GENERACIÓN DE UN NICKNAME AUTOMATICO

    private String generateUniqueNickname(User user) {
        String nickname = user.getName().substring(0, 1).toUpperCase() +
                user.getLastName().substring(0, 1).toUpperCase() +
                user.getDni();
        return nickname.length() > 25 ? nickname.substring(0, 25).toUpperCase() : nickname.toUpperCase();
    }
}
