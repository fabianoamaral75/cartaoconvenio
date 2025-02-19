package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import jakarta.persistence.OneToOne;
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
@EqualsAndHashCode(of ="idSecretaria")
@SequenceGenerator(name = "seq_id_secretaria", sequenceName = "seq_id_secretaria", allocationSize = 1, initialValue = 1)
@Table(name = "SECRETARIA")

public class Secretaria implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_secretaria")
	@Column(name = "ID_SECRETARIA")
	private Long idSecretaria;
	
	@NotNull(message = "O Nome da Secretaria da Entidade deverá ser informado!")
	@Column(name = "NOME_SECRETARIA", length = 300, nullable = false)
	private String nomeSecretaria;	
	
	@Column(name = "DT_CRIACAO", nullable = false )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao;

	@Column(name = "DT_ALTERACAO", nullable = false )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAlteracao;
	
	@NotNull(message = "O Logradoro da Entidade deverá ser informado!")
	@Column(name = "LOGRADORO", length = 300, nullable = false)
	private String logradoro;
	
	@NotNull(message = "O UF da Entidade deverá ser informado!")
	@Column(name = "UF", length = 2, nullable = false)
	private String uf;
	
	@NotNull(message = "O Cidade da Entidade deverá ser informado!")
	@Column(name = "CIDADE", length = 200, nullable = false)
	private String cidade;
	
	@NotNull(message = "O CEP da Entidade deverá ser informado!")
	@Column(name = "CEP", length = 8, nullable = false)
	private String cep;
	
	@NotNull(message = "O Número do Logradoro da Entidade deverá ser informado!")
	@Column(name = "NUMERO", length = 20, nullable = false)
	private String numero;
	
	@Column(name = "COMPLEMENTO", length = 500)
	private String complemento;
	
	@NotNull(message = "O Bairro do Logradoro da Entidade deverá ser informado!")
	@Column(name = "BAIRRO", length = 100, nullable = false)
	private String bairro;
	
	@NotNull(message = "A Secretaria deverá estar associada a uma Entidade, favor informar Entidade!")
	@ManyToOne(targetEntity = Entidade.class)
	@JoinColumn(name = "ID_ENTIDADE", referencedColumnName = "ID_ENTIDADE", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_SECRETARIA_ENTIDADE"))
	private Entidade entidade;

	@JsonIgnore
	@OneToOne(mappedBy = "secretaria", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Funcionario funcionario = new Funcionario();

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
