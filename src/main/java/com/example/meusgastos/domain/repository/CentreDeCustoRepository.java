package com.example.meusgastos.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.meusgastos.domain.model.CentroDeCusto;
import com.example.meusgastos.domain.model.Usuario;

public interface CentreDeCustoRepository extends JpaRepository<CentroDeCusto, Long> {
    
    List<CentroDeCusto> findByUsuario(Usuario usuario);
}
