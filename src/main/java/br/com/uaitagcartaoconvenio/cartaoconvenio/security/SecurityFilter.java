package br.com.uaitagcartaoconvenio.cartaoconvenio.security;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
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
	    try {
	        var token = this.recoverToken(request);
	        
	        if (token != null) {
	            var login = tokenService.validateToken(token);
	            
	            // Lança ExceptionCustomizada em vez de UsernameNotFoundException
	            Usuario user = usuarioRepository.findByLoginWithAcessos(login)
	                .orElseThrow(() -> new ExceptionCustomizada("Usuário não encontrado e/ou Favor realizar login: " + login));

	            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	            SecurityContextHolder.getContext().setAuthentication(authentication);
	        }
	        
	        filterChain.doFilter(request, response);
	    } catch (ExceptionCustomizada ex) {  // Agora o catch é alcançável
	        handleException(ex, request, response);
	    }
	}
	
	private String recoverToken( HttpServletRequest request ) {
		
		var authHeader = request.getHeader("Authorization");
		
		if( authHeader == null ) return null;
		
		return authHeader.replace("Bearer ", "");
		
	}
	
	private void handleException(ExceptionCustomizada ex, HttpServletRequest request, HttpServletResponse response) 
		    throws IOException {
		    long timestamp = System.currentTimeMillis();
		    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		    sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
		    String dataFormatada = sdf.format(new Date(timestamp));
		    
		    ErrorResponse error = new ErrorResponse(
		        HttpStatus.BAD_REQUEST.value(),
		        ex.getMessage(),
		        request.getRequestURI(),
		        dataFormatada
		    );
		    
		    response.setStatus(HttpStatus.BAD_REQUEST.value());
		    response.setContentType("application/json");
		    response.getWriter().write(new ObjectMapper().writeValueAsString(error));
		} 


}
