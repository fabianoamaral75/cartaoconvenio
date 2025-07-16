package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContatoWorkflow;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.WorkflowInformativo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContatoWorkflowDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.WorkflowInformativoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkflowMapper {
    WorkflowMapper INSTANCE = Mappers.getMapper(WorkflowMapper.class);

    WorkflowInformativoDTO toDto(WorkflowInformativo entity);
    WorkflowInformativo toEntity(WorkflowInformativoDTO dto);

    @Mapping(target = "idWorkflowInformativo", source = "workflowInformativo.idWorkflowInformativo")
    ContatoWorkflowDTO contatoToDto(ContatoWorkflow entity);
    
    @Mapping(target = "workflowInformativo", ignore = true)
    ContatoWorkflow contatoToEntity(ContatoWorkflowDTO dto);

    List<ContatoWorkflowDTO> contatosToDtos(List<ContatoWorkflow> contatos);
    List<ContatoWorkflow> contatosToEntities(List<ContatoWorkflowDTO> contatosDtos);
}