package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContasReceberDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RegistroRecebimentoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ContasReceberRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.ContasReceberMappingService;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.ContasReceberService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ContasReceberController {

	
	@Autowired
	private ContasReceberService contasReceberService;
		
	@Autowired
	private ContasReceberMappingService mappingService; 

	@Autowired
	private ContasReceberRepository contasReceberRepository;
	

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getContasReceberByAnoMes/{anoMes}")
	public ResponseEntity<?> getContasReceberByAnoMes( @PathVariable("anoMes") String anoMes , HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
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
			
	    } catch (ExceptionCustomizada ex) {
	    	
	    	long timestamp = System.currentTimeMillis();

	    	// Criar formato desejado
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    	sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Fuso horário opcional

	    	// Converter
	    	String dataFormatada = sdf.format(new Date(timestamp));
	    	
	        ErrorResponse error = new ErrorResponse(
	            HttpStatus.BAD_REQUEST.value(),
	            ex.getMessage(),
	            request.getRequestURI(),
	            dataFormatada
	        );
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	    }

	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getContasReceberByDtCriacao/{dtCriacaoIni}/{dtCriacaoFim}")
	public ResponseEntity<?> getContasReceberByDtCriacao( @PathVariable("dtCriacaoIni") String dtCriacaoIni ,
			                                                                            @PathVariable("dtCriacaoFim") String dtCriacaoFim, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			List<ContasReceber> listaContasReceber = contasReceberService.getContasReceberByDtCriacao( dtCriacaoIni, dtCriacaoFim );
			
			if(listaContasReceber == null) {
				throw new ExceptionCustomizada("Não existe Contas a Receber para o período entre: " + dtCriacaoIni + " e " + dtCriacaoFim);
			}
			
			// List<ContasReceberDTO> dto = contasReceberMapper.toListDto(listaContasReceber);  
			
			// Para mapeamento completo (com fechamentos)
			List<ContasReceberDTO> dtoCompleto = mappingService.mapCompleteList(listaContasReceber);
	
			
			return new ResponseEntity<List<ContasReceberDTO>>(dtoCompleto, HttpStatus.OK);
		
	    } catch (ExceptionCustomizada ex) {
	    	
	    	long timestamp = System.currentTimeMillis();

	    	// Criar formato desejado
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    	sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Fuso horário opcional

	    	// Converter
	    	String dataFormatada = sdf.format(new Date(timestamp));
	    	
	        ErrorResponse error = new ErrorResponse(
	            HttpStatus.BAD_REQUEST.value(),
	            ex.getMessage(),
	            request.getRequestURI(),
	            dataFormatada
	        );
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	    }

	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getContasReceberByDescStatusReceber/{status}")
	public ResponseEntity<?> getContasReceberByDescStatusReceber( @PathVariable("status") String status, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			StatusReceber statusContaReceber = StatusReceber.valueOf(status);
			
			List<ContasReceber> listaContasReceber = contasReceberService.getContasReceberByDescStatusReceber( statusContaReceber );
			
			if(listaContasReceber == null) {
				throw new ExceptionCustomizada("Não existe Contas a Receber para o Status: " + StatusCicloPgVenda.valueOf(status).getDescStatusReceber() );
			}
			
			// List<ContasReceberDTO> dto = contasReceberMapper.toListDto(listaContasReceber);
			// Para mapeamento completo (com fechamentos)
			List<ContasReceberDTO> dtoCompleto = mappingService.mapCompleteList(listaContasReceber);
	
			
			return new ResponseEntity<List<ContasReceberDTO>>(dtoCompleto, HttpStatus.OK);

	    } catch (ExceptionCustomizada ex) {
	    	
	    	long timestamp = System.currentTimeMillis();

	    	// Criar formato desejado
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    	sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Fuso horário opcional

	    	// Converter
	    	String dataFormatada = sdf.format(new Date(timestamp));
	    	
	        ErrorResponse error = new ErrorResponse(
	            HttpStatus.BAD_REQUEST.value(),
	            ex.getMessage(),
	            request.getRequestURI(),
	            dataFormatada
	        );
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	    }


	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getContasReceberVendaByIdConveniados/{idEntidade}")
	public ResponseEntity<?> getContasReceberVendaByIdConveniados( @PathVariable("idEntidade") Long idEntidade , HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			List<ContasReceber> listaContasReceber = contasReceberService.getCicloPagamentoVendaByIdConveniados( idEntidade );
			
			if(listaContasReceber == null) {
				throw new ExceptionCustomizada("Não existe Contas a Receber para a ID da Entidade: " + idEntidade );
			}
			
			// Para mapeamento completo (com fechamentos)
					List<ContasReceberDTO> dtoCompleto = mappingService.mapCompleteList(listaContasReceber);
			
			return new ResponseEntity<List<ContasReceberDTO>>(dtoCompleto, HttpStatus.OK);
	    } catch (ExceptionCustomizada ex) {
	    	
	    	long timestamp = System.currentTimeMillis();

	    	// Criar formato desejado
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    	sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Fuso horário opcional

	    	// Converter
	    	String dataFormatada = sdf.format(new Date(timestamp));
	    	
	        ErrorResponse error = new ErrorResponse(
	            HttpStatus.BAD_REQUEST.value(),
	            ex.getMessage(),
	            request.getRequestURI(),
	            dataFormatada
	        );
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	    }

	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getContasReceberByNomeEntidade/{nomeEntidade}")
	public ResponseEntity<?> getContasReceberByNomeEntidade( @PathVariable("nomeEntidade") String nomeEntidade, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			List<ContasReceber> listaContasReceber = contasReceberService.getContasReceberByNomeEntidade( nomeEntidade );
			
			if(listaContasReceber == null) {
				throw new ExceptionCustomizada("Não existe Contas a Receber para a Entidade: " + nomeEntidade );
			}
			
			// List<ContasReceberDTO> dto = contasReceberMapper.toListDto(listaContasReceber);
			
			// Para mapeamento completo (com fechamentos)
			List<ContasReceberDTO> dtoCompleto = mappingService.mapCompleteList(listaContasReceber);
	
			
			return new ResponseEntity<List<ContasReceberDTO>>(dtoCompleto, HttpStatus.OK);
		
	    } catch (ExceptionCustomizada ex) {
	    	
	    	long timestamp = System.currentTimeMillis();

	    	// Criar formato desejado
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    	sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Fuso horário opcional

	    	// Converter
	    	String dataFormatada = sdf.format(new Date(timestamp));
	    	
	        ErrorResponse error = new ErrorResponse(
	            HttpStatus.BAD_REQUEST.value(),
	            ex.getMessage(),
	            request.getRequestURI(),
	            dataFormatada
	        );
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	    }

	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@PostMapping(value = "/uploadNFEntidades/{idCicloCrVenda}")
	public ResponseEntity<?> uploadPdfsContasReceber( @RequestParam("files") MultipartFile[] files, 
			                                          @PathVariable("idCicloCrVenda") Long idCicloCrVenda , HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			
			if( idCicloCrVenda == null ) {
				throw new ExceptionCustomizada("Não existe um ciclo para este ID: " + idCicloCrVenda );
			}

	        List<ContasReceberDTO> dtos = contasReceberService.processarUpload( files, idCicloCrVenda );
	        return ResponseEntity.ok(dtos);
	    } catch (ExceptionCustomizada ex) {
	    	
	    	long timestamp = System.currentTimeMillis();

	    	// Criar formato desejado
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    	sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Fuso horário opcional

	    	// Converter
	    	String dataFormatada = sdf.format(new Date(timestamp));
	    	
	        ErrorResponse error = new ErrorResponse(
	            HttpStatus.BAD_REQUEST.value(),
	            ex.getMessage(),
	            request.getRequestURI(),
	            dataFormatada
	        );
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	    }

	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/
	@GetMapping("/downloadNFEntidade/{id}")
	public ResponseEntity<byte[]> downloadDocumento(@PathVariable Long id) {
		ContasReceber documento = contasReceberRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

	    byte[] fileContent = Base64.getDecoder().decode(documento.getConteudoBase64());

	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, 
	                    "attachment; filename=\"" + documento.getNomeArquivo() + "\"")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(fileContent);
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/
    @PutMapping("/registrarRecebimento/{id}")
    public ResponseEntity<?> registrarRecebimento( @PathVariable Long id, @RequestBody RegistroRecebimentoDTO registro, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	try {
    		
			if( registro == null ) {
				throw new ExceptionCustomizada("Não existe um Registro Recebimento este ID: " + id );
			}
    		
	        contasReceberService.registrarRecebimento(id, registro.getObservacao(), registro.getDocDeposito(), registro.getDtPagamento() );
	        
	        return ResponseEntity.ok("REGISTRAR_RECEBIMENTO_OK");
	        
	    } catch (ExceptionCustomizada ex) {
	    	
	    	long timestamp = System.currentTimeMillis();

	    	// Criar formato desejado
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    	sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Fuso horário opcional

	    	// Converter
	    	String dataFormatada = sdf.format(new Date(timestamp));
	    	
	        ErrorResponse error = new ErrorResponse(
	            HttpStatus.BAD_REQUEST.value(),
	            ex.getMessage(),
	            request.getRequestURI(),
	            dataFormatada
	        );
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	    }

    }


    
}
