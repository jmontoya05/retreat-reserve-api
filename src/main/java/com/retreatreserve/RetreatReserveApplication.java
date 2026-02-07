package com.retreatreserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RetreatReserveApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetreatReserveApplication.class, args);
    }

}
