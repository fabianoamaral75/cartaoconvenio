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
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TipoPeriodoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TipoPeriodoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TipoPeriodoController {

    private final TipoPeriodoService tipoPeriodoService;

    @GetMapping(value = "/getAllTipoPeriodo")
    public ResponseEntity<?> findAll( HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	
    	try {
    		
    		List<TipoPeriodoDTO> tiposPeriodo = tipoPeriodoService.findAll();
    		
			if(tiposPeriodo == null) {
				throw new ExceptionCustomizada("Não foi encontrado tipos de periodo!");
			}
   		
            return ResponseEntity.ok(tiposPeriodo);
            
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

    @GetMapping(value = "/getTipoPeriodoById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	try {
    		
	        TipoPeriodoDTO tipoPeriodo = tipoPeriodoService.findById(id);
	        
			if(tipoPeriodo == null) {
				throw new ExceptionCustomizada("Não foi encontrado tipos de período para o ID " + id );
			}
	        
	        return ResponseEntity.ok(tipoPeriodo);
        
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

    @PostMapping(value = "/salvarTipoPeriodo")
    public ResponseEntity<?> create(@Valid @RequestBody TipoPeriodoDTO tipoPeriodoDTO, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	
    	try {
    		TipoPeriodoDTO createdTipoPeriodo = tipoPeriodoService.create(tipoPeriodoDTO);
    		
			if(createdTipoPeriodo == null) {
				throw new ExceptionCustomizada("Erro ao savar o Tipos do período para o ID ");
			}
   		   		
    		return ResponseEntity.status(HttpStatus.CREATED).body(createdTipoPeriodo);
    		
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

    @PutMapping("/updateTipoPeriodoById/{id}")
    public ResponseEntity<?> update( @PathVariable Long id, @Valid @RequestBody TipoPeriodoDTO tipoPeriodoDTO, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	
    	try {

	        TipoPeriodoDTO updatedTipoPeriodo = tipoPeriodoService.update(id, tipoPeriodoDTO);
	        
			if(updatedTipoPeriodo == null) {
				throw new ExceptionCustomizada("Erro ao tentar realizar o update do Tipos do período para o ID ");
			}

	        
	        return ResponseEntity.ok(updatedTipoPeriodo);
	        
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

    @DeleteMapping("/deleteTipoPeriodoById/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	try {
    		
	        String retorno = tipoPeriodoService.delete(id);
	        
			if(!retorno.equals("OK")) {
				throw new ExceptionCustomizada("Erro ao tentar deletar o Tipos do período para o ID " + id);
			}
	        
	        
	        return ResponseEntity.noContent().build();
	        
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
