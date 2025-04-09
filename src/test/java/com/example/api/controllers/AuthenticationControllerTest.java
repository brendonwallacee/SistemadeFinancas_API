package com.example.api.controllers;

import com.example.api.domain.usuario.AuthenticationDTO;
import com.example.api.domain.usuario.FuncaoUsuario;
import com.example.api.domain.usuario.RegisterDTO;
import com.example.api.domain.usuario.Usuario;
import com.example.api.infra.security.SecurityConfiguration;
import com.example.api.infra.security.TokenService;
import com.example.api.repositories.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static
        org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static
        org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(AuthenticationController.class)
@Import(SecurityConfiguration.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private TokenService tokenService;

    @Test
    @DisplayName("Retorno de token com login certo")
    void deveRetornarTokenAoFazerLoginComCredenciaisValidas() throws Exception {
        AuthenticationDTO authDTO = new AuthenticationDTO("user", "senha");

        Usuario usuario = new Usuario("user", "senhaCriptografada", FuncaoUsuario.ADMIN);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(usuario, null);

        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenReturn(authToken);
        Mockito.when(tokenService.generateToken(Mockito.any()))
                .thenReturn("token-gerado");

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
    @DisplayName("Bad Request no login incorreto")
    void deveRetornarBadRequestSeLoginForIncorreto(){}

    @Test
    @DisplayName("Registro de Usuario")
    void deveRegistrarUsuarioComSucesso() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO("newuser", "novaSenha", FuncaoUsuario.ADMIN);

        Mockito.when(usuarioRepository.findByLogin("newuser"))
                .thenReturn(null);

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
        Mockito.when(usuarioRepository.findByLogin("existente"))
                .thenReturn(new Usuario());

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