package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloTaxaExtra;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.CicloTaxaExtraDTO;

@Mapper(componentModel = "spring")
public interface CicloTaxaExtraMapper {
    
    @Mapping(target = "cicloPagamentoVendaId", source = "cicloPagamentoVenda.idCicloPagamentoVenda")
    @Mapping(target = "taxaExtraConveniadaId", source = "taxaExtraConveniada.id")
    CicloTaxaExtraDTO toDTO(CicloTaxaExtra entity);
    
    @Mapping(target = "cicloPagamentoVenda", ignore = true)
    @Mapping(target = "taxaExtraConveniada", ignore = true)
    CicloTaxaExtra toEntity(CicloTaxaExtraDTO dto);
    
    List<CicloTaxaExtraDTO> toDTOList(List<CicloTaxaExtra> entities);
}