package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;


import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloTaxaExtra;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaExtraConveniadaDTO;

@Mapper(componentModel = "spring", uses = PeriodoCobrancaTaxaMapper.class)
public interface TaxaExtraConveniadaMapper {
    TaxaExtraConveniadaMapper INSTANCE = Mappers.getMapper(TaxaExtraConveniadaMapper.class);

    @Mapping(target = "conveniadosId", source = "conveniados.idConveniados")
    @Mapping(target = "periodoCobrancaTaxa", source = "periodoCobrancaTaxa")
    @Mapping(target = "ciclosPagamentoIds", source = "ciclosPagamento", qualifiedByName = "mapCiclosPagamento")
    TaxaExtraConveniadaDTO toDTO(TaxaExtraConveniada entity);

    @Mapping(target = "conveniados", ignore = true)
    @Mapping(target = "periodoCobrancaTaxa", ignore = true)
    @Mapping(target = "ciclosPagamento", ignore = true)
    TaxaExtraConveniada toEntity(TaxaExtraConveniadaDTO dto);

    List<TaxaExtraConveniadaDTO> toListDTO(List<TaxaExtraConveniada> taxas);

    @Named("mapCiclosPagamento")
    default List<Long> mapCiclosPagamento(List<CicloTaxaExtra> ciclos) {
        if (ciclos == null) {
            return null;
        }
        return ciclos.stream()
                .map(CicloTaxaExtra::getId)
                .collect(Collectors.toList());
    }
}



