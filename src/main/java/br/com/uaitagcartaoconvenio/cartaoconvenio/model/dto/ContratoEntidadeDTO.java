package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContratoEntidadeDTO {

    private Long idContratoEntidade;

    @NotNull(message = "O valor mensal é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor mensal deve ser positivo")
    private BigDecimal vlrMensal;

    @NotNull(message = "O valor do contrato é obrigatório")
    @Positive(message = "O valor do contrato deve ser positivo")
    private BigDecimal vlrContrato;

    @NotNull(message = "O status é obrigatório")
    private Boolean status;

    private String observacao; // Se for obrigatório, use @NotBlank

    @NotNull(message = "O ID da entidade é obrigatório")
    private Long idEntidade;

    @NotEmpty(message = "A lista de arquivos não pode estar vazia")
    private List<Long> idArquivos;

    @NotEmpty(message = "A lista de vigências não pode estar vazia")
    private List<Long> idVigencias;

    @NotEmpty(message = "A lista de serviços não pode estar vazia")
    private List<Long> idServicos;
    
}