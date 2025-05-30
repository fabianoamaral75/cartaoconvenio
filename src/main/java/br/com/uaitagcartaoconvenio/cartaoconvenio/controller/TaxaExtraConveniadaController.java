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
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaExtraConveniadaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaExtraConveniadaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TaxaExtraConveniadaService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaxaExtraConveniadaController {

    private final TaxaExtraConveniadaService service;
    private final TaxaExtraConveniadaMapper mapper;

    @PostMapping(value = "/salvarTaxaExtraConveniada")
    public ResponseEntity<?> create( @RequestBody TaxaExtraConveniadaDTO dto, HttpServletRequest request ) {
    	
    	try {
			if(dto == null) {
				throw new ExceptionCustomizada("Favor informar os dados da Taxa Extra para a Conveniada!");
			}

    		
	        TaxaExtraConveniada entity = mapper.toEntity(dto);
	        TaxaExtraConveniada savedEntity = service.save(entity);
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

    @GetMapping(value = "/getTaxaExtraConveniadaById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, HttpServletRequest request ) {

    	try {
    	
	    	TaxaExtraConveniada entity = service.findById(id);
	    	
	        if (entity == null) {
	        	throw new ExceptionCustomizada("Não existe Taxa Extra Conveniada com o ID dasta Entidade: " + id );
	            // return ResponseEntity.notFound().build();
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
