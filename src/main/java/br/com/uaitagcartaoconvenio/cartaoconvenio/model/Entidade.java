package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusEmtidade;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@EqualsAndHashCode(of ="idEntidade")
@SequenceGenerator(name = "seq_id_entidade", sequenceName = "seq_id_entidade", allocationSize = 1, initialValue = 1)
@Table(name = "ENTIDADE")

public class Entidade implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_entidade")
	@Column(name = "ID_ENTIDADE")
	private Long idEntidade;
	
	@Column(name = "DT_CRIACAO", nullable = false, insertable=false, updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime(); 

	@Column(name = "DT_CRIACAO", nullable = false )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAlteracao = Calendar.getInstance().getTime(); 
	
	@Column(name = "SITE", length = 500)
	private String site;

	@Column(name = "OBS", length = 50000)
	private String obs;	
    
	@NotNull(message = "O Nome Entidade deverá ser informado!")
	@Column(name = "NOME_ENTIDADE", length = 300, nullable = false)
	private String nomeEntidade;
    
	@NotNull(message = "O CNPJ da Entidade deverá ser informado!")
	@Column(name = "CNPJ", length = 14, nullable = false)
	private String cnpj;

	@Column(name = "INSC_ESTADUAL", length = 50)
	private String inscEstadual;
	
	@Column(name = "INSC_MUNICIPAL", length = 50)
	private String inscMunicipal;

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
	
	
	@OneToMany(mappedBy = "entidade", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Secretaria> secretaria = new ArrayList<Secretaria>();
	
	@NotNull(message = "A Taxa da Entidadedeve(rá ser informado!")
	@OneToMany(mappedBy = "entidade", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<TaxaEntidade> taxaEntidade = new ArrayList<TaxaEntidade>();

	@NotNull(message = "Status da Entidade deverá ser informada!")
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusEmtidade descStatusEmtidade;
	
	@JsonIgnore
	@OneToOne(mappedBy = "entidade", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Funcionario funcionario = new Funcionario();

	@NotNull(message = "A Taxa Base para o cáucula da analise de crédito do funcionário, deverá ser informado!")
	@OneToMany(mappedBy = "entidade", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<TaxaCalcLimiteCreditoFunc> taxaCalcLimiteCreditoFunc = new ArrayList<TaxaCalcLimiteCreditoFunc>();  
	
	@PreUpdate
    public void preUpdate() {
		dtAlteracao = Calendar.getInstance().getTime(); 
    }

}
