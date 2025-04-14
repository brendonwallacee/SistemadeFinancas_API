package com.example.api.domain.usuario.dto;

import jakarta.validation.constraints.Email;

public record RecuperarSenhaDTO(@Email String email){

}

