package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="idPessoa")
@Entity
@SequenceGenerator(name = "seq_id_pessoa", sequenceName = "seq_id_pessoa", initialValue = 1, allocationSize = 1)
@Table(name = "PESSOA")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idPessoa")
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_pessoa")
	@Column(name = "ID_PESSOA")
	private Long idPessoa;

	@NotNull(message = "O Nome da Pessoa deverá ser informado!")
	@Column(name = "NOME_PESSOA", length = 300, nullable = false)
	private String nomePessoa;

	@NotNull(message = "O Logradoro da Pessoa deverá ser informado!")
	@Column(name = "LOGRADORO", length = 300, nullable = false)
	private String logradoro;
	
	@NotNull(message = "O UF da Pessoa deverá ser informado!")
	@Column(name = "UF", length = 2, nullable = false)
	private String uf;
	
	@NotNull(message = "O Cidade da Pessoa deverá ser informado!")
	@Column(name = "CIDADE", length = 200, nullable = false)
	private String cidade;
	
	@NotNull(message = "O CEP da Pessoa deverá ser informado!")
	@Column(name = "CEP", length = 8, nullable = false)
	private String cep;
	
	@NotNull(message = "O Número da Pessoa da Entidade deverá ser informado!")
	@Column(name = "NUMERO", length = 20, nullable = false)
	private String numero;
	
	@Column(name = "COMPLEMENTO", length = 500)
	private String complemento;
	
	@NotNull(message = "O Bairro da Pessoa da Entidade deverá ser informado!")
	@Column(name = "BAIRRO", length = 100, nullable = false)
	private String bairro;
	
	@NotNull(message = "O E-mail da Pessoa da Entidade deverá ser informado!")
	@Column(name = "EMAIL", length = 100, nullable = false)
	private String email;

	@NotNull(message = "O Telefone da Pessoa da Entidade deverá ser informado!")
	@Column(name = "TELEFONE", length = 13, nullable = false)
	private String telefone;
	
	@OneToOne(mappedBy = "pessoa", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private PessoaFisica pessoaFisica;
//	private PessoaFisica pessoaFisica = new PessoaFisica();

	@OneToOne(mappedBy = "pessoa", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private PessoaJuridica pessoaJuridica;
//	private PessoaJuridica pessoaJuridica = new PessoaJuridica();
		
	@OneToOne(mappedBy = "pessoa", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Funcionario funcionario;
//	private Funcionario funcionario = new Funcionario();
	
//	@JsonIgnore
	@ManyToOne(targetEntity = Usuario.class)
	@JoinColumn(name = "ID_USUARIO", nullable = false, referencedColumnName = "ID_USUARIO", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_PESSOA_UUSARIO"))
	private Usuario usuario;

//	@JsonIgnore
	@ManyToOne(targetEntity = Conveniados.class)
	@JoinColumn(name = "ID_CONVENIADOS", referencedColumnName = "ID_CONVENIADOS", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_PESSOA_CONVENIADOS"))
	private Conveniados conveniados;
	
	// Em Pessoa.java
	public void setPessoaFisica(PessoaFisica pessoaFisica) {
	    this.pessoaFisica = pessoaFisica;
	    if (pessoaFisica != null) {
	        pessoaFisica.setPessoa(this);
	    }
	}	
	// Métodos para gerenciar relacionamentos bidirecionais
	public void setUsuario(Usuario usuario) {
	    // Verifica se já está configurado corretamente
	    if (this.usuario == usuario) {
	        return;
	    }
	    
	    // Remove o relacionamento anterior
	    if (this.usuario != null) {
	        Usuario oldUsuario = this.usuario;
	        this.usuario = null;
	        oldUsuario.setPessoa(null);
	    }
	    
	    // Configura o novo relacionamento
	    this.usuario = usuario;
	    if (usuario != null && usuario.getPessoa() != this) {
	        usuario.setPessoa(this);
	    }
	}

	public void setConveniados(Conveniados conveniados) {
	    if (this.conveniados != null) {
	        this.conveniados.setPessoa(null);
	    }
	    this.conveniados = conveniados;
	    if (conveniados != null && conveniados.getPessoa() != this) {
	        conveniados.setPessoa(this);
	    }
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
	    if (this.pessoaJuridica != null) {
	        this.pessoaJuridica.setPessoa(null);
	    }
	    this.pessoaJuridica = pessoaJuridica;
	    if (pessoaJuridica != null) {
	        pessoaJuridica.setPessoa(this);
	    }
	}	
	
}
