package com.wip.assetmanagementsystem.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Override
    public void run(String... args) {
        // Admin credentials are hardcoded in CustomUserDetailsService
    }
}
