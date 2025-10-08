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

        // --- BÚSQUEDA EXPLÍCITA ---

        // Intento 1: ¿Es un administrador de inscripción?
        Optional<Administrador> adminOpt = administradorRepository.findByEmail(username);
        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + admin.getRol().name()));
            String rolLegible = formatRole(admin.getRol().name());

            return new CustomUserDetails(
                    admin.getEmail(),
                    admin.getPassword(),
                    authorities,
                    admin.getNombreCompleto(),
                    rolLegible
            );
        }

        // Intento 2: ¿Es un participante?
        Optional<AdmParticipante> participanteOpt = participanteRepository.findByUsername(username);
        if (participanteOpt.isPresent()) {
            AdmParticipante participante = participanteOpt.get();
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_PARTICIPANTE"));

            return new CustomUserDetails(
                    participante.getUsername(),
                    participante.getPassword(),
                    authorities,
                    participante.getNombreCompleto() + " " + participante.getApellidos(),
                    "Participante"
            );
        }

        // Si después de buscar en AMBAS tablas no se encuentra, entonces lanzamos la excepción.
        throw new UsernameNotFoundException("Usuario no encontrado en el portal de admisión: " + username);
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