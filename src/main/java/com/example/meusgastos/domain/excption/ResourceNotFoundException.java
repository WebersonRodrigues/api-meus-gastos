package com.example.meusgastos.domain.excption;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String mensagem){
        super(mensagem);
    }
}
