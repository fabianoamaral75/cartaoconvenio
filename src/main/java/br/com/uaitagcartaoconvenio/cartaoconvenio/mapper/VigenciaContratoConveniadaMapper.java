package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.VigenciaContratoConveniadaDTO;

@Mapper(componentModel = "spring")
public interface VigenciaContratoConveniadaMapper {
    VigenciaContratoConveniadaMapper INSTANCE = Mappers.getMapper(VigenciaContratoConveniadaMapper.class);

    @Mapping(target = "contratoConveniadoId", source = "contratoConveniado.idContratoConveniado")
    VigenciaContratoConveniadaDTO toDTO(VigenciaContratoConveniada entity);

    @Mapping(target = "contratoConveniado", ignore = true)
    VigenciaContratoConveniada toEntity(VigenciaContratoConveniadaDTO dto);
}
