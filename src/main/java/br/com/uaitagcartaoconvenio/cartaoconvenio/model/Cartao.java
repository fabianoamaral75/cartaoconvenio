package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCartao;
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
@EqualsAndHashCode(of ="idCartao")
@SequenceGenerator(name = "seq_id_cartao", sequenceName = "seq_id_cartao", allocationSize = 1, initialValue = 1)
@Table(name = "CARTAO")
public class Cartao implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_cartao")
	@Column(name = "ID_CARTAO")
	private Long idCartao;
	
	@NotNull(message = "A Numeração do Cartão deverá ser informado!")
	@Column(name = "NUMERACAO", length = 50, nullable = false)
	private String numeracao;
	
//	@Column(name = "DT_CRIACAO", nullable = false, insertable=false, updatable=false,columnDefinition = "TIMESTAMP")
	@Column(name = "DT_CRIACAO", nullable = false, columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime();

	@Column(name = "DT_ALTERACAO", nullable = false, columnDefinition = "TIMESTAMP" )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAlteracao = Calendar.getInstance().getTime();
	
	@NotNull(message = "A Data de Validade do Cartão deverá ser informado!")
	@Column(name = "DT_VALIDADE", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dtValidade ;
	
	@NotNull(message = "Status da taxa da Entidade deve ser informada!")
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusCartao statusCartao;

//	@JsonIgnore
	@ManyToOne(targetEntity = Funcionario.class)
	@JoinColumn(name = "ID_FUNCIONARIO", nullable = true, referencedColumnName = "ID_FUNCIONARIO", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_CARTAO_FUNCIONARIO"))
	private Funcionario funcionario;
	
	@PreUpdate
    public void preUpdate() {
		dtAlteracao =  Calendar.getInstance().getTime();
    }
	
     @PrePersist
    protected void onCreate() {
        dtCriacao = Calendar.getInstance().getTime();
    }

}
