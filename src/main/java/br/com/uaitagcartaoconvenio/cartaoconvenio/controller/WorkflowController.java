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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.WorkflowInformativoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.WorkflowService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @ResponseBody
	@PostMapping(value = "/salvarWorkflowInformativo") 
    public ResponseEntity<?> criar(@RequestBody WorkflowInformativoDTO dto, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	
    	try {
    		
	 		WorkflowInformativoDTO saved = workflowService.criarWorkflow(dto);
	 		
	 		saved = workflowService.criarWorkflow(saved);
	        
			if(saved == null) {
				throw new ExceptionCustomizada("Aconteceu algum erro ao tentar salvar o Workflow Informativo!" );
			}

	       // return ResponseEntity.created(URI.create("/salvarWorkflowInformativo/" + saved.getIdWorkflowInformativo())).body(saved);
	       
	        return new ResponseEntity<WorkflowInformativoDTO>(saved, HttpStatus.OK);
	        
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
	@GetMapping(value = "/getWorkflowInformativoByID/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	
    	try {
    		
			if(id == null) {
				throw new ExceptionCustomizada("Não foi informado o ID para a pesquisa do Workflow Informativo" );
			}
  		
          return ResponseEntity.ok(workflowService.buscarPorId(id));
          
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
	@GetMapping(value = "/getWorkflowInformativoAll")
    public ResponseEntity<?> listarTodos( HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	
    	try {
    		
    		List<WorkflowInformativoDTO> listaWorkflowInformativoAll = workflowService.listarTodos();
    		
			if(listaWorkflowInformativoAll == null) {
				throw new ExceptionCustomizada("Não foi encontrado Workflow Informativo cadastrado!" );
			}
    		
           return ResponseEntity.ok( listaWorkflowInformativoAll );
        
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

    @DeleteMapping(value = "/deleteWorkflowInformativoByID/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	
    	try {
	        String status = workflowService.deletarWorkflow(id);
	        
	        if( !status.equals("OK") ) {
				throw new ExceptionCustomizada("Ocorreu algum problema ao tentar deletar o Workflow Informativo do ID: " + id);
			}
	        
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Workflow Informativo deletado com sucesso!");
	        // return ResponseEntity.noContent().build();
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
