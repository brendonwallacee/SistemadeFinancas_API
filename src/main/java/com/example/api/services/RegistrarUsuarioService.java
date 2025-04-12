package com.example.api.services;

import com.example.api.domain.usuario.dto.RegistrarUsuarioDTO;
import com.example.api.domain.usuario.entidade.Usuario;
import com.example.api.domain.usuario.mapper.UsuarioMapper;
import com.example.api.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RegistrarUsuarioService {
    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;

    public void registrarUsuario(RegistrarUsuarioDTO dto) {
        if ((repository.findByLogin(dto.login()) != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Usuario já cadastrado");
        }
        Usuario usuario = mapper.toEntity(dto);
        repository.save(usuario);
    }
}
