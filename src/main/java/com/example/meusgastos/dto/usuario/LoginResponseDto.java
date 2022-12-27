package com.example.meusgastos.dto.usuario;

public class LoginResponseDto {
    
    private String token;

    private UsuarioResponseDto usuario;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UsuarioResponseDto getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioResponseDto usuario) {
        this.usuario = usuario;
    }

    
}
