package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniadaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaConveniadaEntidadeDTO;

@Mapper
public interface TaxaConveniadaEntidadeMapper {
    TaxaConveniadaEntidadeMapper INSTANCE = Mappers.getMapper(TaxaConveniadaEntidadeMapper.class);

    @Mapping(source = "conveniados.idConveniados", target = "idConveniados")
    @Mapping(source = "entidade.idEntidade", target = "idEntidade")
    TaxaConveniadaEntidadeDTO toDto(TaxaConveniadaEntidade taxa);

    @Mapping(target = "conveniados", ignore = true)
    @Mapping(target = "entidade", ignore = true)
    TaxaConveniadaEntidade toEntity(TaxaConveniadaEntidadeDTO dto);
    
    default List<TaxaConveniadaEntidadeDTO> toListDto(List<TaxaConveniadaEntidade> taxas) {
        if (taxas == null) {
            return null;
        }
        return taxas.stream().map(this::toDto).collect(Collectors.toList());
    }
}