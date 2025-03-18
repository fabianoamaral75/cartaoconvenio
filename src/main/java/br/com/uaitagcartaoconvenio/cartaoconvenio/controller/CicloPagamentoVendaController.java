package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.CicloPagamentoVendaService;

@Controller
public class CicloPagamentoVendaController {

	@Autowired
	private CicloPagamentoVendaService cicloPagamentoVendaService;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getCicloPagamentoVendaByAnoMes/{anoMes}")
	public ResponseEntity<List<CicloPagamentoVenda>> getCicloPagamentoVendaByAnoMes( @PathVariable("anoMes") String anoMes ) throws ExceptionCustomizada{

		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByAnoMes( anoMes );
		
		if(listaCicloPagamentoVenda == null) {
			throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para o período: " + anoMes );
		}
		return new ResponseEntity<List<CicloPagamentoVenda>>(listaCicloPagamentoVenda, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getCicloPagamentoVendaByDtCriacao/{dtCriacaoIni}/{dtCriacaoFim}")
	public ResponseEntity<List<CicloPagamentoVenda>> getCicloPagamentoVendaByDtCriacao( @PathVariable("dtCriacaoIni") String dtCriacaoIni ,
			                                                                            @PathVariable("dtCriacaoFim") String dtCriacaoFim) throws ExceptionCustomizada{

		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByDtCriacao( dtCriacaoIni, dtCriacaoFim );
		
		if(listaCicloPagamentoVenda == null) {
			throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para o período entre: " + dtCriacaoIni + " e " + dtCriacaoFim);
		}
		return new ResponseEntity<List<CicloPagamentoVenda>>(listaCicloPagamentoVenda, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getCicloPagamentoVendaByDescStatusPagamento/{status}")
	public ResponseEntity<List<CicloPagamentoVenda>> getCicloPagamentoVendaByDescStatusPagamento( @PathVariable("status") String status) throws ExceptionCustomizada{

		StatusCicloPgVenda statusCicloPgVenda = StatusCicloPgVenda.valueOf(status);
		
		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByDescStatusPagamento( statusCicloPgVenda );
		
		if(listaCicloPagamentoVenda == null) {
			throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para o Status: " + StatusCicloPgVenda.valueOf(status).getDescStatusReceber() );
		}
		return new ResponseEntity<List<CicloPagamentoVenda>>(listaCicloPagamentoVenda, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getCicloPagamentoVendaByIdConveniados/{idConveniados}")
	public ResponseEntity<CicloPagamentoVenda> getCicloPagamentoVendaByIdConveniados( @PathVariable("idConveniados") Long idConveniados ) throws ExceptionCustomizada{

		CicloPagamentoVenda cicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByIdConveniados( idConveniados );
		
		if(cicloPagamentoVenda == null) {
			throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para a ID da Conveniada: " + idConveniados );
		}
		return new ResponseEntity<CicloPagamentoVenda>(cicloPagamentoVenda, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getCicloPagamentoVendaByNomeConveniado/{nomeConveniado}")
	public ResponseEntity<List<CicloPagamentoVenda>> getCicloPagamentoVendaByNomeConveniado( @PathVariable("nomeConveniado") String nomeConveniado) throws ExceptionCustomizada{

		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByNomeConveniado( nomeConveniado );
		
		if(listaCicloPagamentoVenda == null) {
			throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para a Conveniada: " + nomeConveniado );
		}
		return new ResponseEntity<List<CicloPagamentoVenda>>(listaCicloPagamentoVenda, HttpStatus.OK);		
	}

}
