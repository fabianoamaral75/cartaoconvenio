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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.NichoMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Nicho;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.NichoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.NichoRepository;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class NichoController {

	@Autowired
	private NichoRepository nichoRepository;
	
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

}
