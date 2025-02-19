package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Nicho;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.NichoRepository;

@Controller
public class NichoController {

	@Autowired
	private NichoRepository nichoRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvarNicho")
	public ResponseEntity<Nicho> salvarNicho( @RequestBody Nicho nicho ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( nicho == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar o Nicho para as empresas conveniadas. Valores vazios!");
		
		
		nicho = nichoRepository.saveAndFlush(nicho);
		
		return new ResponseEntity<Nicho>(nicho, HttpStatus.OK);		
	}

}
