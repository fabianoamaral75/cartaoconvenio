package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusContrato;
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
    
///////////////////
///
    @GetMapping("/getVigenciaContratoConveniadaByConveniadaId/{idConveniada}")
    public ResponseEntity<?> getByConveniadaId(@PathVariable Long idConveniada, HttpServletRequest request) {
        try {
            List<VigenciaContratoConveniada> vigencias = service.findByConveniadaId(idConveniada);
            List<VigenciaContratoConveniadaDTO> dtos = vigencias.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }
    
    @GetMapping("/getVigenciaContratoConveniadaByConveniadaIdAndStatus/conveniada/{idConveniada}/status/{status}")
    public ResponseEntity<?> getByConveniadaIdAndStatus(
            @PathVariable Long idConveniada, 
            @PathVariable StatusContrato status,
            HttpServletRequest request) {
        try {
            List<VigenciaContratoConveniada> vigencias = service.findByConveniadaIdAndStatus(idConveniada, status);
            List<VigenciaContratoConveniadaDTO> dtos = vigencias.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }
    
    @GetMapping("/getVigenciaContratoConveniadaCurrentVigencia/conveniada/{idConveniada}/atual")
    public ResponseEntity<?> getCurrentVigencia(@PathVariable Long idConveniada, HttpServletRequest request) {
        try {
            VigenciaContratoConveniada vigencia = service.findCurrentVigencia(idConveniada);
            if (vigencia == null) {
                throw new ExceptionCustomizada("Nenhuma vigência encontrada para a conveniada: " + idConveniada);
            }
            return ResponseEntity.ok(mapper.toDTO(vigencia));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }
    
    @PostMapping("/renovarVigenciaContratoConveniada/conveniada/{idConveniada}/renovar")
    public ResponseEntity<?> renovarVigencia(
            @PathVariable Long idConveniada,
            @RequestBody VigenciaContratoConveniadaDTO dto,
            HttpServletRequest request) {
        try {
            VigenciaContratoConveniada novaVigencia = mapper.toEntity(dto);
            VigenciaContratoConveniada vigenciaRenovada = service.renovarVigencia(idConveniada, novaVigencia);
            return ResponseEntity.ok(mapper.toDTO(vigenciaRenovada));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }
    
    @PutMapping("/updateVigenciaContratoConveniadaStatus/{idVigencia}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long idVigencia,
            @RequestParam StatusContrato newStatus,
            HttpServletRequest request) {
        try {
            VigenciaContratoConveniada vigenciaAtualizada = service.updateStatus(idVigencia, newStatus);
            return ResponseEntity.ok(mapper.toDTO(vigenciaAtualizada));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }
    
    private ResponseEntity<?> handleError(Exception ex, HttpServletRequest request) {
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        String dataFormatada = sdf.format(new Date(timestamp));
        
        String message = ex.getMessage();
        if (ex instanceof ExceptionCustomizada) {
            message = ex.getMessage();
        } else {
            message = "Ocorreu um erro ao processar a requisição";
        }
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            message,
            request.getRequestURI(),
            dataFormatada
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }    
    
    
    
    
    
    
}
