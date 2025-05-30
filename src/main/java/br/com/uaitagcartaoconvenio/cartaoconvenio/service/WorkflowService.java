package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.WorkflowMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContatoWorkflow;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.WorkflowInformativo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.WorkflowInformativoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.WorkflowInformativoRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.DatabaseUnavailableException;

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
        
        for(ContatoWorkflow cw : entity.getContatoWorkflow()) 
        	cw.setWorkflowInformativo(entity);

        // Salva o Workflow principal (os contatos serão salvos em cascade)
        WorkflowInformativo savedWorkflow = workflowRepository.save(entity);
        
        return mapper.toDto(savedWorkflow);
    }    
/*    
    public WorkflowInformativoDTO buscarPorId(Long id) {
        return workflowRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow não encontrado"));
    }
*/    
    public WorkflowInformativoDTO buscarPorId(Long id) {
        int tentativas = 0;
        final int maxTentativas = 3;
        
        while (tentativas < maxTentativas) {
            try {
                return workflowRepository.findById(id)
                        .map(mapper::toDto)
                        .orElseThrow(() -> new ResourceNotFoundException("Workflow não encontrado"));
            } catch (JDBCConnectionException e) {
                tentativas++;
                if (tentativas == maxTentativas) {
                    // throw new DatabaseUnavailableException("Falha ao conectar com o banco de dados após " + maxTentativas + " tentativas");
                    throw new DataAccessResourceFailureException("Falha na conexão com o banco", e);
                }
                try {
                    Thread.sleep(1000 * tentativas); // Backoff exponencial
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new DatabaseUnavailableException("Operação interrompida", ie);
                }
            }
        }
        throw new DatabaseUnavailableException("Erro inesperado ao acessar o banco de dados");
    }

    public List<WorkflowInformativoDTO> listarTodos() {
        return workflowRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public String deletarWorkflow(Long id) {
        workflowRepository.deleteById(id);
        return "OK";
    }
}
