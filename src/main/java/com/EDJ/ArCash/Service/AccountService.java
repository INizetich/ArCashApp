package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.DTO.AliasResponse;
import com.EDJ.ArCash.Models.Account;
import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {



    @Autowired
    private final AccountRepository accountRepository;


    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public void createAccount(User user, String accountType) {
        Account account = new Account();
        account.setUser(user);
        account.setAccountType(accountType);
        account.setBalance(0.0);
        account.setAccountNickname(generateUniqueNickname());
        account.setAccountCvu(generateUniqueCvu());
        accountRepository.save(account);
    }


    public boolean updateBalance(double balanceToAdd, Long id){
        Optional<Account> optionalAccount = accountRepository.findByIdAccount(id);

        if (optionalAccount.isEmpty()) {
            return false;
        } else {
            Account account = optionalAccount.get();
            double newBalance = account.getBalance() + balanceToAdd;
            account.setBalance(newBalance);
            accountRepository.save(account);
            return true;
        }
    }

   public Optional<Account> findAccountByID(long id){
        return accountRepository.findByIdAccount(id);
    }

    public Optional<Account> encontrarCuentaPorAlias(String alias){
        return accountRepository.findByAccountNickname(alias);
    }

    public Optional<Account> encontrarCuentaPorCvu(String cvu){
        return accountRepository.findByAccountCvu(cvu);
    }

    public ResponseEntity<AliasResponse> changeAlias(String newAlias, Long id, Object responseEntity) {
        // Regex mejorado: exige al menos una letra
        String regex = "^(?=.*[A-Za-z])(?=^[A-Za-z0-9]+(\\.[A-Za-z0-9]+)+$)(?!.*\\.\\.)[A-Za-z0-9.]{4,25}$";
        if (!newAlias.matches(regex)) {
            return ResponseEntity.status(400).body(
                    new AliasResponse(false, "Formato de alias inválido. Debe tener entre 4 y 25 caracteres, solo letras, números y puntos, al menos un punto en el medio, no puede ser solo números ni tener '..'.")
            );
        }

        Optional<Account> optionalAccount = accountRepository.findByIdAccount(id);

        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(498).body(new AliasResponse(false, "Cuenta no encontrada."));
        }
        Account acc = optionalAccount.get();

        if (acc.getUser().getId().equals(responseEntity)) {
            if (!accountRepository.existsByAccountNickname(newAlias)) {
                acc.setAccountNickname(newAlias);
                accountRepository.save(acc);
                return ResponseEntity.status(200).body(new AliasResponse(true, "Alias actualizado exitosamente."));
            }
        } else {
            return ResponseEntity.status(403).body(new AliasResponse(false, "No tienes permisos para hacer eso."));
        }
        return ResponseEntity.status(403).body(new AliasResponse(false, "Alias actualmente en uso."));
    }




    /// -----------------------METODOS PRIVATE PARA GENERAR UN ALIAS ALEATORIO Y EL CVU DE LA CUENTA -----------------------

    private String generateUniqueNickname() {
        String account_nickname;
        do {
            account_nickname = generateRandomNickname();
        } while (accountRepository.existsByAccountNickname(account_nickname));
        return account_nickname;
    }

    private String generateRandomNickname() {
        String[] options = {"happy", "brave", "fast", "calm", "smart", "silly", "cool", "kind", "wild", "bold",
                "tiger", "lion", "panda", "eagle", "fox", "whale", "zebra", "wolf", "rabbit", "koala",
                "red", "green", "blue", "yellow", "black", "white", "pink", "orange", "purple", "brown",
                "apple", "orange", "banana", "grapes", "peach", "pear", "strawberry", "cherry", "mango", "melon"
        };
        Random rand = new Random();

        String first = options[rand.nextInt(options.length)];
        String second = options[rand.nextInt(options.length)];


        char sufijo1 = (char) ('A' + rand.nextInt(26));
        char sufijo2 = (char) ('A' + rand.nextInt(26));


        String alias = (first + "." + second + "." + sufijo1 + sufijo2);
        return alias.length() > 25 ? alias.substring(0, 25).toUpperCase() : alias.toUpperCase();
    }

    private String generateUniqueCvu() {
        String account_cvu;
        do {
            account_cvu = generateCvu();
        } while (accountRepository.existsByAccountCvu(account_cvu));
        return account_cvu;
    }

    private String generateCvu() {
        String entidad = "00002001";
        String cuenta = generateRandomDigits(13);
        String base = entidad + cuenta;
        int verificador = calculateValidatorDigit(base);
        return base + verificador;
    }

    private String generateRandomDigits(int length) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    private int calculateValidatorDigit(String base) {
        int[] pesos = {3, 1};
        int suma = 0;
        for (int i = 0; i < base.length(); i++) {
            suma += Character.getNumericValue(base.charAt(i)) * pesos[i % 2];
        }
        int resto = suma % 10;
        return resto == 0 ? 0 : 10 - resto;
    }
}





