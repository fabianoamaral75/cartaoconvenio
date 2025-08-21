package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idCicloPagamentoVenda")
@SequenceGenerator(name = "seq_id_ciclo_pagamento_venda", sequenceName = "seq_id_ciclo_pagamento_venda", allocationSize = 1, initialValue = 1)
@Table(name = "CICLO_PAGAMENTO_VENDA")
public class CicloPagamentoVenda implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_ciclo_pagamento_venda")
	@Column(name = "ID_CICLO_PAGAMENTO_VENDA")
	private Long idCicloPagamentoVenda;
		
	@NotNull(message = "O Ano e Mês referência do mês de fechamento do ciclo deverá ser informado!")
	@Column(name = "ANO_MES", length = 6, nullable = false)
	private String anoMes;
	
	@Column(name = "DT_CRIACAO", nullable = false, columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao;

	@Column(name = "DT_ALTERACAO", nullable = false, columnDefinition = "TIMESTAMP" )
	private Date dtAlteracao = Calendar.getInstance().getTime();
	
	@NotNull(message = "O Valor referência do fechamento do ciclo deverá ser informado")
	@Column(name = "VALOR_CICLO_BRUTO", nullable = false)
	private BigDecimal vlrCicloBruto; 

	@NotNull(message = "O Valor da taxa secundaria da conveniada deverá ser informado")
	@Column(name = "VALOR_TAXA_SECUNDARIA", nullable = false)
	private BigDecimal vlrTaxaSecundaria; 

	@NotNull(message = "O Valor líquido total do fechamento do ciclo deverá ser informado")
	@Column(name = "VALOR_LIQUIDO", nullable = false)
	private BigDecimal vlrLiquido; 
	
	@NotNull(message = "O Valor da taxa extra pelo percentual da conveniada deverá ser informado")
	@Column(name = "VALOR_TAXA_EXTRA_PERCENTUAL", nullable = false)
	private BigDecimal vlrTaxaExtraPercentual; 

	@NotNull(message = "O Valor da taxa extra pelo valor da conveniada deverá ser informado")
	@Column(name = "VALOR_TAXA_EXTRA_VALOR", nullable = false)
	private BigDecimal vlrTaxaExtraValor;
	
	@NotNull(message = "O Valor líquido total a ser pago para o fechamento do ciclo deverá ser informado")
	@Column(name = "VALOR_LIQUIDO_PAGAMENTO", nullable = false)
	private BigDecimal vlrLiquidoPagamento; 
	
	@NotNull(message = "O Valor referente a Taxas de Faixa de Vendas da conveniada deverá ser informado")
	@Column(name = "VALOR_TAXAS_FAIXA_VENDAS", nullable = false)
	private BigDecimal vlrTaxasFaixaVendas; 
		
	@Column(name = "DT_PAGAMENTO", columnDefinition = "DATE")
	private Date dtPagamento;

    @Column(name = "DOC_AUTENTICACAO_BANCO", length = 200, nullable = true)
    private String docAutenticacaoBanco;

    @Column(name = "observacao", columnDefinition = "TEXT", nullable = true)
    private String observacao;

    @Column(name = "nome_arquivo", length = 100, nullable = true)
    private String nomeArquivo;

    @Column(name = "conteudo_base64", columnDefinition = "TEXT", nullable = true)
    private String conteudoBase64;

    @Column(name = "tamanho_bytes")
    private Long tamanhoBytes;

    @Column(name = "data_upload", columnDefinition = "TIMESTAMP")
    private Date dataUpload;
    
	@Column(name = "ID_TAXA_CONVENIADOS_ENTIDATE", nullable = true)
	private Long idTaxaConveniadosEntidate;

	@NotNull(message = "Status da Conta a Receber da Entidade deverá ser informada!")
	@Column(name = "STATUS", nullable = false, unique = false)
	@Enumerated(EnumType.STRING)
	private StatusCicloPgVenda descStatusPagamento;
	
    // Adiciona relacionamento com TaxasFaixaVendas
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TAXAS_FAIXA_VENDAS", nullable = true)
	@JsonBackReference("taxaFaixaVendas-ciclos")
    private TaxasFaixaVendas taxasFaixaVendas;
	
	@ManyToOne(targetEntity = Conveniados.class)
	@JoinColumn(name = "ID_CONVENIADOS", nullable = true, referencedColumnName = "ID_CONVENIADOS", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_CICLO_PG_VENDA_CONV"))
	@JsonBackReference("conveniados-ciclos")// Indica que este lado NÃO deve ser serializado
	private Conveniados conveniados;
	
//	@NotNull(message = "O Tipo da mudança deve(m) ser informado!")
	@ManyToOne(targetEntity = TaxaConveniados.class)
	@JoinColumn(name = "ID_TAXA_CONVEINIADOS", referencedColumnName = "ID_TAXA_CONVEINIADOS", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_CICLO_PG_VENDA_TX_CONVEINIADOS"))
	private TaxaConveniados taxaConveniados;
	
	@OneToMany(mappedBy = "cicloPagamentoVenda", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<FechamentoConvItensVendas> fechamentoConvItensVendas = new ArrayList<FechamentoConvItensVendas>();
	
	
    // Adicionar relacionamento com a tabela de junção
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "cicloPagamentoVenda", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<ItemTaxaExtraConveniada> itemTaxaExtraConveniada = new ArrayList<ItemTaxaExtraConveniada>();   
    
	@PreUpdate
    public void preUpdate() {
		dtAlteracao = Calendar.getInstance().getTime(); 
    }
	
	@PrePersist
	protected void onCreate() {
	    dtCriacao = Calendar.getInstance().getTime();
	}
	
	// Adicione métodos auxiliares similares
	public void addItemTaxaExtraConveniada(ItemTaxaExtraConveniada item) {
	    itemTaxaExtraConveniada.add(item);
	    item.setCicloPagamentoVenda(this);
	}

	public void removeItemTaxaExtraConveniada(ItemTaxaExtraConveniada item) {
	    itemTaxaExtraConveniada.remove(item);
	    item.setCicloPagamentoVenda(null);
	}
	
	public void setItemTaxaExtraConveniada(List<ItemTaxaExtraConveniada> novos) {
	    // Nunca troque a referência da coleção gerenciada pelo Hibernate.
	    this.itemTaxaExtraConveniada.clear();
	    if (novos != null) {
	        for (ItemTaxaExtraConveniada it : novos) {
	            this.addItemTaxaExtraConveniada(it); // garante o back-reference: it.setCicloPagamentoVenda(this)
	        }
	    }
	}

	@Override
	public String toString() {
		return "CicloPagamentoVenda [idCicloPagamentoVenda=" + idCicloPagamentoVenda + ", anoMes=" + anoMes
				+ ", dtCriacao=" + dtCriacao + ", dtAlteracao=" + dtAlteracao + ", vlrCicloBruto=" + vlrCicloBruto
				+ ", vlrTaxaSecundaria=" + vlrTaxaSecundaria + ", vlrLiquido=" + vlrLiquido
				+ ", vlrTaxaExtraPercentual=" + vlrTaxaExtraPercentual + ", vlrTaxaExtraValor=" + vlrTaxaExtraValor
				+ ", vlrLiquidoPagamento=" + vlrLiquidoPagamento + ", vlrTaxasFaixaVendas=" + vlrTaxasFaixaVendas
				+ ", dtPagamento=" + dtPagamento + ", docAutenticacaoBanco=" + docAutenticacaoBanco + ", observacao="
				+ observacao + ", nomeArquivo=" + nomeArquivo + ", conteudoBase64=" + conteudoBase64 + ", tamanhoBytes="
				+ tamanhoBytes + ", dataUpload=" + dataUpload + ", idTaxaConveniadosEntidate="
				+ idTaxaConveniadosEntidate + ", descStatusPagamento=" + descStatusPagamento + ", taxasFaixaVendas="
				+ taxasFaixaVendas + ", conveniados=" + conveniados + ", taxaConveniados=" + taxaConveniados
				+ ", fechamentoConvItensVendas=" + fechamentoConvItensVendas + ", itemTaxaExtraConveniada="
				+ itemTaxaExtraConveniada + "]";
	}

}
