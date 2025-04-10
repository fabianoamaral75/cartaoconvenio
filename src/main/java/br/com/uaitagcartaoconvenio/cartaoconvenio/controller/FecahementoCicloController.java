package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.FecahementoCicloService;

@Controller
@RestController
public class FecahementoCicloController {

	@Autowired
	private FecahementoCicloService fecahementoCicloService;
	
	@ResponseBody                         
	@PostMapping(value = "/fechamentoCicloAutomatico") 
	public ResponseEntity<String> fechamentoCicloAutomatico( ) throws ExceptionCustomizada { 
		
		String retornoMensagem = fecahementoCicloService.fechamentoCiclo();
		return new ResponseEntity<String>(retornoMensagem, HttpStatus.OK);
		
	}

	
}
