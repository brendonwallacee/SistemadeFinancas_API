package com.example.api.services;

import com.example.api.domain.usuario.FuncaoUsuario;
import com.example.api.domain.usuario.Usuario;
import com.example.api.repositories.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan("com.example.api.domain.usuario")
@Import(AutorizacaoService.class)
@ActiveProfiles("test")
class AutorizacaoServiceTest {

    @Autowired
    AutorizacaoService autorizacaoService;
    @Autowired
    UsuarioRepository usuarioRepository;


    @Test
    @DisplayName("Carrega dados do usuario pelo login")
    void deveCarregarUsuarioPorLogin(){
        Usuario usuario = new Usuario("teste", "senha1234", FuncaoUsuario.ADMIN);
        usuarioRepository.save(usuario);

        UserDetails userDetails = autorizacaoService.loadUserByUsername("teste");

        assertNotNull(userDetails);
        assertEquals("teste",
                userDetails.getUsername());

    }

    @Test
    @DisplayName("Exceção se login não existe")
    void deveLancarExcecaoQuandoLoginNaoExiste(){
        assertThrows(UsernameNotFoundException.class, () -> {
            autorizacaoService.loadUserByUsername("nao existe");
        });
    }
}