package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.CicloPagamentoVendaInterfaceMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.CicloPagamentoVendaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.CicloPagamentoVendaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.CicloPagamentoVendaService;

@Controller
public class CicloPagamentoVendaController {

	@Autowired
	private CicloPagamentoVendaService cicloPagamentoVendaService;
	
	@Autowired
	private CicloPagamentoVendaRepository cicloPagamentoVendaRepository;

	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getCicloPagamentoVendaByAnoMes/{anoMes}")
	public ResponseEntity<List<CicloPagamentoVendaDTO>> getCicloPagamentoVendaByAnoMes( @PathVariable("anoMes") String anoMes ) throws ExceptionCustomizada{

		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByAnoMes( anoMes );
		
		if(listaCicloPagamentoVenda == null) {
			throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para o período: " + anoMes );
		}
		
		List<CicloPagamentoVendaDTO> dto = CicloPagamentoVendaInterfaceMapper.INSTANCE.toListDto(listaCicloPagamentoVenda);
		
		return new ResponseEntity<List<CicloPagamentoVendaDTO>>(dto, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getCicloPagamentoVendaByAnoMesStatus/{anoMes}/{status}")
	public ResponseEntity<List<CicloPagamentoVendaDTO>> getCicloPagamentoVendaByAnoMesStatus( @PathVariable("anoMes") String anoMes,@PathVariable("status") String status) throws ExceptionCustomizada{

		StatusCicloPgVenda statusCicloPgVenda = StatusCicloPgVenda.valueOf(status);

		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByAnoMesStatus( anoMes, statusCicloPgVenda );
		
		if(listaCicloPagamentoVenda == null) {
			throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para o período: " + anoMes + " e Status: " + StatusCicloPgVenda.valueOf(status).name() );
		}
		
		List<CicloPagamentoVendaDTO> dto = CicloPagamentoVendaInterfaceMapper.INSTANCE.toListDto(listaCicloPagamentoVenda);
		
		return new ResponseEntity<List<CicloPagamentoVendaDTO>>(dto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getCicloPagamentoVendaByDtCriacao/{dtCriacaoIni}/{dtCriacaoFim}")
	public ResponseEntity<List<CicloPagamentoVendaDTO>> getCicloPagamentoVendaByDtCriacao( @PathVariable("dtCriacaoIni") String dtCriacaoIni ,
			                                                                            @PathVariable("dtCriacaoFim") String dtCriacaoFim) throws ExceptionCustomizada{

		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByDtCriacao( dtCriacaoIni, dtCriacaoFim );
		
		if(listaCicloPagamentoVenda == null) {
			throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para o período entre: " + dtCriacaoIni + " e " + dtCriacaoFim);
		}
		
		List<CicloPagamentoVendaDTO> dto = CicloPagamentoVendaInterfaceMapper.INSTANCE.toListDto(listaCicloPagamentoVenda);
		
		return new ResponseEntity<List<CicloPagamentoVendaDTO>>(dto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getCicloPagamentoVendaByDescStatusPagamento/{status}")
	public ResponseEntity<List<CicloPagamentoVendaDTO>> getCicloPagamentoVendaByDescStatusPagamento( @PathVariable("status") String status) throws ExceptionCustomizada{

		StatusCicloPgVenda statusCicloPgVenda = StatusCicloPgVenda.valueOf(status);
		
		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByDescStatusPagamento( statusCicloPgVenda );
		
		if(listaCicloPagamentoVenda == null) {
			throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para o Status: " + StatusCicloPgVenda.valueOf(status).getDescStatusReceber() );
		}
		
		List<CicloPagamentoVendaDTO> dto = CicloPagamentoVendaInterfaceMapper.INSTANCE.toListDto(listaCicloPagamentoVenda);
		
		return new ResponseEntity<List<CicloPagamentoVendaDTO>>(dto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getCicloPagamentoVendaByIdConveniados/{idConveniados}")
	public ResponseEntity<CicloPagamentoVendaDTO> getCicloPagamentoVendaByIdConveniados( @PathVariable("idConveniados") Long idConveniados ) throws ExceptionCustomizada{

		CicloPagamentoVenda cicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByIdConveniados( idConveniados );
		
		if(cicloPagamentoVenda == null) {
			throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para a ID da Conveniada: " + idConveniados );
		}
		
		CicloPagamentoVendaDTO dto = CicloPagamentoVendaInterfaceMapper.INSTANCE.toDto(cicloPagamentoVenda);
		
		return new ResponseEntity<CicloPagamentoVendaDTO>(dto, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getCicloPagamentoVendaByNomeConveniado/{nomeConveniado}")
	public ResponseEntity<List<CicloPagamentoVendaDTO>> getCicloPagamentoVendaByNomeConveniado( @PathVariable("nomeConveniado") String nomeConveniado) throws ExceptionCustomizada{

		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByNomeConveniado( nomeConveniado );
		
		if(listaCicloPagamentoVenda == null) {
			throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para a Conveniada: " + nomeConveniado );
		}
		
		List<CicloPagamentoVendaDTO> dto = CicloPagamentoVendaInterfaceMapper.INSTANCE.toListDto(listaCicloPagamentoVenda);
		
		return new ResponseEntity<List<CicloPagamentoVendaDTO>>(dto, HttpStatus.OK);		
	}
	
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@PostMapping(value = "/uploadNFConveniada/{idCicloPgVenda}")
	public ResponseEntity<List<CicloPagamentoVendaDTO>> uploadPdfs( 
			                                            @RequestParam("files") MultipartFile[] files, 
			                                            @PathVariable("idCicloPgVenda") Long idCicloPgVenda ) throws ExceptionCustomizada{

		
        List<CicloPagamentoVendaDTO> dtos = cicloPagamentoVendaService.processarUpload(files, idCicloPgVenda);
        return ResponseEntity.ok(dtos);
        
	//	return new ResponseEntity<List<CicloPagamentoVenda>>(listaCicloPagamentoVenda, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/
	@GetMapping("/downloadNFConveniada/{id}")
	public ResponseEntity<byte[]> downloadDocumento(@PathVariable Long id) {
		CicloPagamentoVenda documento = cicloPagamentoVendaRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

	    byte[] fileContent = Base64.getDecoder().decode(documento.getConteudoBase64());

	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, 
	                    "attachment; filename=\"" + documento.getNomeArquivo() + "\"")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(fileContent);
	}

}
