/*

package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.*;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaxasFaixaVendasMapper {

    TaxasFaixaVendasDTO toDto(TaxasFaixaVendas entity);
    
    TaxasFaixaVendas toEntity(TaxasFaixaVendasDTO dto);
    
    List<TaxasFaixaVendasDTO> toListDto(List<TaxasFaixaVendas> entities);
    
    ItemTaxaExtraConveniadaDTO itemToDto(ItemTaxaExtraConveniada item);
    
    ItemTaxaExtraConveniada itemToEntity(ItemTaxaExtraConveniadaDTO dto);
    
}

*/

package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxasFaixaVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxasFaixaVendasDTO;


@Mapper(componentModel = "spring") 
public interface TaxasFaixaVendasMapper {
    TaxasFaixaVendasDTO toDto(TaxasFaixaVendas entity);
    TaxasFaixaVendas toEntity(TaxasFaixaVendasDTO dto);
    
    default List<TaxasFaixaVendasDTO> toDtoList(List<TaxasFaixaVendas> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }
}
