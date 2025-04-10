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
@EqualsAndHashCode(of = "idCicloPagamentoVenda")
public class CicloPagamentoVendaResumoDTO {
	private Long idCicloPagamentoVenda;
	private String anoMes;
}
