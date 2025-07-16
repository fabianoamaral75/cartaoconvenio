package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.NichoMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Nicho;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.NichoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.NichoRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.NichoService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class NichoController {

	@Autowired
	private NichoRepository nichoRepository;
	
	@Autowired
	private NichoService nichoService;
	
	@ResponseBody
	@PostMapping(value = "/salvarNicho")
	public ResponseEntity<?> salvarNicho( @RequestBody Nicho nicho, HttpServletRequest request ) throws ExceptionCustomizada, UnsupportedEncodingException{
		try {
			
			if( nicho == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar o Nicho para as empresas conveniadas. Valores vazios!");
	
			nicho = nichoRepository.saveAndFlush(nicho);
			
			NichoDTO dto = NichoMapper.INSTANCE.toDto(nicho); 
			
			return new ResponseEntity<NichoDTO>(dto, HttpStatus.OK);
			
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

	
	@ResponseBody
	@GetMapping(value = "/getAllNicho")
	public ResponseEntity<?> getAllNicho( HttpServletRequest request ) throws ExceptionCustomizada{
		try {
			
			List<Nicho> listNicho = nichoRepository.getAllNicho();
			
			if(listNicho == null) {
				throw new ExceptionCustomizada("Não existe Nicho cadastradas!" );
			}
			
			List<NichoDTO> dto = NichoMapper.INSTANCE.toListDto(listNicho);  
			
			return new ResponseEntity<List<NichoDTO>>(dto, HttpStatus.OK);	
			
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

	@ResponseBody
	@GetMapping(value = "/findNichoByNome/{nomeNicho}")
	public ResponseEntity<?>findNichoByNome( @PathVariable("nomeNicho") String nomeNicho, HttpServletRequest request ) throws ExceptionCustomizada{
		try {
		List<Nicho> listNicho = nichoRepository.findNichoNome(nomeNicho);
		
		if(listNicho == null) {
			throw new ExceptionCustomizada("Não existe Nicho cadastradas!" );
		}
		
		List<NichoDTO> dto = NichoMapper.INSTANCE.toListDto(listNicho);  
		
			return new ResponseEntity<List<NichoDTO>>(dto, HttpStatus.OK);
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

	@ResponseBody
	@GetMapping(value = "/findNichoNome/{id}")
	public ResponseEntity<?> findNichoById( @PathVariable("id") Long id, HttpServletRequest request ) throws ExceptionCustomizada{
		try {
			
			Nicho nicho = nichoRepository.findNichoById(id);
			
			if(nicho == null) throw new ExceptionCustomizada("Não existe Nicho cadastradas!" );
			
			NichoDTO dto = NichoMapper.INSTANCE.toDto(nicho);
			
			return new ResponseEntity<NichoDTO>(dto, HttpStatus.OK);
		
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
   /////////////////
   ///
   ///
	@ResponseBody
	@PutMapping(value = "/atualizarNicho/{id}")
	public ResponseEntity<?> atualizarNichoCompleto(
	        @PathVariable("id") Long id,
	        @RequestBody Nicho nichoAtualizado,
	        HttpServletRequest request) {
	    try {
	        if (!id.equals(nichoAtualizado.getIdNicho())) {
	            throw new ExceptionCustomizada("ID do nicho não corresponde ao ID na URL");
	        }

	        Nicho nichoAtualizadoSalvo = nichoService.atualizarNichoCompleto(nichoAtualizado);
	        NichoDTO dto = NichoMapper.INSTANCE.toDto(nichoAtualizadoSalvo);
	        
	        return new ResponseEntity<>(dto, HttpStatus.OK);
	    } catch (Exception ex) {
	        return criarRespostaErro(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	    }
	}

	@ResponseBody
	@PatchMapping(value = "/atualizarDescricaoNicho/{id}")
	public ResponseEntity<?> atualizarDescricaoNicho(
	        @PathVariable("id") Long id,
	        @RequestBody String novaDescricao,
	        HttpServletRequest request) {
	    try {
	        nichoService.atualizarDescricao(id, novaDescricao);
	        return new ResponseEntity<>("Descrição do nicho atualizada com sucesso", HttpStatus.OK);
	    } catch (Exception ex) {
	        return criarRespostaErro(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	    }
	}

	@ResponseBody
	@PatchMapping(value = "/atualizarConveniadoNicho/{id}")
	public ResponseEntity<?> atualizarConveniadoNicho(
	        @PathVariable("id") Long id,
	        @RequestBody Long idConveniados,
	        HttpServletRequest request) {
	    try {
	        nichoService.atualizarConveniado(id, idConveniados);
	        return new ResponseEntity<>("Conveniado do nicho atualizado com sucesso", HttpStatus.OK);
	    } catch (Exception ex) {
	        return criarRespostaErro(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	    }
	}

	// Adicione este método para evitar duplicação de código
	private ResponseEntity<?> criarRespostaErro(HttpStatus status, String mensagem, HttpServletRequest request) {
	    long timestamp = System.currentTimeMillis();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
	    String dataFormatada = sdf.format(new Date(timestamp));
	    
	    ErrorResponse error = new ErrorResponse(
	        status.value(),
	        mensagem,
	        request.getRequestURI(),
	        dataFormatada
	    );
	    return ResponseEntity.status(status).body(error);
	}
}
