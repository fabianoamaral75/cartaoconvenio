package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.VigenciaContratoConveniadaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.VigenciaContratoConveniadaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.VigenciaContratoConveniadaService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class VigenciaContratoConveniadaController {
    
    private final VigenciaContratoConveniadaService service;
    private final VigenciaContratoConveniadaMapper mapper;
    
    @PostMapping(value = "/salvarVigenciaContratoConveniada")
    public ResponseEntity<?> create( @RequestBody VigenciaContratoConveniadaDTO dto , HttpServletRequest request ) {
    	
    	try {
    		
			if( dto == null) {
				throw new ExceptionCustomizada("Não existe informação sobre a vigencia para ser salva" );
			}
   		
	        VigenciaContratoConveniada entity = mapper.toEntity(dto);
	        VigenciaContratoConveniada savedEntity = service.save(entity);
	        return ResponseEntity.ok(mapper.toDTO(savedEntity));
        
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
    
    @GetMapping(value = "/getVigenciaContratoConveniadaById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, HttpServletRequest request) {        
    	try {
	        VigenciaContratoConveniada entity = service.findById(id);
	        if (entity == null) {
	        	throw new ExceptionCustomizada("Não foi enconrada vigência para o ID: " + id );
	        //    return ResponseEntity.notFound().build();
	            
	        }
	        return ResponseEntity.ok(mapper.toDTO(entity));
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
