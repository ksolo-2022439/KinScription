package com.ksolorzano.KinScription.dominio.security;

import com.ksolorzano.KinScription.dominio.repository.AdmParticipanteRepository;
import com.ksolorzano.KinScription.dominio.repository.AdministradorRepository;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.Administrador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AdministradorRepository administradorRepository;
    private final AdmParticipanteRepository participanteRepository;

    @Autowired
    public CustomUserDetailsService(AdministradorRepository administradorRepository, AdmParticipanteRepository participanteRepository) {
        this.administradorRepository = administradorRepository;
        this.participanteRepository = participanteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return administradorRepository.findByEmail(username)
                .map(admin -> {
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + admin.getRol().name()));

                    String rolLegible = formatRole(admin.getRol().name());

                    return new CustomUserDetails(
                            admin.getEmail(),
                            admin.getPassword(),
                            authorities,
                            admin.getNombreCompleto(),
                            rolLegible
                    );
                })
                .or(() -> participanteRepository.findByUsername(username)
                        .map(participante -> {
                            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_PARTICIPANTE"));

                            return new CustomUserDetails(
                                    participante.getUsername(),
                                    participante.getPassword(),
                                    authorities,
                                    participante.getNombreCompleto() + " " + participante.getApellidos(), // <-- Nombre completo
                                    "Participante"
                            );
                        }))
                // 3. Si no se encuentra, lanzar excepción
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    private String formatRole(String role) {
        return switch (role) {
            case "ADMIN_INSCRIPCION" -> "Admin. Inscripción";
            case "DIRECTOR_ADMIN" -> "Director Admin.";
            case "ORIENTACION" -> "Orientación";
            case "SECRETARIA" -> "Secretaría";
            default -> role;
        };
    }
}