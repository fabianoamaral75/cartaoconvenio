package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PeriodoCobrancaTaxa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PeriodoCobrancaTaxaDTO;

@Mapper(componentModel = "spring")
public interface PeriodoCobrancaTaxaMapper {

    PeriodoCobrancaTaxaMapper INSTANCE = Mappers.getMapper(PeriodoCobrancaTaxaMapper.class);

    @Mapping(target = "tipoPeriodoId", source = "tipoPeriodo.id")
    @Mapping(target = "taxaExtraConveniadaId", source = "taxaExtraConveniada.id")
    PeriodoCobrancaTaxaDTO toDTO(PeriodoCobrancaTaxa entity);

    @Mapping(target = "tipoPeriodo", ignore = true)
    @Mapping(target = "taxaExtraConveniada", ignore = true)
    PeriodoCobrancaTaxa toEntity(PeriodoCobrancaTaxaDTO dto);

    @Named("mapTaxasExtrasToIds")
    default List<Long> mapTaxasExtrasToIds(List<TaxaExtraConveniada> taxasExtras) {
        if (taxasExtras == null) {
            return null;
        }
        return taxasExtras.stream()
                .map(TaxaExtraConveniada::getId)
                .collect(Collectors.toList());
    }
    
    default PeriodoCobrancaTaxa fromId(Long id) {
        if (id == null) {
            return null;
        }
        PeriodoCobrancaTaxa periodo = new PeriodoCobrancaTaxa();
        periodo.setId(id);
        return periodo;
    }
}