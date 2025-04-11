package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaCalcLimiteCredFuncionaro;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@EqualsAndHashCode(of ="idTaxaCalcLimiteCreditoFunc")
@SequenceGenerator(name = "seq_taxa_calc_limite_credito_func", sequenceName = "seq_taxa_calc_limite_credito_func", allocationSize = 1, initialValue = 1)
@Table(name = "TAXA_CALC_LIMITE_CREDITO_FUNC")
public class TaxaCalcLimiteCreditoFunc implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_taxa_calc_limite_credito_func")
	@Column(name = "ID_TAXA_CALC_LIMITE_CREDITO_FUNC")
	private Long idTaxaCalcLimiteCreditoFunc;

	@NotNull(message = "O Valor da Taxa Base para o cáucula da analise de crédito do funcionário, deverá ser informado")
	@Column(name = "TAXA_BASE", nullable = false)
	private BigDecimal taxaBase; 

	@Column(name = "DT_CRIACAO", nullable = false, columnDefinition = "TIMESTAMP" )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime();

	@Column(name = "DT_ALTERACAO", nullable = false, columnDefinition = "TIMESTAMP" )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAlteracao = Calendar.getInstance().getTime();
	
	@NotNull(message = "Status da Entidade deverá ser informada!")
	@Column(name = "STATUS", nullable = false)
	@Enumerated( EnumType.STRING)
	private StatusTaxaCalcLimiteCredFuncionaro statusTaxaCalcLimiteCredFuncionaro;

//	@JsonIgnore
	@ManyToOne(targetEntity = Entidade.class)
	@JoinColumn(name = "ID_ENTIDADE", nullable = true, referencedColumnName = "ID_ENTIDADE", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_ATIV_MUD"))
	private Entidade entidade;

	@PreUpdate
    public void preUpdate() {
		dtAlteracao = Calendar.getInstance().getTime();
    }
	@PrePersist
	protected void onCreate() {
	    dtCriacao = Calendar.getInstance().getTime();
	}
	
}
