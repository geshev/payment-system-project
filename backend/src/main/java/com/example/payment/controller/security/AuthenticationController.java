package com.example.payment.controller.security;

import com.example.payment.data.dto.security.LoginRequest;
import com.example.payment.data.dto.security.LoginResponse;
import com.example.payment.service.security.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("token")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody final LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
    }
}
