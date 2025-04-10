package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
// import jakarta.persistence.PrePersist;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idUsuario")
@Entity
@Table(name = "USUARIO")
@SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", allocationSize = 1, initialValue = 1)
public class Usuario implements UserDetails{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
	@Column(name = "ID_USUARIO")
	private Long idUsuario;
	
	@Column(name = "LOGIN", length = 50, nullable = false, unique = true)
	private String login;
	
	@Column(name = "SENHA", length = 20, nullable = false)
	private String senha;
	
	@Column(name = "DT_CRIACAO", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime();

	@Column(name = "DT_ATUAL_SENHA", nullable = false)
	private Date dataAtualSenha = Calendar.getInstance().getTime();
/*	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable ( name = "USUARIO_ACESSO",uniqueConstraints = @UniqueConstraint (columnNames = {"ID_USUARIO", "ID_ACESSO"} , name = "unique_acesso_user"),
	             joinColumns = @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO", table = "USUARIO",unique = false, foreignKey = @ForeignKey(name = "fk_usuario", value = ConstraintMode.CONSTRAINT)), 
	             inverseJoinColumns = @JoinColumn(name = "ID_ACESSO",unique = false, referencedColumnName = "ID_ACESSO", table = "ACESSO",foreignKey = @ForeignKey(name = "fk_aesso", value = ConstraintMode.CONSTRAINT)) 
	)	
	private List<Acesso> acessos  = new ArrayList<Acesso>();
*/
	
	@OneToMany(mappedBy = "usuario", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UsuarioAcesso> usuarioAcesso = new ArrayList<UsuarioAcesso>();
	
	  
	@OneToOne(mappedBy = "usuario", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JsonBackReference
	private Pessoa pessoa = new Pessoa(); 
	
	
	@PreUpdate
    public void preUpdate() {
		dataAtualSenha =  Calendar.getInstance().getTime();
    }
/*	
    @PrePersist
    public void prePersist() {
        System.out.println( "this.dtCriacao: " + this.dtCriacao + " - this.dataAtualSenha: " + this.dataAtualSenha);
        this.dtCriacao   = Calendar.getInstance().getTime();
        this.dataAtualSenha = Calendar.getInstance().getTime();
        System.out.println( "this.dtCriacao: " + this.dtCriacao + " - this.dataAtualSenha: " + this.dataAtualSenha);
    }
*/    
     /* Autoridades = São os acesso, ou seja ROLE_ADMIN, ROLE_SECRETARIO, ROLE_FINACEIRO */
//	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		List<Acesso> acessos  = new ArrayList<Acesso>();
		
		for( int i = 0; i < usuarioAcesso.size(); i++ ) 
			acessos.add(usuarioAcesso.get(i).getAcesso());
		
		System.out.println(acessos);
		
		return acessos;
	}
	
//	@JsonIgnore
	@Override
	public String getPassword() {
		return this.senha;
	}

//	@JsonIgnore
	@Override
	public String getUsername() {
		return this.login;
	}
	
//	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

//	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

//	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

//	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}
	@Override
	public String toString() {
		return "Usuario [idUsuario=" + idUsuario + ", login=" + login + ", senha=" + senha + ", dtCriacao=" + dtCriacao
				+ ", dataAtualSenha=" + dataAtualSenha + ", usuarioAcesso=" + usuarioAcesso + ", pessoa=" + pessoa
				+ "]";
	}

    	
	
}
