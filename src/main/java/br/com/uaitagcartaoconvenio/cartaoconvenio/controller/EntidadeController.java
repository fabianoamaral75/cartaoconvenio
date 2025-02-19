package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.EntidadeService;

@RestController
public class EntidadeController {

	@Autowired
	private EntidadeService entidadeService;
	
	@ResponseBody
	@PostMapping(value = "/salvarEntidade")
	public ResponseEntity<Entidade> salvarEntidade( @RequestBody Entidade entidade ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( entidade == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Entidade. Valores vazios!");
		
		
		entidade = entidadeService.salvarEntidadeService(entidade);
		
		return new ResponseEntity<Entidade>(entidade, HttpStatus.OK);		
	}
	

	
}
