package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

	@Autowired
    private TaxaExtraConveniadaService taxaExtraConveniadaService;
	
    private final TaxaExtraConveniadaMapper mapper;

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @PostMapping(value = "/salvarTaxaExtraConveniada")
    public ResponseEntity<?> create( @RequestBody TaxaExtraConveniadaDTO dto, HttpServletRequest request ) {
    	
    	try {
			if(dto == null) {
				throw new ExceptionCustomizada("Favor informar os dados da Taxa Extra para a Conveniada!");
			}

    		
	        TaxaExtraConveniada entity = mapper.toEntity(dto);
	        TaxaExtraConveniada savedEntity = taxaExtraConveniadaService.save(entity);
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

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
/*   
    @GetMapping(value = "/getTaxaExtraConveniadaById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, HttpServletRequest request ) {

    	try {
    	
	    	TaxaExtraConveniada entity = taxaExtraConveniadaService.findById(id);
	    	
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
*/
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @ResponseBody
    @GetMapping(value = "/getAllTaxaExtraConveniadaById/{idConveniado}")
    public ResponseEntity<?> getAllByConveniado( @PathVariable Long idConveniado, HttpServletRequest request) throws ExceptionCustomizada {
        try {
            List<TaxaExtraConveniada> taxas = taxaExtraConveniadaService.findAllByConveniadoId(idConveniado);
            
            if(taxas == null || taxas.isEmpty()) {
                throw new ExceptionCustomizada("Não existem taxas cadastradas para este conveniado!");
            }
            
            List<TaxaExtraConveniadaDTO> dto = TaxaExtraConveniadaMapper.INSTANCE.toListDTO(taxas);
            return new ResponseEntity<List<TaxaExtraConveniadaDTO>>(dto, HttpStatus.OK);
            
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @ResponseBody
    @GetMapping(value = "/getTaxaExtraConveniadaById/{idConveniado}/{idTaxa}")
    public ResponseEntity<?> getById( @PathVariable Long idConveniado, @PathVariable Long idTaxa, HttpServletRequest request) throws ExceptionCustomizada {
        try {
            TaxaExtraConveniada taxa = taxaExtraConveniadaService.findById(idConveniado, idTaxa);
            
            if(taxa == null) {
                throw new ExceptionCustomizada("Taxa não encontrada para o ID: " + idTaxa);
            }
            
            TaxaExtraConveniadaDTO dto = TaxaExtraConveniadaMapper.INSTANCE.toDTO(taxa);
            return new ResponseEntity<TaxaExtraConveniadaDTO>(dto, HttpStatus.OK);
            
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @ResponseBody
    @GetMapping("/getTaxaExtraConveniadaStatusById/{idConveniado}/{status}")
    public ResponseEntity<?> getByStatus( @PathVariable Long idConveniado, @PathVariable String status, HttpServletRequest request) throws ExceptionCustomizada {
        try {
            List<TaxaExtraConveniada> taxas = taxaExtraConveniadaService.findByConveniadoIdAndStatus(idConveniado, status);
            
            if(taxas == null || taxas.isEmpty()) {
                throw new ExceptionCustomizada("Não existem taxas com status " + status + " para este conveniado");
            }
            
            List<TaxaExtraConveniadaDTO> dto = TaxaExtraConveniadaMapper.INSTANCE.toListDTO(taxas);
            return new ResponseEntity<List<TaxaExtraConveniadaDTO>>(dto, HttpStatus.OK);
            
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @ResponseBody
    @PostMapping("/createTaxaExtraConveniada/{idConveniado}")
    public ResponseEntity<?> create( @PathVariable Long idConveniado, @RequestBody TaxaExtraConveniadaDTO dto, HttpServletRequest request) throws ExceptionCustomizada {
        try {
        	
            if(idConveniado == null) {
                throw new ExceptionCustomizada("Não foi informado a Conveniada para incluão da Taxa Extra!");
            }

            if(dto == null ) {
                throw new ExceptionCustomizada("Não existem informação da Taxa Extra para a Conveniada!");
            }

            TaxaExtraConveniada novaTaxa = taxaExtraConveniadaService.create(idConveniado, dto);
            TaxaExtraConveniadaDTO responseDto = TaxaExtraConveniadaMapper.INSTANCE.toDTO(novaTaxa);
            return new ResponseEntity<TaxaExtraConveniadaDTO>(responseDto, HttpStatus.CREATED);
            
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @ResponseBody
    @PutMapping("/updateTaxaExtraConveniada/{idConveniado}/{idTaxa}")
    public ResponseEntity<?> update( @PathVariable Long idConveniado, @PathVariable Long idTaxa, @RequestBody TaxaExtraConveniadaDTO dto, HttpServletRequest request) throws ExceptionCustomizada {
        try {
        	
            if(idConveniado == null) {
                throw new ExceptionCustomizada("Não foi informado a Conveniada para atualização da Taxa Extra!");
            }

            if(dto == null ) {
                throw new ExceptionCustomizada("Não existem informação da Taxa Extra para a Conveniada para atualização!");
            }

            TaxaExtraConveniada taxaAtualizada = taxaExtraConveniadaService.update(idConveniado, idTaxa, dto);
            TaxaExtraConveniadaDTO responseDto = TaxaExtraConveniadaMapper.INSTANCE.toDTO(taxaAtualizada);
            return new ResponseEntity<TaxaExtraConveniadaDTO>(responseDto, HttpStatus.OK);
            
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @ResponseBody
    @PatchMapping("/updateTaxaExtraConveniadaStatus/{idConveniado}/{idTaxa}/{novoStatus}") 
    public ResponseEntity<?> updateStatus( @PathVariable Long idConveniado, @PathVariable Long idTaxa, @PathVariable String novoStatus, HttpServletRequest request) throws ExceptionCustomizada {
        try {
        	
            if(idConveniado == null) {
                throw new ExceptionCustomizada("Não foi informado a Conveniada para atualização da Taxa Extra!");
            }

            if(idTaxa == null ) {
                throw new ExceptionCustomizada("Não foi informado a Taxa para atualização do Status!");
            }

            TaxaExtraConveniada taxaAtualizada = taxaExtraConveniadaService.updateStatus(idConveniado, idTaxa, novoStatus);
            TaxaExtraConveniadaDTO responseDto = TaxaExtraConveniadaMapper.INSTANCE.toDTO(taxaAtualizada);
            return new ResponseEntity<TaxaExtraConveniadaDTO>(responseDto, HttpStatus.OK);
            
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @ResponseBody
    @DeleteMapping("/deleteTaxaExtraConveniada/{idConveniado}/{idTaxa}") 
    public ResponseEntity<?> delete(
            @PathVariable Long idConveniado,
            @PathVariable Long idTaxa,
            HttpServletRequest request) throws ExceptionCustomizada {
        try {
        	
            if(idConveniado == null) {
                throw new ExceptionCustomizada("Não foi informado a Conveniada para ser apagada a Taxa Extra!");
            }

            if(idTaxa == null ) {
                throw new ExceptionCustomizada("Não foi informado a Taxa para ser apagada!");
            }
        	
            taxaExtraConveniadaService.delete(idConveniado, idTaxa);
            return ResponseEntity.noContent().build();
            
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    private ResponseEntity<ErrorResponse> handleException(ExceptionCustomizada ex, HttpServletRequest request) {
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
