package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.SecretariaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Secretaria;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.SecretariaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.SecretariaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
public class SecretariaController {
	
	
	@Autowired
	private SecretariaService secretariaService;
	
	@Autowired
    private SecretariaMapper secretariaMapper; 
	
	@ResponseBody
	@PostMapping(value = "/salvarSecretaria")
	public ResponseEntity<?> salvarSecretaria( @RequestBody Secretaria secretaria, HttpServletRequest request ) throws ExceptionCustomizada, UnsupportedEncodingException{
		try {
			if( secretaria == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Entidade. Valores vazios!");
			
			secretaria.setFuncionario(null);
			
			secretaria = secretariaService.salvarSecretariaService(secretaria);
			
			//SecretariaDTO dto = SecretariaMapper.INSTANCE.toDto(secretaria); 
			SecretariaDTO dto = secretariaMapper.toDto(secretaria); // Use o mapper injetado
			
			return new ResponseEntity<SecretariaDTO>(dto, HttpStatus.OK);	
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
	@GetMapping(value = "/getSecretariaByIdEntidade/{id}")
	public ResponseEntity<?> getSecretariaByIdEntidade( @PathVariable("id") Long id, HttpServletRequest request ) throws ExceptionCustomizada, UnsupportedEncodingException{
		try {
			if( id == null ) throw new ExceptionCustomizada("ERRO: Favor informar o ID da Entidade");
			
			
			List<Secretaria> secretaria = secretariaService.getSecretariaByIdEntidade(id);
			
			List<SecretariaDTO> dto = secretariaMapper.toDtoList(secretaria); 
			
			return new ResponseEntity<List<SecretariaDTO>>(dto, HttpStatus.OK);	
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
	@GetMapping(value = "/getSecretariaByIdFuncionario/{id}")
	public ResponseEntity<?> getSecretariaByIdFuncionario( @PathVariable("id") Long id, HttpServletRequest request ) throws ExceptionCustomizada, UnsupportedEncodingException{
		try {
			if( id == null ) throw new ExceptionCustomizada("ERRO: Favor informar o ID do Funcionario");
			
			
			List<Secretaria> secretaria = secretariaService.getSecretariaByIdFuncionario(id);
			
			List<SecretariaDTO> dto = secretariaMapper.toDtoList(secretaria); 
			
			return new ResponseEntity<List<SecretariaDTO>>(dto, HttpStatus.OK);	
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
	@GetMapping(value = "/getByIdSecretaria/{id}")
	public ResponseEntity<?> getByIdSecretaria(@PathVariable("id") Long id, HttpServletRequest request) 
	    throws ExceptionCustomizada, UnsupportedEncodingException {
	    try {
	        if (id == null) throw new ExceptionCustomizada("ERRO: Favor informar o ID do Funcionario");

	        Optional<Secretaria> secretaria = secretariaService.getByIdSecretaria(id);
	        
	        if (!secretaria.isPresent()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Secretaria não encontrada");
	        }

	        // Converte o conteúdo do Optional para DTO
	        SecretariaDTO dto = secretariaMapper.toDto(secretaria.get());

	        return new ResponseEntity<>(dto, HttpStatus.OK);
	        
	    } catch (ExceptionCustomizada ex) {
	        long timestamp = System.currentTimeMillis();
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
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
	@GetMapping(value = "/getAllSecretaria")
	public ResponseEntity<?> getAllSecretaria( HttpServletRequest request ) throws ExceptionCustomizada, UnsupportedEncodingException{
		try {

			List<Secretaria> secretaria = secretariaService.getAllSecretaria();
			
			if( secretaria == null ) throw new ExceptionCustomizada("Não existe Secretaria cadastrada");
			
			List<SecretariaDTO> dto = secretariaMapper.toDtoList(secretaria); 
			
			return new ResponseEntity<List<SecretariaDTO>>(dto, HttpStatus.OK);	
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

	@PutMapping("/atualizarSecretaria/{id}")
	public ResponseEntity<?> atualizarSecretaria(
	    @PathVariable Long id,
	    @Valid @RequestBody Secretaria secretariaAtualizada,
	    HttpServletRequest request
	) throws ExceptionCustomizada, UnsupportedEncodingException {
	    try {
	        if (id == null) {
	            throw new ExceptionCustomizada("ERRO: Favor informar o ID da Secretaria");
	        }

	        Secretaria secretaria = secretariaService.atualizarSecretaria(id, secretariaAtualizada);
	        SecretariaDTO dto = secretariaMapper.toDto(secretaria);

	        return new ResponseEntity<>(dto, HttpStatus.OK);
	    } catch (ExceptionCustomizada ex) {
	        long timestamp = System.currentTimeMillis();
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
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
