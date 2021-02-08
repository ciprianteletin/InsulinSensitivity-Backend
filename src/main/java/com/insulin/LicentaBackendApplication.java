package com.insulin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * The starting point of the entire application. On start, Spring Context will create all the components and will inject each object where it is needed.
 * It will scan for components(included services, repository, controllers) and will manage the lifecycle of each one.
 * It will find additional config files and will add that configuration to the application.
 */
@SpringBootApplication
public class LicentaBackendApplication {
    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(LicentaBackendApplication.class, args);
    }

}
