package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaxaConveniadaEntidadeDTO {
    private Long id;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtCriacao;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtAlteracao;
    private String descTaxa;
    private BigDecimal vlrTaxa;
    private StatusTaxaConv statusTaxaConEnt;
    private Long idConveniados;
    private Long idEntidade;
}