package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroRecebimentoDTO {
	@NotBlank(message = "Observação não pode estar em branco")
	private String observacao;
	
	@NotBlank(message = "Documento de depósito não pode estar em branco")
	private String docDeposito;
	
	@NotBlank(message = "Data de pagamento não pode estar em branco")
	@Pattern(
	        regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$",
	        message = "Data deve estar no formato YYYY-MM-DD (ex: 2023-12-31)"
	    )
	private String dtPagamento;
}
