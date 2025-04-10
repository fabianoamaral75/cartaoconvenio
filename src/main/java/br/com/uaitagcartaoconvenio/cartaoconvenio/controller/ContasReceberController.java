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
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContasReceberDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.ContasReceberMappingService;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.ContasReceberService;

@Controller
public class ContasReceberController {

	
	@Autowired
	private ContasReceberService contasReceberService;
		
	@Autowired
	private ContasReceberMappingService mappingService; // Injetado pelo Spring

	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getContasReceberByAnoMes/{anoMes}")
	public ResponseEntity<List<ContasReceberDTO>> getContasReceberByAnoMes( @PathVariable("anoMes") String anoMes ) throws ExceptionCustomizada{

		List<ContasReceber> listaContasReceber = contasReceberService.getContasReceberByAnoMes( anoMes );
		
		if(listaContasReceber == null) {
			throw new ExceptionCustomizada("Não existe Contas a Receber para o período: " + anoMes );
		}
		
//		List<ContasReceberDTO> dto = contasReceberMapper.toListDto(listaContasReceber);  
		
		// Para mapeamento básico (sem fechamentos)
		// List<ContasReceberDTO> dtoBasico = contasReceberMapper.toListDto(listaContasReceber);

		// Para mapeamento completo (com fechamentos)
		List<ContasReceberDTO> dtoCompleto = mappingService.mapCompleteList(listaContasReceber);
		
		return new ResponseEntity<List<ContasReceberDTO>>(dtoCompleto, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getContasReceberByDtCriacao/{dtCriacaoIni}/{dtCriacaoFim}")
	public ResponseEntity<List<ContasReceberDTO>> getContasReceberByDtCriacao( @PathVariable("dtCriacaoIni") String dtCriacaoIni ,
			                                                                            @PathVariable("dtCriacaoFim") String dtCriacaoFim) throws ExceptionCustomizada{

		List<ContasReceber> listaContasReceber = contasReceberService.getContasReceberByDtCriacao( dtCriacaoIni, dtCriacaoFim );
		
		if(listaContasReceber == null) {
			throw new ExceptionCustomizada("Não existe Contas a Receber para o período entre: " + dtCriacaoIni + " e " + dtCriacaoFim);
		}
		
		// List<ContasReceberDTO> dto = contasReceberMapper.toListDto(listaContasReceber);  
		
		// Para mapeamento completo (com fechamentos)
		List<ContasReceberDTO> dtoCompleto = mappingService.mapCompleteList(listaContasReceber);

		
		return new ResponseEntity<List<ContasReceberDTO>>(dtoCompleto, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getContasReceberByDescStatusReceber/{status}")
	public ResponseEntity<List<ContasReceberDTO>> getContasReceberByDescStatusReceber( @PathVariable("status") String status) throws ExceptionCustomizada{

		StatusReceber statusContaReceber = StatusReceber.valueOf(status);
		
		List<ContasReceber> listaContasReceber = contasReceberService.getContasReceberByDescStatusReceber( statusContaReceber );
		
		if(listaContasReceber == null) {
			throw new ExceptionCustomizada("Não existe Contas a Receber para o Status: " + StatusCicloPgVenda.valueOf(status).getDescStatusReceber() );
		}
		
		// List<ContasReceberDTO> dto = contasReceberMapper.toListDto(listaContasReceber);
		// Para mapeamento completo (com fechamentos)
		List<ContasReceberDTO> dtoCompleto = mappingService.mapCompleteList(listaContasReceber);

		
		return new ResponseEntity<List<ContasReceberDTO>>(dtoCompleto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getContasReceberVendaByIdConveniados/{idEntidade}")
	public ResponseEntity<List<ContasReceberDTO>> getContasReceberVendaByIdConveniados( @PathVariable("idEntidade") Long idEntidade ) throws ExceptionCustomizada{

		List<ContasReceber> listaContasReceber = contasReceberService.getCicloPagamentoVendaByIdConveniados( idEntidade );
		
		if(listaContasReceber == null) {
			throw new ExceptionCustomizada("Não existe Contas a Receber para a ID da Entidade: " + idEntidade );
		}
		
		// Para mapeamento completo (com fechamentos)
				List<ContasReceberDTO> dtoCompleto = mappingService.mapCompleteList(listaContasReceber);
		
		return new ResponseEntity<List<ContasReceberDTO>>(dtoCompleto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getContasReceberByNomeEntidade/{nomeEntidade}")
	public ResponseEntity<List<ContasReceberDTO>> getContasReceberByNomeEntidade( @PathVariable("nomeEntidade") String nomeEntidade) throws ExceptionCustomizada{

		List<ContasReceber> listaContasReceber = contasReceberService.getContasReceberByNomeEntidade( nomeEntidade );
		
		if(listaContasReceber == null) {
			throw new ExceptionCustomizada("Não existe Contas a Receber para a Entidade: " + nomeEntidade );
		}
		
		// List<ContasReceberDTO> dto = contasReceberMapper.toListDto(listaContasReceber);
		
		// Para mapeamento completo (com fechamentos)
		List<ContasReceberDTO> dtoCompleto = mappingService.mapCompleteList(listaContasReceber);

		
		return new ResponseEntity<List<ContasReceberDTO>>(dtoCompleto, HttpStatus.OK);		
	}

}
