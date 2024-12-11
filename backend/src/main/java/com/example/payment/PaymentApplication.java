package com.example.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PaymentApplication {

    public static void main(final String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }

}
