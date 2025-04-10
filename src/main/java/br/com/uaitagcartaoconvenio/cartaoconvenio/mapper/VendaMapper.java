package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Venda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.VendaDTO;

@Mapper( componentModel = "spring", uses = {ConveniadosMapper.class, TaxaConveniadosMapper.class, TaxaEntidadeMapper.class, CartaoMapper.class, ItensVendaMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE )
public interface VendaMapper {
    VendaMapper INSTANCE = Mappers.getMapper(VendaMapper.class);

    VendaDTO toDto(Venda venda);
    List<VendaDTO> toListDto(List<Venda> listVenda);
    Venda toEntity(VendaDTO vendaDTO);
}