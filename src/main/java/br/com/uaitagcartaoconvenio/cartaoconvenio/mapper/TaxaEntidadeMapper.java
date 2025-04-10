package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaEntidadeDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaxaEntidadeMapper {
    TaxaEntidadeMapper INSTANCE = Mappers.getMapper(TaxaEntidadeMapper.class);

    TaxaEntidadeDTO toDto(TaxaEntidade taxaEntidade);
    List<TaxaEntidadeDTO> toListDto(List<TaxaEntidade> listTaxaEntidade);
    TaxaEntidade toEntity(TaxaEntidadeDTO taxaEntidadeDTO);
}
