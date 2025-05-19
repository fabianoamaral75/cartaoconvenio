package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PeriodoCobrancaTaxa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaExtraEntidadeDTO;

@Mapper(componentModel = "spring", uses = {ItemTaxaExtraEntidadeMapper.class})
public interface TaxaExtraEntidadeMapper {

    @Mapping(target = "periodoCobrancaTaxaId", source = "periodoCobrancaTaxa.id")
    @Mapping(target = "entidadeId", source = "entidade.idEntidade")
    TaxaExtraEntidadeDTO toDTO(TaxaExtraEntidade entity);

    @Mapping(target = "periodoCobrancaTaxa", ignore = true)
    @Mapping(target = "entidade", ignore = true)
    @Mapping(target = "itensContasReceber", ignore = true)
    TaxaExtraEntidade toEntity(TaxaExtraEntidadeDTO dto);

    @AfterMapping
    default void afterToEntity(@MappingTarget TaxaExtraEntidade entity, TaxaExtraEntidadeDTO dto) {
        if (dto.getPeriodoCobrancaTaxaId() != null) {
            PeriodoCobrancaTaxa periodo = new PeriodoCobrancaTaxa();
            periodo.setId(dto.getPeriodoCobrancaTaxaId());
            entity.setPeriodoCobrancaTaxa(periodo);
        }
        
        if (dto.getEntidadeId() != null) {
            Entidade entidade = new Entidade();
            entidade.setIdEntidade(dto.getEntidadeId());
            entity.setEntidade(entidade);
        }
    }
}