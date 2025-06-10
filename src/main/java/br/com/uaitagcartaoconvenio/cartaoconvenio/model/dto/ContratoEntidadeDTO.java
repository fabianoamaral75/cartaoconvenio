package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.DecimalMin;
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

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dtCadastro;
    
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

    private List<ArqContratoEntidadeDTO> arquivos = new ArrayList<ArqContratoEntidadeDTO>();
    
    private List<VigenciaContratoEntidadeDTO> vigencias = new ArrayList<VigenciaContratoEntidadeDTO>();
    
    private List<ServicoContratoDTO> servicos = new ArrayList<ServicoContratoDTO>();
    
}