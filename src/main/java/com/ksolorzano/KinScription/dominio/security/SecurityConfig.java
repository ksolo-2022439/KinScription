package com.ksolorzano.KinScription.dominio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private GestionUserDetailsService gestionUserDetailsService;

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // DEV-ONLY: Este encoder está deprecado, no utilizar en producción.
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public DaoAuthenticationProvider defaultAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public DaoAuthenticationProvider gestionAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(gestionUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain gestionSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/gestion/**")
                .authenticationProvider(gestionAuthenticationProvider()) // <-- Asignamos el proveedor específico
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/gestion/login", "/gestion/perform_login").permitAll()
                        .anyRequest().hasAnyRole("COORDINADOR", "PROFESOR")
                )
                .formLogin(form -> form
                        .loginPage("/gestion/login")
                        .loginProcessingUrl("/gestion/perform_login")
                        .defaultSuccessUrl("/gestion/dashboard", true)
                        .failureUrl("/gestion/login?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(defaultAuthenticationProvider()) // <-- Asignamos el proveedor específico
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/style/**", "/js/**", "/contracts/**", "/vendor/**").permitAll()
                        .requestMatchers("/login", "/gestion/login").permitAll()
                        .requestMatchers("/admin/gestion/**").hasRole("SUPER_ADMIN")
                        .requestMatchers("/portal/**").hasRole("PARTICIPANTE")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN_INSCRIPCION", "DIRECTOR_ADMIN", "ORIENTACION", "SECRETARIA", "SUPER_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }
}