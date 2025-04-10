package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusReceber;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idContasReceber")
public class ContasReceberDTO {
	    private Long idContasReceber;
	    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
	    private Date dtCriacao;
	    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
	    private Date dtAlteracao;
	    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
	    private Date dtPrevisaoRecebimento;
	    private String anoMes;
	    private StatusReceber descStatusReceber;
	    private BigDecimal valorReceber;
	    private BigDecimal valorCalcTaxaEntidadeCiclo;
	    private String docAutenticacaoBanco;
	    private String observacao;
	    private String nomeArquivo;
	    private String conteudoBase64;
	    private Long tamanhoBytes;
	    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
	    private Date dataUpload;
	    private TaxaEntidadeResumoDTO taxaEntidade;
	    private EntidadeResumoDTO entidade;
	    private List<FechamentoEntContasReceberDTO> fechamentoEntContasReceber;

}
