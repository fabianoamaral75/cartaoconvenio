package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestabelecerLimitCreditoDTO {

	private Long idCartao;
	private Long idFuncionario;
	private BigDecimal valorRestituir; 
	
}
