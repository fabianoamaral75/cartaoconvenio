
//EmprestimoRequestDTO.java
package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoRequestDTO {

 @NotNull(message = "ID do funcionário é obrigatório")
 private Long idFuncionario;

 @NotNull(message = "Valor solicitado é obrigatório")
 @DecimalMin(value = "0.01", message = "Valor solicitado deve ser maior que zero")
 private BigDecimal valorSolicitado;
 
 @NotNull(message = "Valor da Taxa de Juros é obrigatório")
 @DecimalMin(value = "0.01", inclusive = false, message = "Valor da Taxa de Juros deve ser maior que zero")
 private BigDecimal taxaBase;
 
 @NotNull(message = "Valor da Taxa de Acréscimo parcelas acima de 12 meses é obrigatório")
 @DecimalMin(value = "0.00", inclusive = true, message = "Valor da Taxa de Acréscimo parcelas acima de 12 meses não deve ser negativo")
 private BigDecimal acrescimoParcelasAcima12Meses;
 
 @NotNull(message = "Desconto para valores altos é obrigatório")
 @DecimalMin(value = "0.00", inclusive = true, message = "Desconto para valores altos não deve ser negativo")
 private BigDecimal descontoValoresAlto;

 @NotNull(message = "Taxa desconto para valores altos é obrigatório")
 @DecimalMin(value = "0.00", inclusive = true, message = "Taxa desconto para valores altos não deve ser negativo")
 private BigDecimal taxaDescontoValoresAlto;

 @NotNull(message = "Quantidade de parcelas é obrigatória")
 @Min(value = 1, message = "Quantidade de parcelas deve ser no mínimo 1")
 private Integer quantidadeParcelas;

 private String observacao;
}