package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Usuario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.AuthenticationDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.LoginResponseDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.security.TokenService;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UsuarioService usuarioServic;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login( @RequestBody @Valid AuthenticationDTO data, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		
	    try {
	    	
			if(data == null) {
				throw new ExceptionCustomizada( "Não existe informação para ser autenticada." );
			}
	    	
	        // Não codifique a senha novamente aqui - ela deve ser enviada já codificada do cliente
	        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login().trim(), data.password().trim());
	        var auth = this.authenticationManager.authenticate(usernamePassword);
	        
	        var token = tokenService.generateToken( (Usuario) auth.getPrincipal());
	        // Retorne o token JWT ou os detalhes da autenticação
	        return ResponseEntity.ok( new LoginResponseDTO(token) );
	    } catch (Exception e) {
	    	long timestamp = System.currentTimeMillis();
	    	
	    	// Criar formato desejado
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    	sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Fuso horário opcional
	
	    	// Converter
	    	String dataFormatada = sdf.format(new Date(timestamp));
	    	
	        ErrorResponse error = new ErrorResponse(
	            HttpStatus.BAD_REQUEST.value(),
	            e.getMessage(),
	            request.getRequestURI(),
	            dataFormatada
	        );

	    	
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	    }

		
	}

	@PostMapping("/updatePassword")
	public ResponseEntity<String> updatePassword( @RequestBody @Valid AuthenticationDTO data ) {
		
		String retorno = usuarioServic.atualizaPass(data.login(), data.password());
		
		return new ResponseEntity<String>(retorno, HttpStatus.OK);		
		
		
		
		
	}
	
}
