package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="idContatoWorkflow")
public class ContatoWorkflowDTO {
	private Long idContatoWorkflow;	
	private String nomeContato;
	private String email;
	private String telefone;

}
