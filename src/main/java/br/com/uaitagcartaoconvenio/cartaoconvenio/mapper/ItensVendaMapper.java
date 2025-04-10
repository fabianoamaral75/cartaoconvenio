package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ItensVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ItensVendaDTO;

@Mapper(componentModel = "spring", uses = {ProdutoMapper.class, VendaMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItensVendaMapper {
    ItensVendaMapper INSTANCE = Mappers.getMapper(ItensVendaMapper.class);

    ItensVendaDTO toDto(ItensVenda itensVenda);
    ItensVenda toEntity(ItensVendaDTO itensVendaDTO);
}
