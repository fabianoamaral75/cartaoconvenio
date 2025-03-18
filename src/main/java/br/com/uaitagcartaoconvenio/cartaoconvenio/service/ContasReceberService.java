package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ContasReceberRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;

@Service
public class ContasReceberService {

	@Autowired
	private ContasReceberRepository contasReceberRepository;

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<ContasReceber> getContasReceberByAnoMes( String anoMes )  {
		
		List<ContasReceber> listaContasReceber = contasReceberRepository.listaContasReceberByAnoMes( anoMes );
		
		return listaContasReceber;		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<ContasReceber> getContasReceberByDtCriacao( String dtCriacaoIni, String dtCriacaoFim )  {
		
		String dtCriacaoIniFormat =  FuncoesUteis.validarEConverterData(dtCriacaoIni, "00:00:00");
		String dtCriacaoFimFormat =  FuncoesUteis.validarEConverterData(dtCriacaoFim, "23:59:59");
		
		List<ContasReceber> listaContasReceber = contasReceberRepository.listaContasReceberByDtCriacao( dtCriacaoIniFormat, dtCriacaoFimFormat );
		
		return listaContasReceber;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<ContasReceber> getContasReceberByDescStatusReceber( StatusReceber descStatusReceber )  {
		
		List<ContasReceber> listaContasReceber = contasReceberRepository.listaContasReceberByDescStatusReceber( descStatusReceber );
		
		return listaContasReceber;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public ContasReceber getCicloPagamentoVendaByIdConveniados( Long idEntidade )  {
		
		ContasReceber contasReceber = contasReceberRepository.listaContasReceberByIdEntidade( idEntidade );
		
		return contasReceber;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<ContasReceber> getContasReceberByNomeEntidade( String nomeEntidade )  {
		
		List<ContasReceber> listaContasReceber = contasReceberRepository.listaContasReceberByNomeEntidade( nomeEntidade );
		
		return listaContasReceber;
		
	}
	
}
