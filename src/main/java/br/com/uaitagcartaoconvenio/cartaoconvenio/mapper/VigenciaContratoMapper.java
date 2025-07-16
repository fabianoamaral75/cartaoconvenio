package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.VigenciaContratoEntidadeDTO;

@Mapper(componentModel = "spring")
public interface VigenciaContratoMapper {

    VigenciaContratoMapper INSTANCE = Mappers.getMapper(VigenciaContratoMapper.class);

    VigenciaContratoEntidadeDTO toDTO(VigenciaContratoEntidade entity);
    
    @Mapping(target = "contratoEntidade", ignore = true)
    VigenciaContratoEntidade toEntity(VigenciaContratoEntidadeDTO dto);
    
    // Novo método para atualizar uma entidade existente a partir de um DTO
    @Mapping(target = "contratoEntidade", ignore = true)
    @Mapping(target = "idVigenciaContratoEntidade", ignore = true) // Não atualiza o ID
    @Mapping(target = "dtCriacao", ignore = true) // Não atualiza a data de criação
    void updateFromDTO(VigenciaContratoEntidadeDTO dto, @MappingTarget VigenciaContratoEntidade entity);
    
}
