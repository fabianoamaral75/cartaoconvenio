package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import java.util.List;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PeriodoCobrancaTaxa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaExtraEntidadeDTO;

@Mapper(componentModel = "spring", 
        uses = {ItemTaxaExtraEntidadeMapper.class, PeriodoCobrancaTaxaMapper.class})
public interface TaxaExtraEntidadeMapper {
    TaxaExtraEntidadeMapper INSTANCE = Mappers.getMapper(TaxaExtraEntidadeMapper.class);

 //   @Mapping(target = "periodoCobrancaTaxa", source = "periodoCobrancaTaxa")
    @Mapping(target = "entidadeId", source = "entidade.idEntidade")
    TaxaExtraEntidadeDTO toDto(TaxaExtraEntidade entity);

    // Adicionando método para lista
    List<TaxaExtraEntidadeDTO> toListDto(List<TaxaExtraEntidade> entities);

    @Mapping(target = "periodoCobrancaTaxa", ignore = true)
    @Mapping(target = "entidade", ignore = true)
    @Mapping(target = "itensContasReceber", ignore = true)
    TaxaExtraEntidade toEntity(TaxaExtraEntidadeDTO dto);

    @AfterMapping
    default void afterToEntity(@MappingTarget TaxaExtraEntidade entity, TaxaExtraEntidadeDTO dto) {
        if (dto.getPeriodoCobrancaTaxa() != null && dto.getPeriodoCobrancaTaxa().getId() != null) {
            PeriodoCobrancaTaxa periodo = new PeriodoCobrancaTaxa();
            periodo.setId(dto.getPeriodoCobrancaTaxa().getId());
            entity.setPeriodoCobrancaTaxa(periodo);
        }
        
        if (dto.getEntidadeId() != null) {
            Entidade entidade = new Entidade();
            entidade.setIdEntidade(dto.getEntidadeId());
            entity.setEntidade(entidade);
        }
    }
}