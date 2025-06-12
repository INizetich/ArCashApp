package com.EDJ.ArCash.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TokenCleanupRunner implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        jdbcTemplate.execute("DELETE FROM recovery_tokens WHERE used = true");
        jdbcTemplate.execute("DELETE FROM refresh_tokens WHERE revoked = true");
        jdbcTemplate.execute("DELETE FROM validation_tokens WHERE used = true");
    }
}