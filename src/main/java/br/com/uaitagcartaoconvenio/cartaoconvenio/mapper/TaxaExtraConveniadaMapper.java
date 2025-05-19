package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaExtraConveniadaDTO;

@Mapper(componentModel = "spring")
public interface TaxaExtraConveniadaMapper {
    TaxaExtraConveniadaMapper INSTANCE = Mappers.getMapper(TaxaExtraConveniadaMapper.class);

    @Mapping(target = "conveniadosId", source = "conveniados.idConveniados")
    @Mapping(target = "periodoCobrancaTaxaId", source = "periodoCobrancaTaxa.id")
    TaxaExtraConveniadaDTO toDTO(TaxaExtraConveniada entity);

    @Mapping(target = "conveniados", ignore = true)
    @Mapping(target = "periodoCobrancaTaxa", ignore = true)
    TaxaExtraConveniada toEntity(TaxaExtraConveniadaDTO dto);
}