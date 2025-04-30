package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idLimiteCredito")
public class LimiteCreditoResumoDTO {
	private Long idLimiteCredito;
	private BigDecimal limite; 
	private BigDecimal valorUtilizado; 
	private Date dtCriacao;
	private Date dtAlteracao;
}
