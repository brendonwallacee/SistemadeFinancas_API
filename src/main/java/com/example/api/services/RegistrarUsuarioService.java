package com.example.api.services;

import com.example.api.domain.usuario.dto.RegistrarUsuarioDTO;
import com.example.api.domain.usuario.entidade.Usuario;
import com.example.api.domain.usuario.mapper.UsuarioMapper;
import com.example.api.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrarUsuarioService {
    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;

    public void registrarUsuario(RegistrarUsuarioDTO dto) {
        if ((repository.findByLogin(dto.login()) != null)) {
            throw new RuntimeException("Usuario já cadastrado");
        }
        Usuario usuario = mapper.toEntity(dto);
        repository.save(usuario);
    }
}
