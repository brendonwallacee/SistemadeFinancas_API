package com.example.api.services;

import com.example.api.domain.usuario.dto.AutenticacaoDTO;
import com.example.api.domain.usuario.dto.RespostaLoginDTO;
import com.example.api.domain.usuario.entidade.Usuario;
import com.example.api.infra.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public RespostaLoginDTO autenticar(AutenticacaoDTO dto) {
        var authToken = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
        var auth = authenticationManager.authenticate(authToken);
        var usuario = (Usuario) auth.getPrincipal();
        String token = tokenService.generateToken(usuario);
        return new RespostaLoginDTO(token);
    }
}
