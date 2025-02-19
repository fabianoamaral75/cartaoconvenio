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
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Pessoa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.ConveniadosService;

@Controller
public class ConveniadosController {

	@Autowired
	private ConveniadosService conveniadosService;
	
//	@Autowired
//	private PessoaService pessoaService;
	
	@ResponseBody
	@PostMapping(value = "/salvarConveniados")
	public ResponseEntity<Pessoa> salvarConveniados( @RequestBody Pessoa pessoa ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( pessoa == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Conveniada. Valores vazios!");

		pessoa = conveniadosService.salvarEntidadeService(pessoa);
		
		return new ResponseEntity<Pessoa>(pessoa, HttpStatus.OK);		
	}

}
