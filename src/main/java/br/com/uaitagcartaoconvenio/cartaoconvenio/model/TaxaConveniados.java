package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
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
@EqualsAndHashCode(of ="idTaxaConveniados")
@SequenceGenerator(name = "seq_tx_conveiniados", sequenceName = "seq_tx_conveiniados", allocationSize = 1, initialValue = 1)
@Table(name = "TAXA_CONVEINIADOS")
public class TaxaConveniados implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tx_conveiniados")
	@Column(name = "ID_TAXA_CONVEINIADOS")
	private Long idTaxaConveniados;
	
	@Column(name = "DT_CRIACAO", nullable = false, columnDefinition = "TIMESTAMP" )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime();
	
	@Column(name = "DT_ALTERACAO", nullable = false, columnDefinition = "TIMESTAMP" )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAlteracao = Calendar.getInstance().getTime();

	@NotNull(message = "Taxa da Entidade deverá ser informado")
	@Column(name = "TAXA", nullable = false)
	private BigDecimal taxa; 
	
	@NotNull(message = "Status da taxa da Conveniada deverá ser informada!")
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusTaxaConv descStatusTaxaCon;
	
//	@JsonIgnore
	@ManyToOne(targetEntity = Conveniados.class)
	@JoinColumn(name = "ID_CONVENIADOS", nullable = true, referencedColumnName = "ID_CONVENIADOS", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_TX_CONVEINIADO_CONVENIADO"))
	private Conveniados conveniados;
	
	@PreUpdate
    public void preUpdate() {
		dtAlteracao = Calendar.getInstance().getTime();
    }

}
