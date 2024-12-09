package com.example.payment.config.security;

import com.example.payment.filter.security.JwtTokenFilter;
import com.example.payment.service.security.PasswordService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

import static com.example.payment.data.model.Role.ADMIN;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenFilter jwtTokenFilter;
    private final PasswordService passwordService;

    public SecurityConfig(final DataSource dataSource, final PasswordEncoder passwordEncoder,
                   final JwtTokenFilter jwtTokenFilter, final PasswordService passwordService) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenFilter = jwtTokenFilter;
        this.passwordService = passwordService;
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .userDetailsPasswordManager(passwordService)
                .usersByUsernameQuery("select username, password, enabled from account where username=?")
                .authoritiesByUsernameQuery("select username, role from account where username=?");
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
                .exceptionHandling(c -> c.authenticationEntryPoint(
                        (request, response, ex) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, null)
                ))
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/token").permitAll()
                        .requestMatchers(HttpMethod.GET, "/merchants").hasAuthority(ADMIN.name())
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
