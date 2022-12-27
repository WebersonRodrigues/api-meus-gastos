package com.example.meusgastos.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.meusgastos.domain.excption.ResourceBadRequestException;
import com.example.meusgastos.domain.excption.ResourceNotFoundException;
import com.example.meusgastos.domain.model.Titulo;
import com.example.meusgastos.domain.model.Usuario;
import com.example.meusgastos.domain.repository.TituloRepository;
import com.example.meusgastos.dto.titulo.TituloRequestDto;
import com.example.meusgastos.dto.titulo.TituloResponseDto;

@Service
public class TituloService implements ICRUDService<TituloRequestDto, TituloResponseDto>{

    @Autowired
    private TituloRepository tituloRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public List<TituloResponseDto> obterTodos() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Titulo> titulos = tituloRepository.findByUsuario(usuario);

        return titulos.stream()
                .map(titulo -> mapper.map(titulo, TituloResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TituloResponseDto obterPorId(Long id) {
        Optional<Titulo> optTitulo = tituloRepository.findById(id);
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(optTitulo.isEmpty() || optTitulo.get().getUsuario().getId() != usuario.getId()){
            throw new ResourceNotFoundException("Não foi possível encontrar o titulo com id " + id);
        }

        return mapper.map(optTitulo.get(), TituloResponseDto.class);
    }

    @Override
    public TituloResponseDto cadastrar(TituloRequestDto dto) {

        validarTitulo(dto);

        Titulo titulo = mapper.map(dto, Titulo.class);

        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        titulo.setUsuario(usuario);
        titulo.setId(null);
        titulo.setDataCadastro(new Date());
        titulo = tituloRepository.save(titulo);

        return mapper.map(titulo, TituloResponseDto.class);
    }

    @Override
    public TituloResponseDto atualizar(Long id, TituloRequestDto dto) {

        obterPorId(id);
        validarTitulo(dto);

        Titulo titulo = mapper.map(dto, Titulo.class);

        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        titulo.setUsuario(usuario);
        titulo.setId(id);
        titulo = tituloRepository.save(titulo);

        return mapper.map(titulo, TituloResponseDto.class);
    }

    @Override
    public void deletar(Long id) {
        obterPorId(id);
        tituloRepository.deleteById(id);
    }

    public List<TituloResponseDto> obterPorDataDeVencimento(String periodoInicial, String periodoFinal){
        List<Titulo> titulos = tituloRepository.obterFluxoCaixaPorDataVencimento(periodoInicial, periodoFinal);
        
        return titulos.stream()
        .map(titulo -> mapper.map(titulo, TituloResponseDto.class))
        .collect(Collectors.toList());
    }

    private void validarTitulo(TituloRequestDto dto){

        if(dto.getTipo() == null ||
           dto.getDataVencimento() == null ||
           dto.getValor() == null ||
           dto.getDescricao() == null){

            throw new ResourceBadRequestException("Os campos Tipo, Data de vencimento, Valor e Descrição são obrigatórios.");
        }
    }


    
}
