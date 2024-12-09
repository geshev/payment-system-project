package com.example.payment.integration;

import com.example.payment.controller.security.AuthenticationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaymentApplicationTests {

    private final AuthenticationController authenticationController;

    @Autowired
    PaymentApplicationTests(final AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
    }

    @Test
    void contextLoads() {
        assertThat(authenticationController).isNotNull();
    }
}
