
package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.FechamentoConvItensVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.FechamentoConvItensVendasDTO;

@Mapper(componentModel = "spring", uses = {CicloPagamentoVendaInterfaceMapper.class, VendaMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FechamentoConvItensVendasMapper {
	FechamentoConvItensVendasMapper INSTANCE = Mappers.getMapper(FechamentoConvItensVendasMapper.class);
	
	FechamentoConvItensVendasDTO toDto(FechamentoConvItensVendas cicloPagamentoVenda);
	List<FechamentoConvItensVendasDTO> toListDto(List<FechamentoConvItensVendas> listFechamentoConvItensVendas); 
	FechamentoConvItensVendas toEntity(FechamentoConvItensVendasDTO fechamentoConvItensVendasDTO);
}

