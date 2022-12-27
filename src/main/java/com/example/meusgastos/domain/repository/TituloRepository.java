package com.example.meusgastos.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.meusgastos.domain.model.Titulo;
import com.example.meusgastos.domain.model.Usuario;

public interface TituloRepository extends JpaRepository<Titulo, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM public.titulo " +
            "WHERE data_vencimento " +
            "BETWEEN TO_TIMESTAMP(:periodoInicial, 'YYYY-MM-DD hh24:MI:SS') AND " +
            "TO_TIMESTAMP(:periodoFinal, 'YYYY-MM-DD hh24:MI:SS')")
    List<Titulo> obterFluxoCaixaPorDataVencimento(
        @Param("periodoInicial") String periodoInicial,
        @Param("periodoFinal") String periodoFinal);

    List<Titulo> findByUsuario(Usuario usuario);
}
