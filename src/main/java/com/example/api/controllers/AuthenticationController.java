package com.example.api.controllers;

import com.example.api.domain.token.dto.ConfirmarTokenDTO;
import com.example.api.domain.usuario.dto.AutenticacaoDTO;
import com.example.api.domain.usuario.dto.RecuperarSenhaDTO;
import com.example.api.domain.usuario.dto.RegistrarUsuarioDTO;
import com.example.api.domain.usuario.dto.RespostaLoginDTO;
import com.example.api.services.AutenticacaoService;
import com.example.api.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthenticationController {

    private final UsuarioService usuarioService;
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

    @PostMapping("/recuperar_senha")
    public ResponseEntity<Void> recuperarSenha(@RequestBody @Valid RecuperarSenhaDTO data) {
        usuarioService.recuperarSenha(data);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/confirmar_token")
    public ResponseEntity<Void> confirmarToken(@RequestBody @Valid ConfirmarTokenDTO data){
        usuarioService.confirmarToken(data);
        return ResponseEntity.ok().build();
    }

}
