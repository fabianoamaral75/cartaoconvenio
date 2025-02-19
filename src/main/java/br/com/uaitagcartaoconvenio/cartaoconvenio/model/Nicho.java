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
@EqualsAndHashCode(of ="idNicho")
@SequenceGenerator(name = "seq_id_nicho", sequenceName = "seq_id_nicho", allocationSize = 1, initialValue = 1)
@Table(name = "NICHO")
public class Nicho implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_nicho")
	@Column(name = "ID_NICHO")
	private Long idNicho;
	
	@NotNull(message = "O Nicho da Conveniada deverá ser informado!")
	@Column(name = "DESC_NICHO", length = 100, nullable = false)
	private String descNicho;
	
	@JsonIgnore
	@ManyToOne(targetEntity = Conveniados.class)
	@JoinColumn(name = "ID_CONVENIADOS", nullable = true, referencedColumnName = "ID_CONVENIADOS", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_NICHO_CONVE"))
	private Conveniados conveniados;

}
