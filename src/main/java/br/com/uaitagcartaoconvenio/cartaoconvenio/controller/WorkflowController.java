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
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusWorkflowInformativo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContatoWorkflowDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.WorkflowInformativoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.WorkflowService;
import io.swagger.v3.oas.annotations.Operation;
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
/*
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
*/    
    
    
////////////////////////////////////////////////    
    
    // Operações para WorkflowInformativo
    @PostMapping(value = "/criarWorkflow")
    @Operation(summary = "Criar um novo workflow")
    public ResponseEntity<?> criarWorkflow(@RequestBody WorkflowInformativoDTO dto, HttpServletRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(workflowService.criarWorkflow(dto));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @GetMapping(value = "/buscarWorkflowPorId/{id}")
    @Operation(summary = "Buscar workflow por ID")
    public ResponseEntity<?> buscarWorkflowPorId(@PathVariable Long id, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(workflowService.buscarWorkflowPorId(id));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @GetMapping(value = "/listarTodosWorkflows")
    @Operation(summary = "Listar todos os workflows")
    public ResponseEntity<?> listarTodosWorkflows(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(workflowService.listarTodosWorkflows());
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @PutMapping(value = "/atualizarWorkflow/{id}")
    @Operation(summary = "Atualizar workflow")
    public ResponseEntity<?> atualizarWorkflow(
            @PathVariable Long id, 
            @RequestBody WorkflowInformativoDTO dto,
            HttpServletRequest request) {
        try {
            return ResponseEntity.ok(workflowService.atualizarWorkflow(id, dto));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @DeleteMapping(value = "/deletarWorkflow/{id}")
    @Operation(summary = "Deletar workflow")
    public ResponseEntity<?> deletarWorkflow(@PathVariable Long id, HttpServletRequest request) {
        try {
            workflowService.deletarWorkflow(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    // Operações para ContatoWorkflow
    @PostMapping(value = "/adicionarContato/{idWorkflow}/contatos")
    @Operation(summary = "Adicionar contato a um workflow")
    public ResponseEntity<?> adicionarContato(
            @PathVariable Long idWorkflow,
            @RequestBody ContatoWorkflowDTO dto,
            HttpServletRequest request) {
        try {
            dto.setIdWorkflowInformativo(idWorkflow);
            return ResponseEntity.status(HttpStatus.CREATED).body(workflowService.criarContato(dto));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @GetMapping(value = "/buscarContatoPorId/contatos/{id}")
    @Operation(summary = "Buscar contato por ID")
    public ResponseEntity<?> buscarContatoPorId(@PathVariable Long id, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(workflowService.buscarContatoPorId(id));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @GetMapping(value = "/listarContatosPorIdWorkflow/{idWorkflow}/contatos")
    @Operation(summary = "Listar contatos de um workflow")
    public ResponseEntity<?> listarContatosPorWorkflow(
            @PathVariable Long idWorkflow,
            HttpServletRequest request) {
        try {
            return ResponseEntity.ok(workflowService.listarContatosPorWorkflow(idWorkflow));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @PutMapping(value = "/atualizarContato/contatos/{id}")
    @Operation(summary = "Atualizar contato")
    public ResponseEntity<?> atualizarContato(
            @PathVariable Long id,
            @RequestBody ContatoWorkflowDTO dto,
            HttpServletRequest request) {
        try {
            return ResponseEntity.ok(workflowService.atualizarContato(id, dto));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @DeleteMapping(value = "/deletarContato/contatos/{id}")
    @Operation(summary = "Deletar contato")
    public ResponseEntity<?> deletarContato(@PathVariable Long id, HttpServletRequest request) {
        try {
            workflowService.deletarContato(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    // Métodos de pesquisa
    @GetMapping(value = "/buscarWorkflowsPorStatus/status/{status}")
    @Operation(summary = "Buscar workflows por status")
    public ResponseEntity<?> buscarWorkflowsPorStatus(
            @PathVariable StatusWorkflowInformativo status,
            HttpServletRequest request) {
        try {
            return ResponseEntity.ok(workflowService.buscarWorkflowsPorStatus(status));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @GetMapping(value = "/buscarWorkflowsPorNome/pesquisa/nome/{nome}")
    @Operation(summary = "Buscar workflows por nome")
    public ResponseEntity<?> buscarWorkflowsPorNome(
            @PathVariable String nome,
            HttpServletRequest request) {
        try {
            return ResponseEntity.ok(workflowService.buscarWorkflowsPorNome(nome));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @GetMapping(value = "/buscarContatosPorNome/contatos/pesquisa/nome/{nome}")
    @Operation(summary = "Buscar contatos por nome")
    public ResponseEntity<?> buscarContatosPorNome(
            @PathVariable String nome,
            HttpServletRequest request) {
        try {
            return ResponseEntity.ok(workflowService.buscarContatosPorNome(nome));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @GetMapping(value = "/buscarContatosPorEmail/contatos/pesquisa/email/{email}")
    @Operation(summary = "Buscar contatos por email")
    public ResponseEntity<?> buscarContatosPorEmail(
            @PathVariable String email,
            HttpServletRequest request) {
        try {
            return ResponseEntity.ok(workflowService.buscarContatosPorEmail(email));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @GetMapping(value = "/buscarContatosPorTelefone/contatos/pesquisa/telefone/{telefone}")
    @Operation(summary = "Buscar contatos por telefone")
    public ResponseEntity<?> buscarContatosPorTelefone(
            @PathVariable String telefone,
            HttpServletRequest request) {
        try {
            return ResponseEntity.ok(workflowService.buscarContatosPorTelefone(telefone));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    private ResponseEntity<ErrorResponse> handleError(Exception ex, HttpServletRequest request) {
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
