package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContratoEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.ContratoEntidadeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ContratoEntidadeController {

    private final ContratoEntidadeService service;

	@ResponseBody                                   /* Poder dar um retorno da API      */
	@PostMapping(value = "/salvarContratoEntidade") /* Mapeando a url para receber JSON */
    public ResponseEntity<?> create(@Valid @RequestBody ContratoEntidadeDTO dto, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			
			if( dto == null ) {
				throw new ExceptionCustomizada("Não existe informações sobre o contrato, favor verificar." );
			}

            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
           
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

    @GetMapping("/getContratoEntidadeById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id , HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	try {
			if( id == null) {
				throw new ExceptionCustomizada("Não existe informações do ID." + id );
			}
   		
           return ResponseEntity.ok(service.findById(id));
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

    @GetMapping("/getAllContratoEntidade")
    public ResponseEntity<?> getAll( HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	try {
            
    		List<ContratoEntidadeDTO> dto = service.findAll();
			if(dto == null) {
				throw new ExceptionCustomizada("Não existe Contas Contratos cadastrados!" );
			}
    		return ResponseEntity.ok( dto );
    		// return ResponseEntity.ok(service.findAll());
    		
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

    @PutMapping("/updateContratoEntidadeById/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ContratoEntidadeDTO dto, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	try {
    		
    		if(dto == null) {
				throw new ExceptionCustomizada("Não existe informações para o Contrato da Entidade!");
			}

    		return ResponseEntity.ok(service.update(id, dto));
        
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

    @DeleteMapping("/deleteContratoEntidadeById/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	try {
			if(id == null) {
				throw new ExceptionCustomizada("Favor informar o ID do contrato a ser deletado!" );
			}
   		
    		
           service.delete(id);
           // return ResponseEntity.noContent().build();
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Entidade deletada com sucesso!");
           
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
