package com.example.api.controllers;

import com.example.api.domain.usuario.dto.AutenticacaoDTO;
import com.example.api.domain.usuario.dto.RegistrarUsuarioDTO;
import com.example.api.domain.usuario.entidade.FuncaoUsuario;
import com.example.api.domain.usuario.entidade.Usuario;
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
        AutenticacaoDTO authDTO = new AutenticacaoDTO("user", "senha");

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
    @DisplayName("Retorno do Forbidden - login incorreto")
    void deveRetornarBadRequestSeLoginForIncorreto() throws Exception{
        Mockito.when(authenticationManager.authenticate(Mockito.any()))
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
        RegistrarUsuarioDTO RegistrarUsuario_DTO = new RegistrarUsuarioDTO("newuser", "novaSenha", FuncaoUsuario.ADMIN);

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