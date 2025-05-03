package com.EDJ.ArCash.Service;

import com.EDJ.ArCash.Models.Account;
import com.EDJ.ArCash.Models.Exc.AlreadyExistCvu;
import com.EDJ.ArCash.Models.Imp.AccountTypes;
import com.EDJ.ArCash.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AccountService {
    @Autowired
    private final AccountRepository accountRepository;


    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public Account createAccount(Account account) {
        account.setAccountType(AccountTypes.PESOS.toString());
        account.setBalance(0.0);
        account.setAccountNickname(generateUniqueNickname());
        account.setAccountCvu(generateUniqueCvu());
        return accountRepository.save(account);
    }


    /// -----------------------METODOS PRIVATE PARA GENERAR UN ALIAS ALEATORIO Y EL CVU DE LA CUENTA -----------------------

    public String generateUniqueNickname() {
        String account_nickname;
        do {
            account_nickname = generateRandomNickname();
        } while (accountRepository.existsByAccountNickname(account_nickname));
        return account_nickname;
    }

    public static String generateRandomNickname() {
        String[] options = {"happy", "brave", "fast", "calm", "smart", "silly", "cool", "kind", "wild", "bold",
                "tiger", "lion", "panda", "eagle", "fox", "whale", "zebra", "wolf", "rabbit", "koala",
                "red", "green", "blue", "yellow", "black", "white", "pink", "orange", "purple", "brown",
                "apple", "orange", "banana", "grapes", "peach", "pear", "strawberry", "cherry", "mango", "melon"
        };
        Random rand = new Random();

        String first = options[rand.nextInt(options.length)];
        String second = options[rand.nextInt(options.length)];
        String third = options[rand.nextInt(options.length)];


        char sufijo1 = (char) ('A' + rand.nextInt(26));
        char sufijo2 = (char) ('A' + rand.nextInt(26));


        String alias = (first + "." + second + "." + third + "." + sufijo1 + sufijo2);
        return alias.length() > 15 ? alias.substring(0, 15) : alias;
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





