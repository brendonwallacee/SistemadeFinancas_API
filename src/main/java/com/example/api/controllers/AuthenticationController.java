package com.example.api.controllers;

import com.example.api.domain.usuario.dto.AutenticacaoDTO;
import com.example.api.domain.usuario.dto.RegistrarUsuarioDTO;
import com.example.api.domain.usuario.dto.RespostaLoginDTO;
import com.example.api.services.AutenticacaoService;
import com.example.api.services.RegistrarUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthenticationController {

    private final RegistrarUsuarioService usuarioService;
    private final AutenticacaoService autenticacaoService;

    @PostMapping("/login")
    public ResponseEntity<RespostaLoginDTO> login(@RequestBody @Valid AutenticacaoDTO data) {
        RespostaLoginDTO resposta = autenticacaoService.autenticar(data);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/registrar")
    public ResponseEntity<Void> registrar(@RequestBody @Valid RegistrarUsuarioDTO data) {
        usuarioService.registrarUsuario(data);
        return ResponseEntity.ok().build();

    }

}
