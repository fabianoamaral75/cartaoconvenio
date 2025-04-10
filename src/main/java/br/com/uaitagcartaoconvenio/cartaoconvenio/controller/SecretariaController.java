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
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.SecretariaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Secretaria;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.SecretariaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.SecretariaService;

@RestController
public class SecretariaController {
	
	
	@Autowired
	private SecretariaService secretariaService;
	
	@ResponseBody
	@PostMapping(value = "/salvarSecretaria")
	public ResponseEntity<SecretariaDTO> salvarSecretaria( @RequestBody Secretaria secretaria ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( secretaria == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Entidade. Valores vazios!");
		
		secretaria.setFuncionario(null);
		
		secretaria = secretariaService.salvarSecretariaService(secretaria);
		
		SecretariaDTO dto = SecretariaMapper.INSTANCE.toDto(secretaria); 
		
		return new ResponseEntity<SecretariaDTO>(dto, HttpStatus.OK);		
	}


}
