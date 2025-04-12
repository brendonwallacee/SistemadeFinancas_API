package com.example.api.controllers;

import com.example.api.domain.usuario.dto.RespostaLoginDTO;
import com.example.api.infra.security.SecurityConfiguration;
import com.example.api.infra.security.TokenService;
import com.example.api.repositories.UsuarioRepository;
import com.example.api.services.AutenticacaoService;
import com.example.api.services.RegistrarUsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static
        org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static
        org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(AuthenticationController.class)
@Import(SecurityConfiguration.class)
@SuppressWarnings("unused")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AutenticacaoService autenticacaoService;

    @MockitoBean
    private RegistrarUsuarioService usuarioService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;


    @Test
    @DisplayName("Retorno de token com login certo")
    void deveRetornarTokenAoFazerLoginComCredenciaisValidas() throws Exception {
        RespostaLoginDTO respostaLoginDTO = new RespostaLoginDTO("token-gerado");

        Mockito.when(autenticacaoService.autenticar(Mockito.any()))
                .thenReturn(respostaLoginDTO);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "login": "user",
                            "senha": "senha"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-gerado"));
    }

    @Test
    @DisplayName("Retorno do Forbidden - login incorreto")
    void deveRetornarBadRequestSeLoginForIncorreto() throws Exception{
        Mockito.when(autenticacaoService.autenticar(Mockito.any()))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Credenciais inválidas"));
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "login": "usuario_invalido",
                        "senha": "senha_errada"
                        }"""))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Registro de Usuario")
    void deveRegistrarUsuarioComSucesso() throws Exception {

        Mockito.doNothing().when(usuarioService).registrarUsuario(Mockito.any());

        mockMvc.perform(post("/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "login": "newuser",
                            "senha": "novaSenha",
                            "funcao": "ADMIN"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Bad Request no registro de usuario existente")
    void deveRetornarBadRequestSeUsuarioJaExistir() throws Exception {
        Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario já cadastrado")).when(usuarioService).registrarUsuario(Mockito.any());

        mockMvc.perform(post("/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "login": "existente",
                            "senha": "senha123",
                            "funcao": "ADMIN"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
}