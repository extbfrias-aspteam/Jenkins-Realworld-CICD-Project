package com.asp.eiyu.api.auth.entity;

public record UsuarioAuthDTO(String username, String passwordHash, Integer idPblu) {
}
