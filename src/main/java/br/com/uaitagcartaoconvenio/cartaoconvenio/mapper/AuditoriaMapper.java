package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.AuditoriaAntecipacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.AuditoriaDTO;

@Mapper
public interface AuditoriaMapper {
    AuditoriaMapper INSTANCE = Mappers.getMapper(AuditoriaMapper.class);

    @Mapping(source = "antecipacao.idAntecipacao", target = "idAntecipacao" )
    AuditoriaDTO toDto(AuditoriaAntecipacao auditoria);
    
    List<AuditoriaDTO> toListDto(List<AuditoriaAntecipacao> auditorias);
    
    
}