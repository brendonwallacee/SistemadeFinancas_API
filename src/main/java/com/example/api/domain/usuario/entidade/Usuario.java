package com.example.api.domain.usuario.entidade;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Entity(name = "usuarios")
@Table(name = "usuarios")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    private String login;
    private String senha;

    @Enumerated(EnumType.STRING)
    private FuncaoUsuario funcao;


    public Usuario( String login, String senha, FuncaoUsuario funcao, String email) {
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.funcao = funcao;
    }

    public Usuario( String login, String senha, FuncaoUsuario funcao) {
        this.login = login;
        this.senha = senha;
        this.funcao = funcao;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.funcao == FuncaoUsuario.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
