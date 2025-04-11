package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@EqualsAndHashCode(of ="idLimiteCredito")
@SequenceGenerator(name = "seq_id_limite_credito", sequenceName = "seq_id_limite_credito", allocationSize = 1, initialValue = 1)
@Table(name = "LIMITE_CREDITO")
public class LimiteCredito implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_limite_credito")
	@Column(name = "ID_LIMITE_CREDITO")
	private Long idLimiteCredito;

	@NotNull(message = "O Limite de Crédito Funcionário deverá ser informado")
	@Column(name = "LIMITE", nullable = false)
	private BigDecimal limite; 

	@NotNull(message = "O Valor Utilizado pelo Funcionário deverá ser informado")
	@Column(name = "VALOR_UTILIZADO", nullable = false)
	private BigDecimal valorUtilizado; 
	
	@Column(name = "DT_CRIACAO", nullable = false, columnDefinition = "TIMESTAMP" )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime();

	@Column(name = "DT_ALTERACAO", nullable = false, columnDefinition = "TIMESTAMP" )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAlteracao = Calendar.getInstance().getTime();

//	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "ID_FUNCIONARIO", referencedColumnName = "ID_FUNCIONARIO", nullable = true,foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_limite_credito_funcionario"))
	private Funcionario funcionario;	
			
	@PreUpdate
    public void preUpdate() {
		dtAlteracao = Calendar.getInstance().getTime();
    }
	
    @PrePersist
    public void prePersist() {
        dtCriacao   = Calendar.getInstance().getTime();
        dtAlteracao = Calendar.getInstance().getTime();
    }

}
