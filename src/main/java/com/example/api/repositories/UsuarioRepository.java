package com.example.api.repositories;

import com.example.api.domain.usuario.entidade.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    UserDetails findByLogin(String login);
    Usuario findByEmail(String email);
}
