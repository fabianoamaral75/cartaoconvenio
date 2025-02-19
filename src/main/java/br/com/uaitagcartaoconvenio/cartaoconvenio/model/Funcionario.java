package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@EqualsAndHashCode(of ="idFuncionario")
@SequenceGenerator(name = "seq_id_funcionario", sequenceName = "seq_id_funcionario", allocationSize = 1, initialValue = 1)
@Table(name = "FUNCIONARIO")
public class Funcionario {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_funcionario")
	@Column(name = "ID_FUNCIONARIO")
	private Long idFuncionario;

	@Column(name = "DT_CRIACAO", nullable = false, insertable=true, updatable=true )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime();   

	@Column(name = "DT_ALTERACAO", nullable = false )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAlteracao = Calendar.getInstance().getTime();   

	@JsonIgnoreProperties(allowGetters = true)
	@NotNull(message = "O limite de Crédito do Funcionário deverá ser informada!")
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_LIMITE_CREDITO", referencedColumnName = "ID_LIMITE_CREDITO", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_funcionario_limite_credito"))
	private LimiteCredito limiteCredito;

	@JsonIgnoreProperties(allowGetters = true)
	@NotNull(message = "O Salário do Funcionário deverá ser informado!")
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_SALARIO", referencedColumnName = "ID_SALARIO", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_funcionario_salario"))
	private Salario salario;

	@OneToMany(mappedBy = "funcionario", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Cartao> cartao = new ArrayList<Cartao>();
	
	@JsonIgnore
	@ManyToOne(targetEntity = Pessoa.class)
	@JoinColumn(name = "ID_PESSOA", nullable = true, referencedColumnName = "ID_PESSOA", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_funcionario_pessoa"))
	private Pessoa pessoa;
	
	@ManyToOne(targetEntity = Secretaria.class)
	@JoinColumn(name = "ID_SECRETARIA", nullable = true, referencedColumnName = "ID_SECRETARIA", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_funcionario_secretaria"))
	private Secretaria secretaria;
	
	@ManyToOne(targetEntity = Entidade.class)
	@JoinColumn(name = "ID_ENTIDADE", nullable = true, referencedColumnName = "ID_ENTIDADE", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_funcionario_entidade"))
	private Entidade entidade;

	@PreUpdate
    public void preUpdate() {
		dtAlteracao = Calendar.getInstance().getTime();   
    }
	
	@Override
	public String toString() {
		return "Funcionario [idFuncionario=" + idFuncionario + ", dtCriacao=" + dtCriacao + ", dtAlteracao="
				+ dtAlteracao + ", limiteCredito=" + limiteCredito + ", salario=" + salario + ", cartao=" + cartao
				+ ", pessoa=" + pessoa + ", secretaria=" + secretaria + ", entidade=" + entidade + "]";
	}

    
}
