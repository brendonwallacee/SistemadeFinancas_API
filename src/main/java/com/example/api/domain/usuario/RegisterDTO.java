package com.example.api.domain.usuario;

public record RegisterDTO(String login, String senha, FuncaoUsuario funcao) {
}
