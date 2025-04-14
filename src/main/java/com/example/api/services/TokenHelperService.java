package com.example.api.services;

import com.example.api.domain.token.entidade.TokenRecuperacao;
import com.example.api.domain.usuario.entidade.Usuario;
import com.example.api.repositories.TokenRecuperacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenHelperService {

    private final TokenRecuperacaoRepository tokenRepository;

    public TokenRecuperacao validarToken(String token) {
        TokenRecuperacao entity = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido"));

        if (entity.isUsado() || entity.getExpiracao().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expirado ou já utilizado");
        }

        return entity;
    }

    public TokenRecuperacao criarToken(Usuario usuario) {
        String token = gerarTokenRecovery();

        TokenRecuperacao entity = new TokenRecuperacao();
        entity.setToken(token);
        entity.setExpiracao(LocalDateTime.now().plusMinutes(15));
        entity.setUsuario(usuario);

        return tokenRepository.save(entity);
    }

    public void apagarToken(TokenRecuperacao token) {
        token.setUsado(true);
        tokenRepository.delete(token);
    }

    public String gerarTokenRecovery() {
        return UUID.randomUUID().toString().replaceAll("[^A-Za-z)-9]", "").substring(0,6).toUpperCase();
    }
}
