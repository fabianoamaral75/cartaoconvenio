package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CNPJ;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PESSOA_JURIDICA")
@SequenceGenerator(name = "seq_id_pessoa_juridica", sequenceName = "seq_id_pessoa_juridica", initialValue = 1, allocationSize = 1)
public class PessoaJuridica implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_pessoa_juridica")
	@Column(name = "ID_PESSOA_JURIDICA")
	private Long idPessoaJuridica;
	
	@CNPJ(message = "CNPJ está inválido")
	@NotNull(message = "O CNPJ da Pessoa Jurídica deverá ser informado!")
	@Column(name = "CNPJ", length = 18, nullable = false)
	private String cnpj;
	
	@NotNull(message = "O Inscrição Estadual da Pessoa Jurídica deverá ser informado!")
	@Column(name = "INSC_ESTADUAL", length = 40, nullable = true)
	private String inscEstadual;
	
	@NotNull(message = "O Inscrição Municipal da Pessoa Jurídica deverá ser informado!")
	@Column(name = "INSC_MUNICIPAL", length = 40, nullable = true)
	private String inscMunicipal;

	@NotNull(message = "O Nome Fantasia da Pessoa Jurídica deverá ser informado!")
	@Column(name = "NOME_FANTASIA", length = 200, nullable = false)
	private String nomeFantasia;

	@NotNull(message = "O Razão Social da Pessoa Jurídica deverá ser informado!")
	@Column(name = "RAZAO_SOCIAL", length = 200, nullable = false)
	private String razaoSocial;
/*
	@OneToOne
	@JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID_PESSOA", nullable = true,foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_pessoa_juridica_pessoa"))
	private Pessoa pessoa;			
*/	
	@JsonIgnore
	@ManyToOne(targetEntity = Pessoa.class)
	@JoinColumn(name = "ID_PESSOA", nullable = true, referencedColumnName = "ID_PESSOA", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_PESSOA_JURIDICA_PESSOA"))
	private Pessoa pessoa;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(idPessoaJuridica);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PessoaJuridica other = (PessoaJuridica) obj;
		return Objects.equals(idPessoaJuridica, other.idPessoaJuridica);
	}

	@Override
	public String toString() {
		return "PessoaJuridica [idPessoaJuridica=" + idPessoaJuridica + ", cnpj=" + cnpj + ", inscEstadual="
				+ inscEstadual + ", inscMunicipal=" + inscMunicipal + ", nomeFantasia=" + nomeFantasia
				+ ", razaoSocial=" + razaoSocial + ", pessoa=" + pessoa + "]";
	}

	
	
}
