package com.EDJ.ArCash;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



/// ES POSIBLE BUILDEAR LA APP EN RAILWAY SIN DOCKER FILE. SOLO JAVA 17.
/// https://arcash.ddns.net/

/// si queremos testear algo, usemos este codigo y modifiquemoslo:
///
/// @Bean public CommandLineRunner commandLineRunner() {
///
/// 		metodo();
/// 		return null;
///    }

@EnableScheduling
@SpringBootApplication
public class ArCashApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArCashApplication.class, args);
    }
}
