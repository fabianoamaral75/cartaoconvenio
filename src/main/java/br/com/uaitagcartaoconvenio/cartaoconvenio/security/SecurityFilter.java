package br.com.uaitagcartaoconvenio.cartaoconvenio.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Usuario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter{
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
         var token = this.recoverToken(request);
         
         if( token != null) {
        	 var login = tokenService.validateToken(token);
        	 
        	 Usuario user = usuarioRepository.findByLoginWithAcessos(login).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login));

        	 var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        	 
        	 SecurityContextHolder.getContext().setAuthentication(authentication);
         }
         
         filterChain.doFilter(request, response);
		 
	}
	
	private String recoverToken( HttpServletRequest request ) {
		
		var authHeader = request.getHeader("Authorization");
		
		if( authHeader == null ) return null;
		
		return authHeader.replace("Bearer ", "");
		
	}

}
