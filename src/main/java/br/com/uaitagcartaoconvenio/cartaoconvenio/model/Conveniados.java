package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusConveniada;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@EqualsAndHashCode(of ="idConveniados")
@SequenceGenerator(name = "seq_id_conveniados", sequenceName = "seq_id_conveniados", allocationSize = 1, initialValue = 1)
@Table(name = "CONVENIADOS")
public class Conveniados implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_conveniados")
	@Column(name = "ID_CONVENIADOS")
	private Long idConveniados;	
	
	@Column(name = "DT_CRIACAO", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime();

	@Column(name = "DT_ALTERACAO", nullable = false )
	@Temporal(TemporalType.TIMESTAMP)
	private Date  dtAlteracao = Calendar.getInstance().getTime();
	
	@Column(name = "SITE", length = 500)
	private String site;

	@Column(name = "OBS", length = 50000)
	private String obs;	
	
	@NotNull(message = "Status da Conveniada deverá ser informada!")
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusConveniada descStatusConveniada;
	
	@NotNull(message = "O Nicho da Conveniada deverá ser informado!")
	@ManyToOne(targetEntity = Nicho.class)
	@JoinColumn(name = "ID_NICHO", referencedColumnName = "ID_NICHO", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_conveniados_nicho"))
	private Nicho nicho;
	
	@NotNull(message = "O Ramo da Atividade da Conveniada deverá ser informado!")
	@ManyToOne(targetEntity = RamoAtividade.class)
	@JoinColumn(name = "ID_RAMO_ATIVIDADE", referencedColumnName = "ID_RAMO_ATIVIDADE", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_conveniados_ramo_atividade"))
	private RamoAtividade ramoAtividade;

	@OneToMany(mappedBy = "conveniados", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CicloPagamentoVenda> CicloPagamentoVenda = new ArrayList<CicloPagamentoVenda>();

	@NotNull(message = "A Taxa de Desconto da Conveniada deverá ser informado!")
	@OneToMany(mappedBy = "conveniados", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<TaxaConveiniados> taxaConveiniados = new ArrayList<TaxaConveiniados>();

	@OneToOne(mappedBy = "conveniados", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Pessoa pessoa = new Pessoa();

		
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
