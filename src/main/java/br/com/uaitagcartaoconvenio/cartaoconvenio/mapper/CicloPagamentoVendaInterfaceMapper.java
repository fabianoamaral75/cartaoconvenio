package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.CicloPagamentoVendaDTO;

@Mapper(uses = {TaxaConveniadosMapper.class, ConveniadosMapper.class, FechamentoConvItensVendasMapper.class})
public interface CicloPagamentoVendaInterfaceMapper {

	CicloPagamentoVendaInterfaceMapper INSTANCE = Mappers.getMapper(CicloPagamentoVendaInterfaceMapper.class);
	
	CicloPagamentoVendaDTO toDto(CicloPagamentoVenda cicloPagamentoVenda);
	List<CicloPagamentoVendaDTO> toListDto(List<CicloPagamentoVenda> listCicloPagamentoVenda); 
	CicloPagamentoVenda toEntity(CicloPagamentoVendaDTO cicloPagamentoVendaDTO);
	
}


