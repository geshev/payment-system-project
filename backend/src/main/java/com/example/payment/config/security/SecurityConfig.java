package com.example.payment.config.security;

import com.example.payment.service.security.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final PasswordService passwordService;

    public SecurityConfig(DataSource dataSource, PasswordEncoder passwordEncoder, PasswordService passwordService) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.passwordService = passwordService;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .userDetailsPasswordManager(passwordService)
                .usersByUsernameQuery("select username, password, enabled from account where username=?")
                .authoritiesByUsernameQuery("select username, role from account where username=?");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.GET, "/**").authenticated()
                )
                .formLogin(login ->
                        login.defaultSuccessUrl("/", true)
                );
        return http.build();
    }
}
