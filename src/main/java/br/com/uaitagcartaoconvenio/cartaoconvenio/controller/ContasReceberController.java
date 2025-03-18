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
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.ContasReceberService;

@Controller
public class ContasReceberController {

	
	@Autowired
	private ContasReceberService contasReceberService;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getContasReceberByAnoMes/{anoMes}")
	public ResponseEntity<List<ContasReceber>> getContasReceberByAnoMes( @PathVariable("anoMes") String anoMes ) throws ExceptionCustomizada{

		List<ContasReceber> listaContasReceber = contasReceberService.getContasReceberByAnoMes( anoMes );
		
		if(listaContasReceber == null) {
			throw new ExceptionCustomizada("Não existe Contas a Receber para o período: " + anoMes );
		}
		return new ResponseEntity<List<ContasReceber>>(listaContasReceber, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getContasReceberByDtCriacao/{dtCriacaoIni}/{dtCriacaoFim}")
	public ResponseEntity<List<ContasReceber>> getContasReceberByDtCriacao( @PathVariable("dtCriacaoIni") String dtCriacaoIni ,
			                                                                            @PathVariable("dtCriacaoFim") String dtCriacaoFim) throws ExceptionCustomizada{

		List<ContasReceber> listaContasReceber = contasReceberService.getContasReceberByDtCriacao( dtCriacaoIni, dtCriacaoFim );
		
		if(listaContasReceber == null) {
			throw new ExceptionCustomizada("Não existe Contas a Receber para o período entre: " + dtCriacaoIni + " e " + dtCriacaoFim);
		}
		return new ResponseEntity<List<ContasReceber>>(listaContasReceber, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getContasReceberByDescStatusReceber/{status}")
	public ResponseEntity<List<ContasReceber>> getContasReceberByDescStatusReceber( @PathVariable("status") String status) throws ExceptionCustomizada{

		StatusReceber statusContaReceber = StatusReceber.valueOf(status);
		
		List<ContasReceber> listaContasReceber = contasReceberService.getContasReceberByDescStatusReceber( statusContaReceber );
		
		if(listaContasReceber == null) {
			throw new ExceptionCustomizada("Não existe Contas a Receber para o Status: " + StatusCicloPgVenda.valueOf(status).getDescStatusReceber() );
		}
		return new ResponseEntity<List<ContasReceber>>(listaContasReceber, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getCicloPagamentoVendaByIdConveniados/{idEntidade}")
	public ResponseEntity<ContasReceber> getCicloPagamentoVendaByIdConveniados( @PathVariable("idEntidade") Long idEntidade ) throws ExceptionCustomizada{

		ContasReceber contasReceber = contasReceberService.getCicloPagamentoVendaByIdConveniados( idEntidade );
		
		if(contasReceber == null) {
			throw new ExceptionCustomizada("Não existe Contas a Receber para a ID da Entidade: " + idEntidade );
		}
		return new ResponseEntity<ContasReceber>(contasReceber, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getContasReceberByNomeEntidade/{nomeEntidade}")
	public ResponseEntity<List<ContasReceber>> getContasReceberByNomeEntidade( @PathVariable("nomeEntidade") String nomeEntidade) throws ExceptionCustomizada{

		List<ContasReceber> listaCicloPagamentoVenda = contasReceberService.getContasReceberByNomeEntidade( nomeEntidade );
		
		if(listaCicloPagamentoVenda == null) {
			throw new ExceptionCustomizada("Não existe Contas a Receber para a Entidade: " + nomeEntidade );
		}
		return new ResponseEntity<List<ContasReceber>>(listaCicloPagamentoVenda, HttpStatus.OK);		
	}

}
