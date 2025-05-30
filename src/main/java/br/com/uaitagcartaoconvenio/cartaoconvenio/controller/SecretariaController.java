package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

@RestController
public class SecretariaController {
	
	
	@Autowired
	private SecretariaService secretariaService;
	
	@Autowired
    private SecretariaMapper secretariaMapper; // Injeção do mapper
	
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


}
