package com.ll.rest.base.security;

import com.ll.rest.base.entry.ApiAuthenticationEntryPoint;
import com.ll.rest.base.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApiSecurityConfig {
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    private final ApiAuthenticationEntryPoint apiAuthenticationEntryPoint;

    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .exceptionHandling(exHandle -> exHandle.authenticationEntryPoint(apiAuthenticationEntryPoint))
                .authorizeHttpRequests(
                        authHttpRequests -> authHttpRequests
                                .requestMatchers("/api/*/member/login").permitAll()
                                .requestMatchers("/api/*/articles").permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .cors().disable()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement(sessionManageMent -> sessionManageMent.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
