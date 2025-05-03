package com.EDJ.ArCash;

import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


/// ES POSIBLE BUILDEAR LA APP EN RAILWAY SIN DOCKER FILE. SOLO JAVA 17.
/// https://arcash.ddns.net/

/// si queremos testear algo, usemos este codigo y modifiquemoslo:
///
///     @Bean
///     public CommandLineRunner commandLineRunner() {
///
/// 		metodo();
/// 		return null;
///    }

@SpringBootApplication
public class ArCashApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArCashApplication.class, args);
    }

}
