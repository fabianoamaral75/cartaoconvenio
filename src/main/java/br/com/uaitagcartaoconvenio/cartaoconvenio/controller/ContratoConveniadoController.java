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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.ContratoConveniadoMapper;
//import br.com.uaitagcartaoconvenio.cartaoconvenio.dto.ContratoConveniadoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoConveniado;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContratoConveniadoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.ContratoConveniadoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
// @RequestMapping("/api/conveniados/{idConveniado}/contratos")
public class ContratoConveniadoController {

    @Autowired
    private ContratoConveniadoService contratoConveniadoService;

    /**
     * Retorna uma lista com todos contratos para uma Conveniada.
     * 
     * @param ID Conveniado 
     * @return List<ContratoConveniado>.
     */
   @GetMapping("/getAllContratoConveniadoByIdConveniado/{idConveniado}")
    public ResponseEntity<?> getAllByConveniado(@PathVariable Long idConveniado, HttpServletRequest request) throws ExceptionCustomizada {
	   
	   try {
		 
		 if( idConveniado == null ){
             throw new ExceptionCustomizada("Não foi informado a Conveniada!");
         }
        
	     List<ContratoConveniado> contratos = contratoConveniadoService.findAllByConveniadoId(idConveniado);
        
	     List<ContratoConveniadoDTO> responseDto = ContratoConveniadoMapper.INSTANCE.toListDTO(contratos);
	     
	     return new ResponseEntity<List<ContratoConveniadoDTO>>(responseDto, HttpStatus.OK);
         // return ResponseEntity.ok(contratos);
        
       } catch (ExceptionCustomizada ex) {
           return handleException(ex, request);
       }
    }

   /**
    * Retorna uma contratos para uma Conveniada.
    * 
    * @param idContrato   --> Id do Contrato
    * @param idConveniado --> Id da Conveniado
    * @return objeto ContratoConveniado
    * @throws EntityNotFoundException ==> "Contrato não encontrado para o conveniado informado"
    */
   @GetMapping("/getContratoConveniado/{idContrato}/{idConveniado}")
    public ResponseEntity<?> getById( @PathVariable Long idConveniado, @PathVariable Long idContrato, HttpServletRequest request) throws ExceptionCustomizada {

        ContratoConveniado contrato = contratoConveniadoService.findById(idConveniado, idContrato);
        
        ContratoConveniadoDTO responseDto = ContratoConveniadoMapper.INSTANCE.toDTO(contrato);
        
        return new ResponseEntity<ContratoConveniadoDTO>(responseDto, HttpStatus.OK);

    }

   /**
    * Retorna um contrato Novo para uma Conveniada.
    * 
    * @param idConveniado          --> Id da Conveniado
    * @param ContratoConveniadoDTO --> com a estrutura completa das informaçoes do contratro
    * @return objeto ContratoConveniado contendo o novo contrato que foi criado.
    * @throws EntityNotFoundException --> caso nõa enconte o contrato do ID psequisado: ("Conveniado não encontrado")
    */
    @PostMapping("/createContratoConveniado/{idConveniado}")
    public ResponseEntity<?> create( @PathVariable Long idConveniado, @RequestBody ContratoConveniadoDTO dto, HttpServletRequest request) throws ExceptionCustomizada {
    	try {
    		
   		 if( idConveniado == null || dto== null ){
             throw new ExceptionCustomizada("Não foi informado os dados para a criação do Contrato da Conveniada!");
         }
   		
	        ContratoConveniado novoContrato = contratoConveniadoService.create(idConveniado, dto);
	        
	        ContratoConveniadoDTO responseDto = ContratoConveniadoMapper.INSTANCE.toDTO(novoContrato);
	        
	        return new ResponseEntity<ContratoConveniadoDTO>(responseDto, HttpStatus.CREATED);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }    
        
    }

    /**
     * Retorna um contrato Update para uma Conveniada.
     * 
     * @param idContrato            --> Id do Contrato
     * @param idConveniado          --> Id da Conveniado
     * @param ContratoConveniadoDTO --> com a estrutura completa das informaçoes do contratro
     * @return objeto ContratoConveniado contendo o contrato que foi atualizado.
     * @throws ?????
     */
    @PutMapping("/updateContratoConveniado/{idConveniado}/{idContrato}")
    public ResponseEntity<?> update( @PathVariable Long idConveniado, @PathVariable Long idContrato, @RequestBody ContratoConveniadoDTO dto, HttpServletRequest request) throws ExceptionCustomizada {
        try {
 
	    	if( idConveniado == null || dto== null ){
	             throw new ExceptionCustomizada("Não foi informado os dados para a criação do Contrato da Conveniada!");
	         }
	    	
	        ContratoConveniado contratoAtualizado = contratoConveniadoService.update(idConveniado, idContrato, dto);
	        
	        ContratoConveniadoDTO responseDto = ContratoConveniadoMapper.INSTANCE.toDTO(contratoAtualizado);
	        
	        return new ResponseEntity<ContratoConveniadoDTO>(responseDto, HttpStatus.OK);
	        
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
       
    }

    /**
     * Retorna Mensagem de sucesso ou erro caso a operação tenha sucesso.
     * 
     * @param idContrato            --> Id do Contrato
     * @param idConveniado          --> Id da Conveniado
     * @return String com a data no formato PostgreSQL
     * @throws EntityNotFoundException("Contrato não encontrado para o conveniado informado")
     */
    @DeleteMapping("/deleteContratoConveniado/{idConveniado}/{idContrato}")
    public ResponseEntity<?> delete( @PathVariable Long idConveniado, @PathVariable Long idContrato) {
        try {  	
	        contratoConveniadoService.delete(idConveniado, idContrato);
	        return ResponseEntity.ok("Status atualizado com sucesso para " + idContrato);
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
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