package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveiniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaConveniadosDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaxaConveniadosMapper {
    TaxaConveniadosMapper INSTANCE = Mappers.getMapper(TaxaConveniadosMapper.class);

    TaxaConveniadosDTO toDto(TaxaConveiniados taxaConveiniados);
    List<TaxaConveniadosDTO> toListDto(List<TaxaConveiniados> listTaxaConveiniados);
    TaxaConveiniados toEntity(TaxaConveniadosDTO taxaConveniadosDTO);
}
