package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusWorkflowInformativo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.WorkflowMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContatoWorkflow;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.WorkflowInformativo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContatoWorkflowDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.WorkflowInformativoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ContatoWorkflowRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.WorkflowInformativoRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.DatabaseUnavailableException;

@Service
@Transactional
public class WorkflowService {

    @Autowired
    private WorkflowInformativoRepository workflowRepository; // Para WorkflowInformativo
    
    @Autowired
    private WorkflowMapper mapper;
    
    @Autowired
    private ContatoWorkflowRepository contatoRepository;

/*    
    public WorkflowInformativoDTO criarWorkflow(WorkflowInformativoDTO dto) {
        // Converter DTO para entidade (isso já inclui a conversão dos contatos)
        WorkflowInformativo entity = mapper.toEntity(dto);
        
        for(ContatoWorkflow cw : entity.getContatoWorkflow()) 
        	cw.setWorkflowInformativo(entity);

        // Salva o Workflow principal (os contatos serão salvos em cascade)
        WorkflowInformativo savedWorkflow = workflowRepository.save(entity);
        
        return mapper.toDto(savedWorkflow);
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

    
    // Operações para WorkflowInformativo
    @Transactional
    public WorkflowInformativoDTO criarWorkflow(WorkflowInformativoDTO dto) {
        WorkflowInformativo entity = mapper.toEntity(dto);
        entity = workflowRepository.save(entity);
        
        // Variável final para uso no lambda
        final WorkflowInformativo finalEntity = entity;
        
        if (dto.getContatoWorkflow() != null && !dto.getContatoWorkflow().isEmpty()) {
            List<ContatoWorkflow> contatos = mapper.contatosToEntities(dto.getContatoWorkflow());
            contatos.forEach(c -> c.setWorkflowInformativo(finalEntity)); // Usa a cópia final
            contatoRepository.saveAll(contatos);
        }
        
        return mapper.toDto(entity);
    }

    public WorkflowInformativoDTO buscarWorkflowPorId(Long id) {
        return workflowRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow não encontrado com ID: " + id));
    }

    public List<WorkflowInformativoDTO> listarTodosWorkflows() {
        return workflowRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkflowInformativoDTO atualizarWorkflow(Long id, WorkflowInformativoDTO dto) {
        WorkflowInformativo existing = workflowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow não encontrado com ID: " + id));

        existing.setWorkflowInformativo(dto.getWorkflowInformativo());
        existing.setStatusWorkflowInformativo(dto.getStatusWorkflowInformativo());
        
        return mapper.toDto(workflowRepository.save(existing));
    }

    @Transactional
    public void deletarWorkflow(Long id) {
        WorkflowInformativo workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow não encontrado com ID: " + id));
        workflowRepository.delete(workflow);
    }

    // Operações para ContatoWorkflow
    @Transactional
    public ContatoWorkflowDTO criarContato(ContatoWorkflowDTO dto) {
        WorkflowInformativo workflow = workflowRepository.findById(dto.getIdWorkflowInformativo())
                .orElseThrow(() -> new ResourceNotFoundException("Workflow não encontrado com ID: " + dto.getIdWorkflowInformativo()));

        ContatoWorkflow contato = mapper.contatoToEntity(dto);
        contato.setWorkflowInformativo(workflow);
        contato = contatoRepository.save(contato);
        
        return mapper.contatoToDto(contato);
    }

    public ContatoWorkflowDTO buscarContatoPorId(Long id) {
        return contatoRepository.findById(id)
                .map(mapper::contatoToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado com ID: " + id));
    }

    public List<ContatoWorkflowDTO> listarContatosPorWorkflow(Long idWorkflow) {
        return contatoRepository.findByWorkflowInformativoIdWorkflowInformativo(idWorkflow).stream()
                .map(mapper::contatoToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ContatoWorkflowDTO atualizarContato(Long id, ContatoWorkflowDTO dto) {
        ContatoWorkflow existing = contatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado com ID: " + id));

        existing.setNomeContato(dto.getNomeContato());
        existing.setEmail(dto.getEmail());
        existing.setTelefone(dto.getTelefone());
        
        return mapper.contatoToDto(contatoRepository.save(existing));
    }

    @Transactional
    public void deletarContato(Long id) {
        ContatoWorkflow contato = contatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado com ID: " + id));
        contatoRepository.delete(contato);
    }

    // Métodos de pesquisa
    public List<WorkflowInformativoDTO> buscarWorkflowsPorStatus(StatusWorkflowInformativo status) {
        return workflowRepository.findByStatusWorkflowInformativo(status.name()).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<WorkflowInformativoDTO> buscarWorkflowsPorNome(String nome) {
        return workflowRepository.findByWorkflowInformativoContaining(nome).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ContatoWorkflowDTO> buscarContatosPorNome(String nome) {
        return contatoRepository.findByNomeContatoContaining(nome).stream()
                .map(mapper::contatoToDto)
                .collect(Collectors.toList());
    }

    public List<ContatoWorkflowDTO> buscarContatosPorEmail(String email) {
        return contatoRepository.findByEmail(email).stream()
                .map(mapper::contatoToDto)
                .collect(Collectors.toList());
    }

    public List<ContatoWorkflowDTO> buscarContatosPorTelefone(String telefone) {
        return contatoRepository.findByTelefone(telefone).stream()
                .map(mapper::contatoToDto)
                .collect(Collectors.toList());
    }

}
