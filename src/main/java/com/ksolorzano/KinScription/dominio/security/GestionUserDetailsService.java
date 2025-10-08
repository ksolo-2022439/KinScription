package com.ksolorzano.KinScription.dominio.security;

import com.ksolorzano.KinScription.dominio.repository.CoordinadorRepository;
import com.ksolorzano.KinScription.dominio.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GestionUserDetailsService implements UserDetailsService {

    private final CoordinadorRepository coordinadorRepository;
    private final ProfesorRepository profesorRepository;

    @Autowired
    public GestionUserDetailsService(CoordinadorRepository coordinadorRepository, ProfesorRepository profesorRepository) {
        this.coordinadorRepository = coordinadorRepository;
        this.profesorRepository = profesorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return coordinadorRepository.findByEmail(email)
                .map(coordinador -> {
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_COORDINADOR"));
                    String nombreCompleto = coordinador.getNombreCompleto() + " " + coordinador.getApellidoCompleto();

                    return new CustomUserDetails(
                            coordinador.getEmail(),
                            coordinador.getContrasena(),
                            authorities,
                            nombreCompleto,
                            "Coordinador"
                    );
                })
                .or(() -> profesorRepository.findByEmail(email)
                        .map(profesor -> {
                            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_PROFESOR"));
                            String nombreCompleto = profesor.getNombreCompleto() + " " + profesor.getApellidoCompleto();

                            return new CustomUserDetails(
                                    profesor.getEmail(),
                                    profesor.getContrasena(),
                                    authorities,
                                    nombreCompleto,
                                    "Profesor"
                            );
                        }))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario de gestión no encontrado: " + email));
    }
}