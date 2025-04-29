package com.EDJ.ArCash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/// ES POSIBLE BUILDEAR LA APP EN RAILWAY SIN DOCKER FILE. SOLO JAVA 17.
/// https://arcash.ddns.net/

@SpringBootApplication
public class ArCashApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArCashApplication.class, args);
	}

}
