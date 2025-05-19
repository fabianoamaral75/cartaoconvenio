package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
	
	@Column(name = "SENHA", length = 200, nullable = false)
	private String senha;
	
	@Column(name = "DT_CRIACAO", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime();

	@Column(name = "DT_ATUAL_SENHA", nullable = false)
	private Date dataAtualSenha = Calendar.getInstance().getTime();
	
	@OneToMany(mappedBy = "usuario", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UsuarioAcesso> usuarioAcesso = new ArrayList<UsuarioAcesso>();
		  
	@OneToOne(mappedBy = "usuario", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Pessoa pessoa; 
//	private Pessoa pessoa = new Pessoa(); 
	
	
	@PreUpdate
    public void preUpdate() {
		dataAtualSenha =  Calendar.getInstance().getTime();
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		if (usuarioAcesso == null) {
	        return Collections.emptyList();
	    }
	    
	    // Garante que a coleção está inicializada
	    Hibernate.initialize(usuarioAcesso);
	    
	    List<GrantedAuthority> authorities = new ArrayList<>();
	    
	    for (UsuarioAcesso ua : usuarioAcesso) {
	        if (ua != null && ua.getAcesso() != null) {
	            Hibernate.initialize(ua.getAcesso()); // Garante que o acesso está carregado
	            authorities.add(ua.getAcesso());
	        }
	    }
	    
	    return authorities;

	}
	// Adicione esta anotação para evitar problemas com proxy
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return idUsuario != null && idUsuario.equals(usuario.idUsuario);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
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

	// Método para gerenciar o relacionamento bidirecional
	public void setPessoa(Pessoa pessoa) {
	    // Verifica se já está configurado corretamente
	    if (this.pessoa == pessoa) {
	        return;
	    }
	    
	    // Remove o relacionamento anterior
	    if (this.pessoa != null) {
	        Pessoa oldPessoa = this.pessoa;
	        this.pessoa = null;
	        oldPessoa.setUsuario(null);
	    }
	    
	    // Configura o novo relacionamento
	    this.pessoa = pessoa;
	    if (pessoa != null && pessoa.getUsuario() != this) {
	        pessoa.setUsuario(this);
	    }
	}
}
