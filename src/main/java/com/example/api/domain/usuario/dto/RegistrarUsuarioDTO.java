package com.example.api.domain.usuario.dto;

import com.example.api.domain.usuario.entidade.FuncaoUsuario;

public record RegistrarUsuarioDTO(String login, String senha, FuncaoUsuario funcao, String email) {
}
