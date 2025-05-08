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

    public Credentials createCredentials(User user){
        Credentials credentials = new Credentials();
        credentials.setUsername(user.getAlias());
        String passEncoded = passwordEncoder.encode(user.getDni());
        credentials.setPass(passEncoded);
        credentials.setUser(user);
        credentials.setPermissions("USER");
        return credentialRepository.save(credentials);
    }



}
