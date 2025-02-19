package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@EqualsAndHashCode(of ="idSalario")
@SequenceGenerator(name = "seq_id_salario", sequenceName = "seq_id_salario", allocationSize = 1, initialValue = 1)
@Table(name = "SALARIO")
public class Salario implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_salario")
	@Column(name = "ID_SALARIO")
	private Long idSalario;

	@NotNull(message = "O Valor do Salário Liquido do Funcionário deverá ser informado")
	@Column(name = "VALOR_LIQUIDO", nullable = false)
	private BigDecimal valorLiquido; 

	@NotNull(message = "O Valor do Salário Liquido do Funcionário deverá ser informado")
	@Column(name = "VALOR_BRUTO", nullable = false)
	private BigDecimal valorBruto; 
	
	@Column(name = "DT_CRIACAO", nullable = false )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime();

	@Column(name = "DT_ALTERACAO", nullable = false )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAlteracao = Calendar.getInstance().getTime();

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "ID_FUNCIONARIO", referencedColumnName = "ID_FUNCIONARIO", nullable = true,foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_salario_funcionario"))
	private Funcionario funcionario;	

	@PreUpdate
    public void preUpdate() {
		dtAlteracao = Calendar.getInstance().getTime();
    }
		
}
