package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.VigenciaContratoEntidadeDTO;

@Mapper(componentModel = "spring")
public interface VigenciaContratoMapper {

    VigenciaContratoMapper INSTANCE = Mappers.getMapper(VigenciaContratoMapper.class);

    VigenciaContratoEntidadeDTO toDTO(VigenciaContratoEntidade entity);
    
    @Mapping(target = "contratoEntidade", ignore = true)
    VigenciaContratoEntidade toEntity(VigenciaContratoEntidadeDTO dto);
}
