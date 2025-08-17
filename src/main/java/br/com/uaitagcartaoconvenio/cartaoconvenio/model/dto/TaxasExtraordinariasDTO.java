package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaxasExtraordinariasDTO {
	
    private Long idTaxasExtraordinarias;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 400, message = "Descrição deve ter no máximo 400 caracteres")
    private String descricaoTaxaExtraordinarias;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dataCriacao;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtAlteracao;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
    @Digits(integer = 16, fraction = 2, message = "Valor deve ter no máximo 2 casas decimais")
    private BigDecimal valorTaxaExtraordinarias;

    @NotBlank(message = "Status é obrigatório")
    @Size(max = 200, message = "Status deve ter no máximo 200 caracteres")
    private String statusTaxa;
}