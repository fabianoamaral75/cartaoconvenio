package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
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
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.ContratoEntidadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContratoEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.ContratoEntidadeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ContratoEntidadeController {

    @Autowired
    private ContratoEntidadeService contratoEntidadeService;
    
    @Autowired
    private ContratoEntidadeMapper contratoEntidadeMapper;

	@ResponseBody                                   /* Poder dar um retorno da API      */
	@PostMapping(value = "/salvarContratoEntidade") /* Mapeando a url para receber JSON */
    public ResponseEntity<?> create(@Valid @RequestBody ContratoEntidadeDTO dto, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			
			if( dto == null ) {
				throw new ExceptionCustomizada("Não existe informações sobre o contrato, favor verificar." );
			}
			
			ContratoEntidadeDTO createdContrato = contratoEntidadeService.create(dto);
			return new ResponseEntity<ContratoEntidadeDTO>(createdContrato, HttpStatus.CREATED);
			
            // return ResponseEntity.status(HttpStatus.CREATED).body(contratoEntidadeService.create(dto));
           
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
	
    @GetMapping(value ="/getAllContratoEntidade")
    public ResponseEntity<?> getAll( HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	try {
            
    		List<ContratoEntidadeDTO> dto = contratoEntidadeService.findAll();
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
    
    /*************************************************************************************************************/

    @ResponseBody
    @GetMapping(value ="/getAllContratoEntidadeById/{idEntidade}")
    public ResponseEntity<?> getAllByEntidadeId(
            @PathVariable("idEntidade") Long idEntidade,
            HttpServletRequest request) throws ExceptionCustomizada, IOException {
        try {
            List<ContratoEntidade> contratos = contratoEntidadeService.findAllByEntidadeId(idEntidade);
            
            if(contratos == null || contratos.isEmpty()) {
                throw new ExceptionCustomizada("Nenhum contrato encontrado para a entidade com ID: " + idEntidade);
            }
            
            List<ContratoEntidadeDTO> dto = contratoEntidadeMapper.toListDto(contratos);
            return new ResponseEntity<>(dto, HttpStatus.OK);
            
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @GetMapping(value ="/getContratoEntidadeById/{idEntidade}/{idContrato}")
    public ResponseEntity<?> getById(
            @PathVariable("idEntidade") Long idEntidade,
            @PathVariable("idContrato") Long idContrato,
            HttpServletRequest request) throws ExceptionCustomizada, IOException {
        try {
            ContratoEntidade contrato = contratoEntidadeService.findByIdAndEntidadeId(idContrato, idEntidade);
            ContratoEntidadeDTO dto = contratoEntidadeMapper.toDTO(contrato);
            return new ResponseEntity<>(dto, HttpStatus.OK);
            
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @GetMapping(value ="/getContratoEntidadeByIdStatus/{idEntidade}/{status}")
    public ResponseEntity<?> getByStatus(
            @PathVariable("idEntidade") Long idEntidade,
            @PathVariable("status") Boolean status,
            HttpServletRequest request) throws ExceptionCustomizada, IOException {
        try {
            List<ContratoEntidade> contratos = contratoEntidadeService.findByEntidadeIdAndStatus(idEntidade, status);
            
            if(contratos == null || contratos.isEmpty()) {
                throw new ExceptionCustomizada("Nenhum contrato encontrado com status " + (status ? "ativo" : "inativo") + " para a entidade com ID: " + idEntidade);
            }
            
            List<ContratoEntidadeDTO> dto = contratoEntidadeMapper.toListDto(contratos);
            return new ResponseEntity<>(dto, HttpStatus.OK);
            
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }


    @ResponseBody
    @PutMapping(value = "/updateContratoEntidade/{idContrato}")
    public ResponseEntity<?> updateContrato( @PathVariable Long idContrato, @RequestBody ContratoEntidadeDTO dto, HttpServletRequest request) throws ExceptionCustomizada, IOException {
        try {
        	
        	
            if(dto == null) {
                throw new ExceptionCustomizada("Nenhuma informação encontrado para atualização a ID do Contrato da Entidade: " + idContrato );
            }

            dto = contratoEntidadeService.update(idContrato, dto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }
    
    @ResponseBody
    @PostMapping(value = "/renovarContratoEntidade/{idEntidade}/{idContrato}")
    public ResponseEntity<?> renovarContrato(
            @PathVariable("idEntidade") Long idEntidade,
            @PathVariable("idContrato") Long idContrato,
            @RequestBody ContratoEntidadeDTO novoContratoDto,
            HttpServletRequest request) throws ExceptionCustomizada, IOException {
        try {
            ContratoEntidade novoContrato     = contratoEntidadeMapper.toEntity(novoContratoDto);
            ContratoEntidade contratoRenovado = contratoEntidadeService.renovarContrato(idContrato, idEntidade, novoContrato);
            ContratoEntidadeDTO responseDto   = contratoEntidadeMapper.toDTO(contratoRenovado);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
            
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/deleteContratoEntidade/{idEntidade}/{idContrato}")
    public ResponseEntity<?> delete(
            @PathVariable("idEntidade") Long idEntidade,
            @PathVariable("idContrato") Long idContrato,
            HttpServletRequest request) throws ExceptionCustomizada, IOException {
        try {
            contratoEntidadeService.delete(idContrato, idEntidade);
            return ResponseEntity.noContent().build();
            
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

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
