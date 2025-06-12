package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.Models.Credentials;
import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Repository.CredentialRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CredentialsService {


    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;

    public CredentialsService(CredentialRepository credentialRepository, PasswordEncoder passwordEncoder) {
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createCredentials(User user, String rawPassword){
        Credentials credentials = new Credentials();
        credentials.setUsername(user.getAlias());
        credentials.setPass(passwordEncoder.encode(rawPassword));
        credentials.setUser(user);
        credentialRepository.save(credentials);
    }



}
