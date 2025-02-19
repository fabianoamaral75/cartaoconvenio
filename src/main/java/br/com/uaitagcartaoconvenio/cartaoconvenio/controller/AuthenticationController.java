package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.AuthenticationDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login( @RequestBody @Valid AuthenticationDTO data ) {
		
		var uernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		var auth = authenticationManager.authenticate(uernamePassword);
		
		
		return ResponseEntity.ok().build();
		
	}
/*	
	@PostMapping("/register")
	public ResponseEntity<?> register( @RequestBody @Valid RegisterDTO data ) {
		
			
		return ResponseEntity.ok().build();
		
	}
*/	
	
}
