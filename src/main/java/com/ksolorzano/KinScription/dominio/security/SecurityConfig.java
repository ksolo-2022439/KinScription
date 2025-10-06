package com.ksolorzano.KinScription.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // DEV-ONLY: Este encoder está deprecado, no utilizar en producción.
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/vendor/**").permitAll()
                        .requestMatchers("/login").permitAll()

                        // --- REGLAS DE ACCESO POR ROL ---
                        .requestMatchers("/portal/**").hasRole("PARTICIPANTE")
                        .requestMatchers("/admin/inscripcion/**").hasRole("ADMIN_INSCRIPCION")
                        .requestMatchers("/admin/director/**").hasRole("DIRECTOR_ADMIN")
                        .requestMatchers("/admin/orientacion/**").hasRole("ORIENTACION")
                        .requestMatchers("/admin/secretaria/**").hasRole("SECRETARIA")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}