package com.example.meusgastos.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.meusgastos.domain.Enum.ETipoTitulo;
import com.example.meusgastos.dto.dashboard.DashboardResponseDto;
import com.example.meusgastos.dto.titulo.TituloResponseDto;

@Service
public class DashboardService {
    
    @Autowired
    private TituloService tituloService;

    public DashboardResponseDto obterFluxoDeCaixa(String periodoInicial, String periodoFinal){

        List<TituloResponseDto> titulos = tituloService.obterPorDataDeVencimento(periodoInicial, periodoFinal);
        
        Double totalApagar = 0.0;
        Double totalAreceber = 0.0;
        List<TituloResponseDto> titulosApagar = new ArrayList<>();
        List<TituloResponseDto> titulosAreceber = new ArrayList<>();
        Double saldo = 0.0;

        for(TituloResponseDto titulo : titulos){

            if(titulo.getTipo() == ETipoTitulo.APAGAR){
                totalApagar += titulo.getValor();
                titulosApagar.add(titulo);
            }else{
                totalAreceber += titulo.getValor();
                titulosAreceber.add(titulo);
            }
        }

        saldo = totalAreceber - totalApagar;

        return new DashboardResponseDto(totalApagar, totalAreceber, saldo, titulosApagar, titulosAreceber);
    }
}
