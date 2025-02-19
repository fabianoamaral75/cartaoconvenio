package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
@EqualsAndHashCode(of ="idRamoAtividade")
@SequenceGenerator(name = "seq_id_ramo_atividade", sequenceName = "seq_id_ramo_atividade", allocationSize = 1, initialValue = 1)
@Table(name = "RAMO_ATIVIDADE")
public class RamoAtividade implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_ramo_atividade")
	@Column(name = "ID_RAMO_ATIVIDADE")
	private Long idRamoAtividade;
	
	@NotNull(message = "O Ramo Atividade da Conveniada deverá ser informado!")
	@Column(name = "DESC_RAMO_ATIVIDADE", length = 100, nullable = false)
	private String descRamoAtividade;

	@JsonIgnore
	@ManyToOne(targetEntity = Conveniados.class)
	@JoinColumn(name = "ID_CONVENIADOS", nullable = true, referencedColumnName = "ID_CONVENIADOS", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_RAMO_ATIVIDADE_CONVE"))
	private Conveniados conveniados;

}
