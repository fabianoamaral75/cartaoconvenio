package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
@EqualsAndHashCode(of ="idContatoWorkflow")
@SequenceGenerator(name = "seq_id_contato_workflow", sequenceName = "seq_id_contato_workflow", allocationSize = 1, initialValue = 1)
@Table(name = "CONTATO_WORKFLOW")
public class ContatoWorkflow {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_contato_workflow")
	@Column(name = "ID_CONTATO_WORKFLOW")
	private Long idContatoWorkflow;	
	
	@Column(name = "NOME_CONTATO", length = 100)
	private String nomeContato;

	@Column(name = "EMAIL", length = 100)
	private String email;

	@Column(name = "TELEFONE", length = 100)
	private String telefone;

	@ManyToOne(targetEntity = WorkflowInformativo.class)
	@JoinColumn(name = "ID_WORKFLOW_INFORMATIVO", nullable = true, referencedColumnName = "ID_WORKFLOW_INFORMATIVO", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_WORKFLOW_CONTATO"))
	private WorkflowInformativo workflowInformativo;

}
