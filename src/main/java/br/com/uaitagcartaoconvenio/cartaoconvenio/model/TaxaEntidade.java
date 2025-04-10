package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaEntidade;
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
@EqualsAndHashCode(of ="idTaxaEntidade")
@SequenceGenerator(name = "seq_tx_entidade", sequenceName = "seq_tx_entidade", allocationSize = 1, initialValue = 1)
@Table(name = "TAXA_ENTIDADE")
public class TaxaEntidade implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tx_entidade")
	@Column(name = "ID_TAXA_ENTIDADE")
	private Long idTaxaEntidade;
	
	@Column(name = "DT_CRIACAO", nullable = false, columnDefinition = "TIMESTAMP" )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime();
	
	@Column(name = "DT_ALTERACAO", nullable = false, columnDefinition = "TIMESTAMP" )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAlteracao = Calendar.getInstance().getTime();

	@NotNull(message = "Taxa da Entidade deverá ser informado")
	@Column(name = "TAXA_ENTIDADE", nullable = false)
	private BigDecimal taxaEntidade; 
	
	@NotNull(message = "Status da taxa da Entidade deve ser informada!")
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusTaxaEntidade statusTaxaEntidade;
	
//	@JsonIgnore
	@ManyToOne(targetEntity = Entidade.class)
	@JoinColumn(name = "ID_ENTIDADE", nullable = true, referencedColumnName = "ID_ENTIDADE", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_TAXA_ENTI_ENTIDADE"))
	private Entidade entidade;

	@PreUpdate
    public void preUpdate() {
		dtAlteracao = Calendar.getInstance().getTime();
    }
}
