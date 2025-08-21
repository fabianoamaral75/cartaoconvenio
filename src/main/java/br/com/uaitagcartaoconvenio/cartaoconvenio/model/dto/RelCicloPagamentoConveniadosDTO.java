package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelCicloPagamentoConveniadosDTO {

	private Long idCicloPagamentoVenda;
	private String anoMes;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
	private Date dtCriacao;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
	private Date dtAlteracao;
	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
	private Date dtPagamento;
    private String observacao;
	private BigDecimal vlrCicloBruto;          // VALOR_CICLO_BRUTO
	private BigDecimal vlrTaxaSecundaria;      // VALOR_TAXA_SECUNDARIA
	private BigDecimal vlrLiquido;             // VALOR_LIQUIDO
	private BigDecimal vlrTaxaExtraPercentual; // VALOR_TAXA_EXTRA_PERCENTUAL
	private BigDecimal vlrTaxaExtraValor;      // VALOR_TAXA_EXTRA_VALOR
	private BigDecimal vlrLiquidoPagamento;    // VALOR_LIQUIDO_PAGAMENTO
	private BigDecimal vlrTaxasFaixaVendas;    // VALOR_TAXAS_FAIXA_VENDAS
	private Long idConveniados;	
	private String site;
	private String anoMesUltinoFechamento;	
    private Long idTaxasFaixaVendas;
    private String descricaoTaxa;
	private String bairro;
	private String cep;
	private String cidade;
	private String email;
	private String logradoro;
	private String numero;
	private String telefone;
	private String uf;
	private String cnpj;
	private String razaoSocial;

}
