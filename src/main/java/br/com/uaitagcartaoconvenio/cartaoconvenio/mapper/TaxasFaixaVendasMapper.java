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
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxasFaixaVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.CicloPagamentoVendaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxasFaixaVendasDTO;

@Mapper
public interface TaxasFaixaVendasMapper {
    TaxasFaixaVendasMapper INSTANCE = Mappers.getMapper(TaxasFaixaVendasMapper.class);

    @Mapping(target = "ciclosPagamento", ignore = true) // Ignora a lista circular
    TaxasFaixaVendasDTO toDto(TaxasFaixaVendas entity);

    @Mapping(target = "ciclosPagamento", ignore = true) // Ignora a lista circular
    TaxasFaixaVendas toEntity(TaxasFaixaVendasDTO dto);

    // Método para mapeamento completo quando necessário
    default TaxasFaixaVendasDTO toDtoWithCiclos(TaxasFaixaVendas entity, CicloPagamentoVendaMapper cicloMapper) {
        TaxasFaixaVendasDTO dto = toDto(entity);
        if (entity.getCiclosPagamento() != null) {
            dto.setCiclosPagamento(
                entity.getCiclosPagamento().stream()
                    .map(ciclo -> {
                        CicloPagamentoVendaDTO cicloDto = cicloMapper.toDTO(ciclo);
                        cicloDto.setTaxasFaixaVendas(null); // Quebra a circularidade
                        return cicloDto;
                    })
                    .collect(Collectors.toList())
            );
        }
        return dto;
    }
    
        default List<TaxasFaixaVendasDTO> toDtoList(List<TaxasFaixaVendas> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}

