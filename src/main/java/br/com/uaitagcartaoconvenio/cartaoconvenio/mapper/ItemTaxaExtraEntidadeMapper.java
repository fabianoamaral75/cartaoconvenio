package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ItemTaxaExtraEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ItemTaxaExtraEntidadeDTO;

@Mapper(componentModel = "spring")
public interface ItemTaxaExtraEntidadeMapper {

    @Mapping(target = "taxaExtraEntidadeId", source = "taxaExtraEntidade.id")
    @Mapping(target = "contasReceberId", source = "contasReceber.idContasReceber")
    ItemTaxaExtraEntidadeDTO toDTO(ItemTaxaExtraEntidade entity);

    // Adicione este método para conversão de lista
    List<ItemTaxaExtraEntidadeDTO> toDTOList(List<ItemTaxaExtraEntidade> entities);

    @Mapping(target = "taxaExtraEntidade", ignore = true)
    @Mapping(target = "contasReceber", ignore = true)
    ItemTaxaExtraEntidade toEntity(ItemTaxaExtraEntidadeDTO dto);

    @AfterMapping
    default void afterToEntity(@MappingTarget ItemTaxaExtraEntidade entity, ItemTaxaExtraEntidadeDTO dto) {
        if (dto.getTaxaExtraEntidadeId() != null) {
            TaxaExtraEntidade taxa = new TaxaExtraEntidade();
            taxa.setId(dto.getTaxaExtraEntidadeId());
            entity.setTaxaExtraEntidade(taxa);
        }
        
        if (dto.getContasReceberId() != null) {
            ContasReceber conta = new ContasReceber();
            conta.setIdContasReceber(dto.getContasReceberId());
            entity.setContasReceber(conta);
        }
    }
}