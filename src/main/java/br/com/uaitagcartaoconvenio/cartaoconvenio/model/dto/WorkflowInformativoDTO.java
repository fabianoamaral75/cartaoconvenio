package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusWorkflowInformativo;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="idWorkflowInformativo")
public class WorkflowInformativoDTO {

	private Long idWorkflowInformativo;	
	private String workflowInformativo;
	private StatusWorkflowInformativo statusWorkflowInformativo;
	private List<ContatoWorkflowDTO> contatoWorkflow = new ArrayList<ContatoWorkflowDTO>();

}
