package com.example.api.domain.usuario.mapper;

import com.example.api.domain.usuario.dto.RegistrarUsuarioDTO;
import com.example.api.domain.usuario.entidade.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public Usuario toEntity(RegistrarUsuarioDTO dto) {
        return new Usuario(dto.login(), new BCryptPasswordEncoder().encode(dto.senha()), dto.funcao());
    }

}
