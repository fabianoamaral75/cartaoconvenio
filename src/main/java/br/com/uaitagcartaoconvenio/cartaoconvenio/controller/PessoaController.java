package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Pessoa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.PessoaService;

@Controller
public class PessoaController {
	
	@Autowired
	private PessoaService pessoaService;

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getPessoaFisicaByCpf/{cpf}")
	public ResponseEntity<List<Pessoa>> getPessoaFisicaByCpf( @PathVariable("cpf") String cpf ) throws ExceptionCustomizada{

		List<Pessoa> listaPessoa = pessoaService.getPessoaFisicaByCpf(cpf);
		
		if(listaPessoa == null) {
			throw new ExceptionCustomizada("Não existe a Pessoa com este CPJ: " + cpf );
		}
		return new ResponseEntity<List<Pessoa>>(listaPessoa, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getPessoaFisicaByNome/{nomePessoa}")
	public ResponseEntity<List<Pessoa>> getPessoaFisicaByNome( @PathVariable("nomePessoa") String nomePessoa ) throws ExceptionCustomizada{

		List<Pessoa> listaPessoa = pessoaService.getPessoaFisicaByNome(nomePessoa);
		
		if(listaPessoa == null) {
			throw new ExceptionCustomizada("Não existe a Pessoa com este nome: " + nomePessoa );
		}
		return new ResponseEntity<List<Pessoa>>(listaPessoa, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getPessoaJuridicaByCnpj/{cnpj}")
	public ResponseEntity<List<Pessoa>> getPessoaJuridicaByCnpj( @PathVariable("cnpj") String cnpj ) throws ExceptionCustomizada{

		List<Pessoa> listaPessoa = pessoaService.getPessoaJuridicaByCnpj(cnpj);
		
		if(listaPessoa == null) {
			throw new ExceptionCustomizada("Não existe a Pessoa com este CNPJ: " + cnpj );
		}
		return new ResponseEntity<List<Pessoa>>(listaPessoa, HttpStatus.OK);		
	}
	


}
