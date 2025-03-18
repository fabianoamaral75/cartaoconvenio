package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.CicloPagamentoVendaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;

@Service
public class CicloPagamentoVendaService {

	@Autowired
	private CicloPagamentoVendaRepository cicloPagamentoVendaRepository;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<CicloPagamentoVenda> getCicloPagamentoVendaByAnoMes( String anoMes )  {
		
		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaRepository.listaCicloPagamentoVendaByAnoMes( anoMes );
		
		return listaCicloPagamentoVenda;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<CicloPagamentoVenda> getCicloPagamentoVendaByDtCriacao( String dtCriacaoIni, String dtCriacaoFim )  {
		
		String dtCriacaoIniFormat =  FuncoesUteis.validarEConverterData(dtCriacaoIni, "00:00:00");
		String dtCriacaoFimFormat =  FuncoesUteis.validarEConverterData(dtCriacaoFim, "23:59:59");
		
		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaRepository.listaCicloPagamentoVendaByDtCriacao( dtCriacaoIniFormat, dtCriacaoFimFormat );
		
		return listaCicloPagamentoVenda;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<CicloPagamentoVenda> getCicloPagamentoVendaByDescStatusPagamento( StatusCicloPgVenda descStatusPagamento )  {
		
		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaRepository.listaCicloPagamentoVendaByDescStatusPagamento( descStatusPagamento );
		
		return listaCicloPagamentoVenda;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public CicloPagamentoVenda getCicloPagamentoVendaByIdConveniados( Long idConveniados )  {
		
		CicloPagamentoVenda listaCicloPagamentoVenda = cicloPagamentoVendaRepository.listaCicloPagamentoVendaByIdConveniados( idConveniados );
		
		return listaCicloPagamentoVenda;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<CicloPagamentoVenda> getCicloPagamentoVendaByNomeConveniado( String nomeConveniado )  {
		
		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaRepository.listaCicloPagamentoVendaByNomeConveniado( nomeConveniado );
		
		return listaCicloPagamentoVenda;
		
	}

	
}
