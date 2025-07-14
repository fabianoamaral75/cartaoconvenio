package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusConveniada;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="idConveniados")
@SequenceGenerator(name = "seq_id_conveniados", sequenceName = "seq_id_conveniados", allocationSize = 1, initialValue = 1)
@Table(name = "CONVENIADOS")
public class Conveniados implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_conveniados")
	@Column(name = "ID_CONVENIADOS")
	private Long idConveniados;	
	
	@Column(name = "DT_CRIACAO", nullable = false, columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime();

	@Column(name = "DT_ALTERACAO", nullable = false, columnDefinition = "TIMESTAMP" )
	@Temporal(TemporalType.TIMESTAMP)
	private Date  dtAlteracao = Calendar.getInstance().getTime();

	@NotNull(message = "Dia do mês a ser realizado o pagamento após fechamento!")
	@Column(name = "DIA_PAGAMENTO", nullable = false, columnDefinition = "bigint default 0")
	private Long diaPagamento;	
		
	@Column(name = "SITE", length = 500)
	private String site;

	@Column(name = "OBS", length = 50000)
	private String obs;	
	
	@Column(name = "ANO_MES_ULTINO_FECHAMENTO", length = 6)
	private String anoMesUltinoFechamento;	
	
	@NotNull(message = "Status da Conveniada deverá ser informada!")
	@Column(name = "STATUS", nullable = false)
//	@Enumerated(EnumType.STRING)
	private StatusConveniada descStatusConveniada;
	
	@NotNull(message = "O Nicho da Conveniada deverá ser informado!")
	@ManyToOne(targetEntity = Nicho.class)
	@JoinColumn(name = "ID_NICHO", referencedColumnName = "ID_NICHO", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_conveniados_nicho"))
	private Nicho nicho;
	
	@NotNull(message = "O Ramo da Atividade da Conveniada deverá ser informado!")
	@ManyToOne(targetEntity = RamoAtividade.class)
	@JoinColumn(name = "ID_RAMO_ATIVIDADE", referencedColumnName = "ID_RAMO_ATIVIDADE", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_conveniados_ramo_atividade"))
	private RamoAtividade ramoAtividade;
	
    @Column(name = "IS_TAXAS_FAIXA_VENDAS", nullable = false, columnDefinition = "boolean default true")
    private Boolean isTaxasFaixaVendas;


	@OneToMany(mappedBy = "conveniados", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference // Indica que este lado DEVE ser serializado
	private List<CicloPagamentoVenda> CicloPagamentoVenda = new ArrayList<CicloPagamentoVenda>();

	@NotNull(message = "A Taxa de Desconto da Conveniada deverá ser informado!")
	@OneToMany(mappedBy = "conveniados", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<TaxaConveniados> taxaConveniados = new ArrayList<TaxaConveniados>();

	@OneToOne(mappedBy = "conveniados", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	private Pessoa pessoa = new Pessoa();
	private Pessoa pessoa; // Remova o = new Pessoa()

	@OneToMany(mappedBy = "conveniados", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ContratoConveniado> cartao = new ArrayList<ContratoConveniado>();

	@OneToMany(mappedBy = "conveniados", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<TaxaExtraConveniada> taxaExtraConveniada = new ArrayList<TaxaExtraConveniada>();

	@OneToMany(mappedBy = "conveniados", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ContratoConveniado> contratoConveniado = new ArrayList<ContratoConveniado>();
	
	@OneToMany(mappedBy = "conveniados", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TaxaConveniadaEntidade> taxasEntidades = new ArrayList<>();

    // Método auxiliar para adicionar contrato
    public void adicionarContrato(ContratoConveniado contrato) {
        contratoConveniado.add(contrato);
        contrato.setConveniados(this);
    }	
	
	@PreUpdate
    public void preUpdate() {
		dtAlteracao = Calendar.getInstance().getTime();
    }
	
    @PrePersist
    public void prePersist() {
        dtCriacao   = Calendar.getInstance().getTime();
        dtAlteracao = Calendar.getInstance().getTime();
    }

 // Método para gerenciar relacionamento bidirecional
    public void setPessoa(Pessoa pessoa) {
        if (this.pessoa != null) {
            this.pessoa.setConveniados(null);
        }
        this.pessoa = pessoa;
        if (pessoa != null && pessoa.getConveniados() != this) {
            pessoa.setConveniados(this);
        }
    }
    
}
