package br.com.uaitagcartaoconvenio.cartaoconvenio.security;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Usuario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
        
        // Forçar carregamento dos acessos
        Hibernate.initialize(usuario.getUsuarioAcesso());
        usuario.getUsuarioAcesso().forEach(ua -> Hibernate.initialize(ua.getAcesso()));
        
        return usuario;
    }
}
