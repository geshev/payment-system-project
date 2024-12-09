package com.example.payment.service.security;

import com.example.payment.data.dto.security.LoginRequest;
import com.example.payment.data.dto.security.LoginResponse;
import com.example.payment.service.AccountService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {

    private static final String ROLES_CLAIM = "roles";

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;

    public AuthenticationService(final JwtService jwtService, final AuthenticationManager authenticationManager,
                                 final AccountService accountService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
    }

    public LoginResponse authenticate(final LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        Map<String, Set<String>> accountClaims = new HashMap<>();
        Set<String> accountRoles = Set.of(accountService.getAccountRole(loginRequest.username()));
        accountClaims.put(ROLES_CLAIM, accountRoles);

        String jwtToken = jwtService.generateToken(accountClaims, loginRequest.username());
        return new LoginResponse(jwtToken);
    }
}
