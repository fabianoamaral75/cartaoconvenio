package br.com.uaitagcartaoconvenio.cartaoconvenio.model;


import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@EqualsAndHashCode(of ="idPessoaFisica")
@SequenceGenerator(name = "seq_id_pessoa_fisica", sequenceName = "seq_id_pessoa_fisica", allocationSize = 1, initialValue = 1)
@Table(name = "PESSOA_FISICA")
public class PessoaFisica implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_pessoa_fisica")
	@Column(name = "ID_PESSOA_FISICA")
	private Long idPessoaFisica;
	
	
	@JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_NASCIMENTO", nullable = true)
	private Date dtNascimento;
	
	@CPF(message = "CPF está inválido")
	@NotNull(message = "O CPF da Pessoa Fisíca deverá ser informado!")
	@Column(name = "CPF", length = 11, nullable = false)
	private String cpf;
/*
	@OneToOne
	@JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID_PESSOA", nullable = true,foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_pessoa_fisica_pessoa"))
	private Pessoa pessoa;
*/
	
//	@JsonIgnore
	@ManyToOne(targetEntity = Pessoa.class)
	@JoinColumn( name = "ID_PESSOA", 
	             nullable = true, 
	             referencedColumnName = "ID_PESSOA", 
	             foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_PESSOA_FISICA_PESSOA"))
	private Pessoa pessoa;
	
	
	@Override
	public String toString() {
		return "PessoaFisica [idPessoaFisica=" + idPessoaFisica + ", dtNascimento=" + dtNascimento + ", cpf=" + cpf
				+ ", pessoa=" + pessoa + "]";
	}			

	

}
