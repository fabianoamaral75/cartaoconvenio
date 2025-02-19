package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.RoleAcesso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="idAcesso")
@SequenceGenerator(name = "seq_id_acesso", sequenceName = "seq_id_acesso", allocationSize = 1, initialValue = 1)
@Table(name = "ACESSO")
public class Acesso implements GrantedAuthority{

	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_acesso")
	@Column(name = "ID_ACESSO")
	private Long idAcesso;

	@NotNull(message = "A campo Descrição do Acesso deverá ser informada!")
	@Column(name = "DESC_ACESSO", nullable = false)
	@Enumerated(EnumType.STRING)
	private RoleAcesso descAcesso;	
	
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return this.descAcesso.toString();
	}



	@Override
	public String toString() {
		return "Acesso [idAcesso=" + idAcesso + ", descAcesso=" + descAcesso + "]";
	}



	public Long getIdAcesso() {
		return idAcesso;
	}



	public void setIdAcesso(Long idAcesso) {
		this.idAcesso = idAcesso;
	}



	public RoleAcesso getDescAcesso() {
		return descAcesso;
	}



	public void setDescAcesso(RoleAcesso descAcesso) {
		this.descAcesso = descAcesso;
	}

	
}
