package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusRestabeleceLimiteCredito;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaPg;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaReceb;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendas;
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
@EqualsAndHashCode(of = "idVenda")
@SequenceGenerator(name = "seq_id_venda", sequenceName = "seq_id_venda", allocationSize = 1, initialValue = 1)
@Table(name = "VENDA")
public class Venda implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_venda")
	@Column(name = "ID_VENDA")
	private Long idVenda;

	@NotNull(message = "O Valor da Venda deverá ser informado")
	@Column(name = "VALOR_VENDA", nullable = false)
	private BigDecimal valorVenda; 
	
	@NotNull(message = "O Valor calculado da venda com a taxa da conveniada deverá ser informado")
	@Column(name = "VALOR_CALC_TAXA_CONVENIADO", nullable = false)
	private BigDecimal valorCalcTaxaConveniado; 

	@NotNull(message = "O Valor calculado da venda com a taxa da Entidade deverá ser informado")
	@Column(name = "VALOR_CALC_TAXA_ENTIDADE", nullable = false)
	private BigDecimal valorCalcTaxaEntidade; 

	@NotNull(message = "O Ano e Mês referência da venda deverá ser informado!")
	@Column(name = "ANO_MES", length = 6, nullable = false)
	private String anoMes;
	
	@Column(name = "DT_VENDA", nullable = false, columnDefinition = "TIMESTAMP" )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtVenda = Calendar.getInstance().getTime(); 

	@Column(name = "DT_ALTERACAO", nullable = false, columnDefinition = "TIMESTAMP" )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAlteracao = Calendar.getInstance().getTime(); 

	@NotNull(message = "O Login do User do usuário realizado da venda deverá ser informado!")
	@Column(name = "LOGIN_USER", length = 100, nullable = false)
	private String loginUser;

	@NotNull(message = "Status da Conta a Receber da Entidade deverá ser informada!")
	@Column(name = "STATUS_VENDA_RECEBIDA", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusVendaReceb descStatusVendaReceb;
	
	@NotNull(message = "Status da Conta a Receber da Entidade deverá ser informada!")
	@Column(name = "STATUS_VENDA_PAGA", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusVendaPg descStatusVendaPg;

	@NotNull(message = "Status da Conta a Receber da Entidade deverá ser informada!")
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusVendas descStatusVendas;

	@NotNull(message = "Status Referênte ao Limite de Crédito a Ser Restabelecido!")
	@Column(name = "STATUS_LIMITE_CREDITO_RESTABELECIDO", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusRestabeleceLimiteCredito descRestLimiteCredito;

	@ManyToOne(targetEntity = Conveniados.class)
	@JoinColumn(name = "ID_CONVENIADOS", nullable = true, referencedColumnName = "ID_CONVENIADOS", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_VENDA_CONVENIADO"))
	private Conveniados conveniados;
	
	@NotNull(message = "O Taxa configurada para desconto na venda da Conveniada deverá ser informada!")
	@ManyToOne(targetEntity = TaxaConveiniados.class)
	@JoinColumn(name = "ID_TAXA_CONVEINIADOS", referencedColumnName = "ID_TAXA_CONVEINIADOS", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_VENDA_TX_CONVEINIADO"))
	private TaxaConveiniados taxaConveiniados;

	@NotNull(message = "O Taxa configurada para desconto aplicada para a Prefeitura deverá ser informada!")
	@ManyToOne(targetEntity = TaxaEntidade.class)
	@JoinColumn(name = "ID_TAXA_ENTIDADE", referencedColumnName = "ID_TAXA_ENTIDADE", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_VENDA_TX_ENTIDADE"))
	private TaxaEntidade taxaEntidade;

	@NotNull(message = "O Cartão do funcionario realizador da compra deverá ser informada!")
	@ManyToOne(targetEntity = Cartao.class)
	@JoinColumn(name = "ID_CARTAO", referencedColumnName = "ID_CARTAO", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_VENDA_CARTAO"))
	private Cartao cartao;

	@OneToMany(mappedBy = "venda", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ItensVenda> itensVenda = new ArrayList<ItensVenda>();
	
	@PreUpdate
    public void preUpdate() {
		dtAlteracao = Calendar.getInstance().getTime(); 
    }

}
