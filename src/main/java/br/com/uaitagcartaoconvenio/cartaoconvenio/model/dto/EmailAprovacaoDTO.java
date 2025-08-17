package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idAntecipacao")
public class EmailAprovacaoDTO {

	private Long idAntecipacao;
	private String destinatarioPrincipal;
	private List<String> destinatariosCopia;
	
	
}
