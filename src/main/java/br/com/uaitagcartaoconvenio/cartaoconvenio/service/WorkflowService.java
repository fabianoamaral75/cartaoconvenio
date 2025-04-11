package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.WorkflowMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.WorkflowInformativo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.WorkflowInformativoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.WorkflowInformativoRepository;

@Service
@Transactional
public class WorkflowService {

    @Autowired
    private WorkflowInformativoRepository workflowRepository; // Para WorkflowInformativo
 /*   
    @Autowired
    private ContatoWorkflowRepository contatoRepository; // Para ContatoWorkflow
*/
    @Autowired
    private WorkflowMapper mapper;

/*
    public WorkflowInformativoDTO criarWorkflow(WorkflowInformativoDTO dto) {
        // 1. Salva primeiro o Workflow principal
        WorkflowInformativo entity = mapper.toEntity(dto);
        WorkflowInformativo savedWorkflow = workflowRepository.save(entity);
        // 2. Salva os contatos associados
        if (dto.getContatoWorkflow() != null) {
            dto.getContatoWorkflow().forEach(contatoDto -> {
                ContatoWorkflow contato = mapper.contatoToEntity(contatoDto);
                contato.setWorkflowInformativo(savedWorkflow);
                contatoRepository.save(contato); // Usa contatoRepository
            });
        }
        
        return mapper.toDto(savedWorkflow);
    }
*/
    
    public WorkflowInformativoDTO criarWorkflow(WorkflowInformativoDTO dto) {
        // Converter DTO para entidade (isso já inclui a conversão dos contatos)
        WorkflowInformativo entity = mapper.toEntity(dto);
        
        // Salva o Workflow principal (os contatos serão salvos em cascade)
        WorkflowInformativo savedWorkflow = workflowRepository.save(entity);
        
        return mapper.toDto(savedWorkflow);
    }    
    
    public WorkflowInformativoDTO buscarPorId(Long id) {
        return workflowRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow não encontrado"));
    }

    public List<WorkflowInformativoDTO> listarTodos() {
        return workflowRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public void deletarWorkflow(Long id) {
        workflowRepository.deleteById(id);
    }
}
