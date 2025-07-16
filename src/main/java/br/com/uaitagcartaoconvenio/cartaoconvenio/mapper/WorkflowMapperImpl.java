package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContatoWorkflow;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.WorkflowInformativo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContatoWorkflowDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.WorkflowInformativoDTO;

@Component
public class WorkflowMapperImpl implements WorkflowMapper {

	@Override
	public WorkflowInformativoDTO toDto(WorkflowInformativo entity) {
	    if (entity == null) return null;
	    
	    WorkflowInformativoDTO dto = new WorkflowInformativoDTO();
	    dto.setIdWorkflowInformativo(entity.getIdWorkflowInformativo());
	    dto.setWorkflowInformativo(entity.getWorkflowInformativo());
	    dto.setStatusWorkflowInformativo(entity.getStatusWorkflowInformativo());
	    
	    // Converter lista de entidades para DTOs
	    if(entity.getContatoWorkflow() != null) {
	        dto.setContatoWorkflow(
	            entity.getContatoWorkflow().stream()
	                .map(this::contatoToDto)
	                .collect(Collectors.toList())
	        );
	    }
	    
	    return dto;
	}

	@Override
	public WorkflowInformativo toEntity(WorkflowInformativoDTO dto) {
	    if (dto == null) return null;
	    
	    WorkflowInformativo entity = new WorkflowInformativo();
	    entity.setIdWorkflowInformativo(dto.getIdWorkflowInformativo());
	    entity.setWorkflowInformativo(dto.getWorkflowInformativo());
	    entity.setStatusWorkflowInformativo(dto.getStatusWorkflowInformativo());
	    
	    // Converter lista de DTOs para entidades
	    if(dto.getContatoWorkflow() != null) {
	        entity.setContatoWorkflow(
	            dto.getContatoWorkflow().stream()
	                .map(this::contatoToEntity)
	                .collect(Collectors.toList())
	        );
	    }
	    
	    return entity;
	}
    @Override
    public ContatoWorkflowDTO contatoToDto(ContatoWorkflow entity) {
        if (entity == null) return null;
        
        ContatoWorkflowDTO dto = new ContatoWorkflowDTO();
        dto.setIdContatoWorkflow(entity.getIdContatoWorkflow());
        dto.setNomeContato(entity.getNomeContato());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        return dto;
    }

    @Override
    public ContatoWorkflow contatoToEntity(ContatoWorkflowDTO dto) {
        if (dto == null) return null;
        
        ContatoWorkflow entity = new ContatoWorkflow();
        entity.setIdContatoWorkflow(dto.getIdContatoWorkflow());
        entity.setNomeContato(dto.getNomeContato());
        entity.setEmail(dto.getEmail());
        entity.setTelefone(dto.getTelefone());
        return entity;
    }

    @Override
    public List<ContatoWorkflowDTO> contatosToDtos(List<ContatoWorkflow> contatos) {
        if (contatos == null) return null;
        
        return contatos.stream()
                .map(this::contatoToDto) // Reutiliza o método contatoToDto
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ContatoWorkflow> contatosToEntities(List<ContatoWorkflowDTO> contatosDtos) {
        if (contatosDtos == null) return null;
        
        return contatosDtos.stream()
                .map(this::contatoToEntity) // Reutiliza o método contatoToEntity
                .collect(Collectors.toList());
    }  
    
    
}
