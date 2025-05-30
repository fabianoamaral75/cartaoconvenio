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
import org.springframework.validation.annotation.Validated;
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
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.CicloPagamentoVendaInterfaceMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.CicloPagamentoVendaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RegistroRecebimentoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.CicloPagamentoVendaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.CicloPagamentoVendaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@Validated // Habilita validação no controller
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
	public ResponseEntity<?> getCicloPagamentoVendaByAnoMes( @PathVariable("anoMes") String anoMes , HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByAnoMes( anoMes );
			
			if(listaCicloPagamentoVenda == null) {
				throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para o período: " + anoMes );
			}
			
			List<CicloPagamentoVendaDTO> dto = CicloPagamentoVendaInterfaceMapper.INSTANCE.toListDto(listaCicloPagamentoVenda);
			
			return new ResponseEntity<List<CicloPagamentoVendaDTO>>(dto, HttpStatus.OK);
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
	@GetMapping(value = "/getCicloPagamentoVendaByAnoMesStatus/{anoMes}/{status}")
	public ResponseEntity<?> getCicloPagamentoVendaByAnoMesStatus( @PathVariable("anoMes") String anoMes,@PathVariable("status") String status, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			StatusCicloPgVenda statusCicloPgVenda = StatusCicloPgVenda.valueOf(status);
	
			List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByAnoMesStatus( anoMes, statusCicloPgVenda );
			
			if(listaCicloPagamentoVenda == null) {
				throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para o período: " + anoMes + " e Status: " + StatusCicloPgVenda.valueOf(status).name() );
			}
			
			List<CicloPagamentoVendaDTO> dto = CicloPagamentoVendaInterfaceMapper.INSTANCE.toListDto(listaCicloPagamentoVenda);
			
			return new ResponseEntity<List<CicloPagamentoVendaDTO>>(dto, HttpStatus.OK);
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
	@GetMapping(value = "/getCicloPagamentoVendaByDtCriacao/{dtCriacaoIni}/{dtCriacaoFim}")
	public ResponseEntity<?> getCicloPagamentoVendaByDtCriacao( @PathVariable("dtCriacaoIni") String dtCriacaoIni ,
			                                                                            @PathVariable("dtCriacaoFim") String dtCriacaoFim, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByDtCriacao( dtCriacaoIni, dtCriacaoFim );
			
			if(listaCicloPagamentoVenda == null) {
				throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para o período entre: " + dtCriacaoIni + " e " + dtCriacaoFim);
			}
			
			List<CicloPagamentoVendaDTO> dto = CicloPagamentoVendaInterfaceMapper.INSTANCE.toListDto(listaCicloPagamentoVenda);
			
			return new ResponseEntity<List<CicloPagamentoVendaDTO>>(dto, HttpStatus.OK);
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
	@GetMapping(value = "/getCicloPagamentoVendaByDescStatusPagamento/{status}")
	public ResponseEntity<?> getCicloPagamentoVendaByDescStatusPagamento( @PathVariable("status") String status, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			StatusCicloPgVenda statusCicloPgVenda = StatusCicloPgVenda.valueOf(status);
			
			List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByDescStatusPagamento( statusCicloPgVenda );
			
			if(listaCicloPagamentoVenda == null) {
				throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para o Status: " + StatusCicloPgVenda.valueOf(status).getDescStatusReceber() );
			}
			
			List<CicloPagamentoVendaDTO> dto = CicloPagamentoVendaInterfaceMapper.INSTANCE.toListDto(listaCicloPagamentoVenda);
			
			return new ResponseEntity<List<CicloPagamentoVendaDTO>>(dto, HttpStatus.OK);
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
	@GetMapping(value = "/getCicloPagamentoVendaByIdConveniados/{idConveniados}")
	public ResponseEntity<?> getCicloPagamentoVendaByIdConveniados( @PathVariable("idConveniados") Long idConveniados , HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			CicloPagamentoVenda cicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByIdConveniados( idConveniados );
			
			if(cicloPagamentoVenda == null) {
				throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para a ID da Conveniada: " + idConveniados );
			}
			
			CicloPagamentoVendaDTO dto = CicloPagamentoVendaInterfaceMapper.INSTANCE.toDto(cicloPagamentoVenda);
			
			return new ResponseEntity<CicloPagamentoVendaDTO>(dto, HttpStatus.OK);
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
	@GetMapping(value = "/getCicloPagamentoVendaByNomeConveniado/{nomeConveniado}")
	public ResponseEntity<?> getCicloPagamentoVendaByNomeConveniado( @PathVariable("nomeConveniado") String nomeConveniado, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaService.getCicloPagamentoVendaByNomeConveniado( nomeConveniado );
			
			if(listaCicloPagamentoVenda == null) {
				throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para a Conveniada: " + nomeConveniado );
			}
			
			List<CicloPagamentoVendaDTO> dto = CicloPagamentoVendaInterfaceMapper.INSTANCE.toListDto(listaCicloPagamentoVenda);
			
			return new ResponseEntity<List<CicloPagamentoVendaDTO>>(dto, HttpStatus.OK);	
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
	@PostMapping(value = "/uploadNFConveniada/{idCicloPgVenda}")
	public ResponseEntity<?> uploadPdfs( @RequestParam("files") MultipartFile[] files, 
			                             @PathVariable("idCicloPgVenda") Long idCicloPgVenda , HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			if(idCicloPgVenda == null) {
				throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para a ID: " + idCicloPgVenda );
			}

	        List<CicloPagamentoVendaDTO> dtos = cicloPagamentoVendaService.processarUpload(files, idCicloPgVenda);
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
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/
    @PutMapping("/registrarPagamento/{id}")
    
    public ResponseEntity<?> registrarRecebimento( @PathVariable Long id, @Valid @RequestBody RegistroRecebimentoDTO registro, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	try {
    		
			if(registro == null) {
				throw new ExceptionCustomizada("Não existe Registro Recebimento para a ID: " + id );
			}
    		
	        cicloPagamentoVendaService.registrarPagamento(id, registro.getObservacao(), registro.getDocDeposito(), registro.getDtPagamento() );
	        
	        return ResponseEntity.ok("REGISTRAR_PAGAMENTO_OK");
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
