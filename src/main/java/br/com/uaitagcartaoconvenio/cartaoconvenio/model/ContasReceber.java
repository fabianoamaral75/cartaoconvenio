package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusReceber;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idContasReceber")
@SequenceGenerator(name = "seq_contas_receber", sequenceName = "seq_contas_receber", allocationSize = 1, initialValue = 1)
@Table(name = "CONTAS_RECEBER")
public class ContasReceber implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_contas_receber")
	@Column(name = "ID_CONTAS_RECEBER")
	private Long idContasReceber;
	
	@Column(name = "DT_CRIACAO", nullable = false, columnDefinition = "TIMESTAMP", unique = true )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime();  

	@Column(name = "DT_ALTERACAO", nullable = false, columnDefinition = "TIMESTAMP", unique = true)
	private Date dtAlteracao = Calendar.getInstance().getTime();  

	@Column(name = "DT_PREVISAO_RECEBIMENTO", columnDefinition = "DATE")
	private Date dtPrevisaoRecebimento;  

	@NotNull(message = "O Ano e Mês referência deverá ser informado!")
	@Column(name = "ANO_MES", length = 6, nullable = false)
	private String anoMes;
	
	@NotNull(message = "Status da Conta a Receber da Entidade deverá ser informada!")
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusReceber descStatusReceber;
	
	@NotNull(message = "O Valor a Receber deverá ser informado")
	@Column(name = "VALOR_RECEBER", nullable = false)
	private BigDecimal valorReceber; 
	
	@NotNull(message = "O Valor calculado da venda com a taxa da Entidade deverá ser informado")
	@Column(name = "VALOR_CALC_TAXA_ENTIDADE_CICLO", nullable = false)
	private BigDecimal valorCalcTaxaEntidadeCiclo; 

    @Column(name = "DOC_AUTENTICACAO_BANCO", length = 200, nullable = false)
    private String docAutenticacaoBanco;

    @Column(name = "observacao", columnDefinition = "TEXT", nullable = false)
    private String observacao;

    @Column(name = "nome_arquivo", length = 100, nullable = true)
    private String nomeArquivo;

    @Column(name = "conteudo_base64", columnDefinition = "TEXT", nullable = true)
    private String conteudoBase64;

    @Column(name = "tamanho_bytes")
    private Long tamanhoBytes;

    @Column(name = "data_upload", columnDefinition = "TIMESTAMP")
    private Date dataUpload;
	
	@NotNull(message = "A Taxa de Desconto da Preiveitura deverá ser informado!")
	@ManyToOne(targetEntity = TaxaEntidade.class)
	@JoinColumn(name = "ID_TAXA_ENTIDADE", referencedColumnName = "ID_TAXA_ENTIDADE", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_CONTAS_RECEBER_TAXA_ENTI"))
	private TaxaEntidade taxaEntidade = new TaxaEntidade();

	@NotNull(message = "A Conta a Receber deverá estar associada a uma Entidade, favor informar Entidade!")
	@ManyToOne(targetEntity = Entidade.class)
	@JoinColumn(name = "ID_ENTIDADE", referencedColumnName = "ID_ENTIDADE", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_CONTAS_RECEBER_ENTIDADE"))
	private Entidade entidade = new Entidade();

	@OneToMany(mappedBy = "contasReceber", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<FechamentoEntContasReceber> fechamentoEntContasReceber = new ArrayList<FechamentoEntContasReceber>();

	@PreUpdate
    public void preUpdate() {
		dtAlteracao = Calendar.getInstance().getTime(); 
    }

	
}
