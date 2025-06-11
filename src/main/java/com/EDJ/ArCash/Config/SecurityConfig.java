package com.EDJ.ArCash.Config;

import com.EDJ.ArCash.Security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF si no lo necesitas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/demo", "/register", "/css/*", "/img/*",
                                "/js/*", "/create", "/api/impuestos/calculateARS", "/api/impuestos/calculateUSD",
                                "/api/auth/login", "/error",
                                "/validate","/forgot","/reset-password",
                                "/api/auth/send-recover-mail", "/validate-request" ,
                                "/api/auth/changeUsername",
                                "/dashboard","/api/user/data",
                                "/api/transactions/{id}/getTransactions", "/swagger-ui.html").permitAll() // Endpoints libres
                        .anyRequest().authenticated()/// Los demás requieren login
                )
                .formLogin(form -> form
                        .loginPage("/PreLogin") // Tu endpoint de login personalizado (opcional)
                        .permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Usar el bean gestionado por Spring
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}