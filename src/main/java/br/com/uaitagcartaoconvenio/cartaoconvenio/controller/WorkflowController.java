package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.WorkflowInformativoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.WorkflowService;

@RestController
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @ResponseBody
	@PostMapping(value = "/salvarWorkflowInformativo") 
    public ResponseEntity<WorkflowInformativoDTO> criar(@RequestBody WorkflowInformativoDTO dto) {    	
    	
 		WorkflowInformativoDTO saved = workflowService.criarWorkflow(dto);
 		
 		saved = workflowService.criarWorkflow(saved);
        
       // return ResponseEntity.created(URI.create("/salvarWorkflowInformativo/" + saved.getIdWorkflowInformativo())).body(saved);
       
        return new ResponseEntity<WorkflowInformativoDTO>(saved, HttpStatus.OK);
        
    }

    @ResponseBody
	@GetMapping(value = "/getWorkflowInformativoByID/{id}")
    public ResponseEntity<WorkflowInformativoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(workflowService.buscarPorId(id));
    }

    @ResponseBody
	@GetMapping(value = "/getWorkflowInformativoAll")
    public ResponseEntity<List<WorkflowInformativoDTO>> listarTodos() {
        return ResponseEntity.ok(workflowService.listarTodos());
    }

    @DeleteMapping(value = "/deleteWorkflowInformativoByID/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        workflowService.deletarWorkflow(id);
        return ResponseEntity.noContent().build();
    }
}
