package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.*;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.AntecipacaoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.AntecipacaoVendaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.VendaRepository;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", 
uses = {ConveniadosMapper.class, CicloPagamentoVendaMapper.class},
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AntecipacaoMapper {

	@Autowired
	protected AntecipacaoVendaRepository antecipacaoVendaRepository;
	
	@Autowired
	protected VendaRepository vendaRepository;
	
	@Mapping(target = "idConveniados", source = "conveniados.idConveniados")
	@Mapping(target = "idCicloPagamentoVenda", source = "cicloPagamentoVenda.idCicloPagamentoVenda")
	@Mapping(target = "idsVendas", source = "vendas", qualifiedByName = "mapVendasToIds")
	public abstract AntecipacaoDTO toDTO(Antecipacao antecipacao);
	
	@Mapping(target = "conveniados", source = "idConveniados")
	@Mapping(target = "cicloPagamentoVenda", source = "idCicloPagamentoVenda")
	@Mapping(target = "vendas", source = "idsVendas", qualifiedByName = "idsToAntecipacaoVendas")
	public abstract Antecipacao toEntity(AntecipacaoDTO antecipacaoDTO);
	
	@Named("mapVendasToIds")
	protected List<Long> mapVendasToIds(List<AntecipacaoVenda> vendas) {
		if (vendas == null) {
		    return null;
		}
		return vendas.stream()
		        .map(AntecipacaoVenda::getIdVenda)
		        .collect(Collectors.toList());
	}
	
	@Named("idsToAntecipacaoVendas")
	protected List<AntecipacaoVenda> mapIdsToAntecipacaoVendas(List<Long> idsVendas) {  // Removido @Context
		if (idsVendas == null) {
		    return null;
	}
	
	return idsVendas.stream()
	        .map(idVenda -> {
	            Venda venda = vendaRepository.findById(idVenda)
	                    .orElseThrow(() -> new RuntimeException("Venda não encontrada com ID: " + idVenda));
	            
	            AntecipacaoVenda av = new AntecipacaoVenda();
	            av.setVenda(venda);
	            // av.setAntecipacao(antecipacao); // Removido porque não temos a antecipação aqui
	            return av;
	        })
	        .collect(Collectors.toList());
	}
	
	// Métodos para mapeamento de IDs para entidades (mantidos iguais)
	protected Conveniados mapConveniados(Long idConveniados) {
		if (idConveniados == null) {
		    return null;
		}
		Conveniados conveniados = new Conveniados();
		conveniados.setIdConveniados(idConveniados);
		return conveniados;
	}
	
	protected CicloPagamentoVenda mapCicloPagamentoVenda(Long idCicloPagamentoVenda) {
		if (idCicloPagamentoVenda == null) {
		    return null;
		}
		CicloPagamentoVenda ciclo = new CicloPagamentoVenda();
		ciclo.setIdCicloPagamentoVenda(idCicloPagamentoVenda);
		return ciclo;
	}
}




