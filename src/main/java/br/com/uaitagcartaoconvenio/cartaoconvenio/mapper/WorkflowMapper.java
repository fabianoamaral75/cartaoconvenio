package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContatoWorkflow;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.WorkflowInformativo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContatoWorkflowDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.WorkflowInformativoDTO;

public interface WorkflowMapper {
    WorkflowInformativoDTO toDto(WorkflowInformativo entity);
    WorkflowInformativo toEntity(WorkflowInformativoDTO dto);
    
    ContatoWorkflowDTO contatoToDto(ContatoWorkflow entity);
    ContatoWorkflow contatoToEntity(ContatoWorkflowDTO contatoDto);
}
