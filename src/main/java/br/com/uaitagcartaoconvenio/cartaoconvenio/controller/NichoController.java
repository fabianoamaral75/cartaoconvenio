package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	
	@ResponseBody
	@GetMapping(value = "/getAllNicho")
	public ResponseEntity<List<Nicho>> getAllNicho(  ) throws ExceptionCustomizada{

		List<Nicho> nicho = nichoRepository.getAllNicho();
		
		if(nicho == null) {
			throw new ExceptionCustomizada("Não existe Nicho cadastradas!" );
		}
		return new ResponseEntity<List<Nicho>>(nicho, HttpStatus.OK);		
	}

	@ResponseBody
	@GetMapping(value = "/findNichoByNome/{nomeNicho}")
	public ResponseEntity<List<Nicho>> findNichoByNome( @PathVariable("nomeNicho") String nomeNicho ) throws ExceptionCustomizada{

		List<Nicho> nicho = nichoRepository.findNichoNome(nomeNicho);
		
		if(nicho == null) {
			throw new ExceptionCustomizada("Não existe Nicho cadastradas!" );
		}
		return new ResponseEntity<List<Nicho>>(nicho, HttpStatus.OK);		
	}

	@ResponseBody
	@GetMapping(value = "/findNichoNome/{id}")
	public ResponseEntity<Nicho> findNichoById( @PathVariable("id") Long id ) throws ExceptionCustomizada{

		Nicho nicho = nichoRepository.findNichoById(id);
		
		if(nicho == null) throw new ExceptionCustomizada("Não existe Nicho cadastradas!" );
		
		return new ResponseEntity<Nicho>(nicho, HttpStatus.OK);		
	}

}
