package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@ResponseBody
	@GetMapping(value = "/getAllEntidades")
	public ResponseEntity<List<Entidade>> getAllEntidades(  ) throws ExceptionCustomizada{

		List<Entidade> listaEntidade = entidadeService.getAllEntidades();
		
		if(listaEntidade == null) {
			throw new ExceptionCustomizada("Não existe Entidades cadastradas!" );
		}
		return new ResponseEntity<List<Entidade>>(listaEntidade, HttpStatus.OK);		
	}

	@ResponseBody
	@GetMapping(value = "/getEntidadesCNPJ/{cnpj}")
	public ResponseEntity<Entidade> getEntidadesCNPJ( @PathVariable("cnpj") String cnpj ) throws ExceptionCustomizada{

		Entidade listaEntidade = entidadeService.getEntidadesCnpj(cnpj);
		
		if(listaEntidade == null) {
			throw new ExceptionCustomizada("Não existe Entidades relacionada ao CNPJ: " + cnpj );
		}
		return new ResponseEntity<Entidade>(listaEntidade, HttpStatus.OK);		
	}

	@ResponseBody
	@GetMapping(value = "/findEntidadeByNome/{nomeEntidade}")
	public ResponseEntity<List<Entidade>> findEntidadeByNome( @PathVariable("nomeEntidade") String nomeEntidade ) throws ExceptionCustomizada{

		List<Entidade> listaEntidade = entidadeService.findEntidadeNome( nomeEntidade );
		
		if(listaEntidade == null) {
			throw new ExceptionCustomizada("Não existe Entidades cadastradas com este nome: " + nomeEntidade);
		}
		return new ResponseEntity<List<Entidade>>(listaEntidade, HttpStatus.OK);		
	}

	@ResponseBody
	@GetMapping(value = "/getIdEntidade/{id}")
	public ResponseEntity<Entidade> getEntidadesIdEntidade( @PathVariable("id") Long id ) throws ExceptionCustomizada{

		Entidade listaEntidade = entidadeService.getEntidadesId(id);
		
		if(listaEntidade == null) {
			throw new ExceptionCustomizada("Não existe Entidades relacionada ao CNPJ: " + id );
		}
		return new ResponseEntity<Entidade>(listaEntidade, HttpStatus.OK);		
	}

}
