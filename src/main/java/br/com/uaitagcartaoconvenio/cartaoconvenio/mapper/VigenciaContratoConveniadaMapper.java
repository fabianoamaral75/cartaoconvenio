package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.VigenciaContratoConveniadaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VigenciaContratoConveniadaMapper {
    VigenciaContratoConveniadaMapper INSTANCE = Mappers.getMapper(VigenciaContratoConveniadaMapper.class);

    @Mapping(target = "contratoConveniadoId", source = "contratoConveniado.idContratoConveniado")
    VigenciaContratoConveniadaDTO toDTO(VigenciaContratoConveniada entity);

    @Mapping(target = "contratoConveniado", ignore = true) // O contrato será tratado separadamente
    VigenciaContratoConveniada toEntity(VigenciaContratoConveniadaDTO dto);
}