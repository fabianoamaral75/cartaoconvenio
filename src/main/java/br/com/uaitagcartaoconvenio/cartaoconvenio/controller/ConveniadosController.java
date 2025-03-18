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
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
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

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getConveniadosByCnpj/{cnpj}")
	public ResponseEntity<Conveniados> getConveniadosByCnpj( @PathVariable("cnpj") String cnpj ) throws ExceptionCustomizada{

		Conveniados conveniada = conveniadosService.getConveniadosByCnpj(cnpj.trim());
		
		if(conveniada == null) {
			throw new ExceptionCustomizada("Não existe a Conveniada com este CNPJ: " + cnpj );
		}
		return new ResponseEntity<Conveniados>(conveniada, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getConveniadosByNome/{nome}")
	public ResponseEntity<List<Conveniados>> getConveniadosByNome( @PathVariable("nome") String nome ) throws ExceptionCustomizada{

		List<Conveniados> conveniada = conveniadosService.getConveniadosByNome(nome.trim());
		
		if(conveniada == null) {
			throw new ExceptionCustomizada("Não existe a Conveniada com este Nome: " + nome );
		}
		return new ResponseEntity<List<Conveniados>>(conveniada, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getConveniadosByCidade/{cidade}")
	public ResponseEntity<List<Conveniados>> getConveniadosByCidade( @PathVariable("cidade") String cidade ) throws ExceptionCustomizada{

		List<Conveniados> conveniada = conveniadosService.getConveniadosByCidade(cidade.trim());
		
		if(conveniada == null) {
			throw new ExceptionCustomizada("Não existe Conveniada(os) para esta Cidade: " + cidade );
		}
		return new ResponseEntity<List<Conveniados>>(conveniada, HttpStatus.OK);		
	}

}
