// EmprestimoMapper.java
package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Emprestimo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PrestacaoEmprestimo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.EmprestimoRequestDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.EmprestimoResponseDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PrestacaoResponseDTO;

@Mapper(componentModel = "spring")
public interface EmprestimoMapper {

    EmprestimoMapper INSTANCE = Mappers.getMapper(EmprestimoMapper.class);
    
    @Mapping(target = "idEmprestimo" , ignore = true)
    @Mapping(target = "status"       , ignore = true)
    @Mapping(target = "dtSolicitacao", ignore = true)
    @Mapping(target = "dtAprovacao"  , ignore = true)
    @Mapping(target = "dtQuitacao"   , ignore = true)
    @Mapping(target = "prestacoes"   , ignore = true)
    @Mapping(target = "funcionario"  , ignore = true)
    Emprestimo toEntity(EmprestimoRequestDTO dto);
    
    EmprestimoResponseDTO toDto(Emprestimo entity);
    
    List<EmprestimoResponseDTO> toDtoList(List<Emprestimo> entities);
    
    PrestacaoResponseDTO toPrestacaoDto(PrestacaoEmprestimo entity);
    
    List<PrestacaoResponseDTO> toPrestacaoDtoList(List<PrestacaoEmprestimo> entities);
}