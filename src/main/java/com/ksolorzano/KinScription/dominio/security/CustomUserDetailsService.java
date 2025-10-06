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
        Optional<Administrador> adminOpt = administradorRepository.findByEmail(username);
        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + admin.getRol().name()));

            return new User(admin.getEmail(), admin.getPassword(), authorities);
        }

        Optional<AdmParticipante> participanteOpt = participanteRepository.findByUsername(username);
        if (participanteOpt.isPresent()) {
            AdmParticipante participante = participanteOpt.get();
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_PARTICIPANTE"));

            return new User(participante.getUsername(), participante.getPassword(), authorities);
        }

        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }
}