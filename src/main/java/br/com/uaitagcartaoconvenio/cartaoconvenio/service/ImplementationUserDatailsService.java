package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Usuario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.UsuarioRepository;

@Service
public class ImplementationUserDatailsService implements UserDetailsService{

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
	//	Usuario usuario = usuarioRepository.BuscaByLogin(username);
		
	    Usuario usuario = usuarioRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
		
		if(usuario == null) {
			throw new UsernameNotFoundException("Usuário não encontrato!");
		}
        
		return new User(usuario.getLogin(), usuario.getSenha(), usuario.getAuthorities() );
	}

	
}
