package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.util.ArrayList;
import java.util.List;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusWorkflowInformativo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="idWorkflowInformativo")
@SequenceGenerator(name = "seq_id_workflow_informativo", sequenceName = "seq_id_workflow_informativo", allocationSize = 1, initialValue = 1)
@Table(name = "WORKFLOW_INFORMATIVO")
public class WorkflowInformativo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_workflow_informativo")
	@Column(name = "ID_WORKFLOW_INFORMATIVO")
	private Long idWorkflowInformativo;	

	@Column(name = "WORKFLOW_INFORMATIVO", length = 100)
	private String workflowInformativo;
	
	@NotNull(message = "Status da Workflow Informativo deverá ser informada!")
	@Column(name = "STATUS", nullable = false)
	private StatusWorkflowInformativo statusWorkflowInformativo;

	@NotNull(message = "É necessário informar pelo menos um Contato para o Workflow Informativo!")
	@OneToMany(mappedBy = "workflowInformativo", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ContatoWorkflow> contatoWorkflow = new ArrayList<ContatoWorkflow>();

}
