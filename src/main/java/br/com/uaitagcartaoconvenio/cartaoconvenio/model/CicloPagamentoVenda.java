package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	@Column(name = "VALOR_CICLO", nullable = false)
	private BigDecimal valorCiclo; 

	@NotNull(message = "O Valor calculado da venda com a taxa da conveniada deverá ser informado")
	@Column(name = "VALOR_CALC_TAXA_CONVENIADO_CICLO", nullable = false)
	private BigDecimal valorCalcTaxaConveniadoCiclo; 

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

	@NotNull(message = "Status da Conta a Receber da Entidade deverá ser informada!")
	@Column(name = "STATUS", nullable = false, unique = false)
	@Enumerated(EnumType.STRING)
	private StatusCicloPgVenda descStatusPagamento;
	
	@ManyToOne(targetEntity = Conveniados.class)
	@JoinColumn(name = "ID_CONVENIADOS", nullable = true, referencedColumnName = "ID_CONVENIADOS", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_CICLO_PG_VENDA_CONV"))
	private Conveniados conveniados;
	
	@NotNull(message = "O Tipo da mudança deve(m) ser informado!")
	@ManyToOne(targetEntity = TaxaConveiniados.class)
	@JoinColumn(name = "ID_TAXA_CONVEINIADOS", referencedColumnName = "ID_TAXA_CONVEINIADOS", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_CICLO_PG_VENDA_TX_CONVEINIADOS"))
	private TaxaConveiniados taxaConveiniados;
	
	@OneToMany(mappedBy = "cicloPagamentoVenda", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<FechamentoConvItensVendas> fechamentoConvItensVendas = new ArrayList<FechamentoConvItensVendas>();
	
	@PreUpdate
    public void preUpdate() {
		dtAlteracao = Calendar.getInstance().getTime(); 
    }
	
	@PrePersist
	protected void onCreate() {
	    dtCriacao = Calendar.getInstance().getTime();
	}
}
