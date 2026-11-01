
package com.example.api_gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    JWT_UTILS jwtUtils(@Value("${JWT_SECRET:change-me-to-a-32-byte-development-secret}") String secret) {
        return new JWT_UTILS(secret);
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JWT_UTILS jwt) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(errors ->
                        errors.authenticationEntryPoint(
                                (request, response, exception) ->
                                        response.sendError(401)))
                .authorizeHttpRequests(admin -> admin
                        .requestMatchers(
                                "/admin/login",
                                "/admin/register",
                                "/auth-public/login",
                                "/auth-public/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/image/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(
                        new JWTfilter(jwt),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
