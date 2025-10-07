package com.ksolorzano.KinScription.dominio.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private final String nombreCompleto;
    private final String rolSimple;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String nombreCompleto, String rolSimple) {
        super(username, password, authorities);
        this.nombreCompleto = nombreCompleto;
        this.rolSimple = rolSimple;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getRolSimple() {
        return rolSimple;
    }
}