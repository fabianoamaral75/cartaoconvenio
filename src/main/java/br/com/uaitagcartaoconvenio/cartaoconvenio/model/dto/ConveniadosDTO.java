package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusConveniada;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idConveniados")
public class ConveniadosDTO {
    private Long idConveniados;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtCriacao;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtAlteracao;
    private Long qtyDiasPagamento;
    private String site;
    private String obs;
    private StatusConveniada descStatusConveniada;
    private NichoResumoDTO nicho;
    private RamoAtividadeResumoDTO ramoAtividade;
    private List<CicloPagamentoVendaResumoDTO> cicloPagamentoVenda;
    private List<TaxaConveniadosResumoDTO> taxaConveniados;
    private PessoaResumoDTO pessoa = new PessoaResumoDTO();

}
