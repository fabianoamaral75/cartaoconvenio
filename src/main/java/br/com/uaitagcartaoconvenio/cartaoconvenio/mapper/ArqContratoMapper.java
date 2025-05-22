package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ArqContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ArqContratoEntidadeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ArqContratoMapper {

    ArqContratoMapper INSTANCE = Mappers.getMapper(ArqContratoMapper.class);

    ArqContratoEntidadeDTO toDTO(ArqContratoEntidade entity);

    @Mapping(target = "conteudoBase64", ignore = true) // Ignorado por padrão por questões de performance
    @Mapping(target = "contratoEntidade", ignore = true)
    ArqContratoEntidade toEntity(ArqContratoEntidadeDTO dto);
}
