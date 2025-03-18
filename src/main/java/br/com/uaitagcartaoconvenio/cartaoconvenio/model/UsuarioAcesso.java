package br.com.uaitagcartaoconvenio.cartaoconvenio.model;


import java.io.Serializable;

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
@EqualsAndHashCode(of ="idUsuarioAcesso")
@SequenceGenerator(name = "seq_id_usuario_acesso", sequenceName = "seq_id_usuario_acesso", allocationSize = 1, initialValue = 1)
@Table(name = "USUARIO_ACESSO")
public class UsuarioAcesso implements Serializable {

	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_usuario_acesso")
	@Column(name = "ID_USUARIO_ACESSO")
	private Long idUsuarioAcesso;
	
	@ManyToOne(targetEntity = Acesso.class)
	@JoinColumn(name = "ID_ACESSO", referencedColumnName = "ID_ACESSO", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_USUARIO_AC_ACESSO"))	
	private Acesso acesso;
	
	@JsonIgnore
	@ManyToOne(targetEntity = Usuario.class)
	@JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_USUARIO_AC_USUARIO"))	
	private Usuario usuario;	
}
