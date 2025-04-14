package com.example.api.services;

import com.example.api.domain.token.dto.ConfirmarTokenDTO;
import com.example.api.domain.usuario.dto.RecuperarSenhaDTO;
import com.example.api.domain.usuario.dto.RegistrarUsuarioDTO;
import com.example.api.domain.token.entidade.TokenRecuperacao;
import com.example.api.domain.usuario.entidade.Usuario;
import com.example.api.domain.usuario.mapper.UsuarioMapper;
import com.example.api.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final TokenHelperService tokenService;
    private final UsuarioMapper mapper;
    private final EmailService emailService;

    public void registrarUsuario(RegistrarUsuarioDTO dto) {
        if ((repository.findByLogin(dto.login()) != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Usuario já cadastrado");
        }
        Usuario usuario = mapper.toEntity(dto);
        repository.save(usuario);
    }

    public void recuperarSenha(RecuperarSenhaDTO dto){
        Usuario usuario = repository.findByEmail(dto.email());
        if ((usuario == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email não existe");
        }

        TokenRecuperacao tokenEntity = tokenService.criarToken(usuario);
        emailService.enviarEmailComToken(dto.email(), tokenEntity.getToken());
    }

    public void confirmarToken(ConfirmarTokenDTO dto) {
        TokenRecuperacao tokenEntity = tokenService.validarToken(dto.token());
        Usuario usuario = tokenEntity.getUsuario();

        mapper.atualizarSenha(usuario, dto.novaSenha());
        repository.save(usuario);

        tokenService.apagarToken(tokenEntity);
    }

}
