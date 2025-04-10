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
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.EntidadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.EntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.EntidadeService;

@RestController
public class EntidadeController {

	@Autowired
	private EntidadeService entidadeService;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@PostMapping(value = "/salvarEntidade")
	public ResponseEntity<EntidadeDTO> salvarEntidade( @RequestBody Entidade entidade ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( entidade == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Entidade. Valores vazios!");
		
		
		entidade = entidadeService.salvarEntidadeService(entidade);
		
		EntidadeDTO dto = EntidadeMapper.INSTANCE.toDto(entidade);
		
		return new ResponseEntity<EntidadeDTO>(dto, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getAllEntidades")
	public ResponseEntity<List<EntidadeDTO>> getAllEntidades(  ) throws ExceptionCustomizada{

		List<Entidade> listaEntidade = entidadeService.getAllEntidades();
		
		if(listaEntidade == null) {
			throw new ExceptionCustomizada("Não existe Entidades cadastradas!" );
		}
		
		List<EntidadeDTO> dto = EntidadeMapper.INSTANCE.toListDto(listaEntidade);
		return new ResponseEntity<List<EntidadeDTO>>(dto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getEntidadesCNPJ/{cnpj}")
	public ResponseEntity<EntidadeDTO> getEntidadesCNPJ( @PathVariable("cnpj") String cnpj ) throws ExceptionCustomizada{

		Entidade listaEntidade = entidadeService.getEntidadesCnpj(cnpj);
		
		if(listaEntidade == null) {
			throw new ExceptionCustomizada("Não existe Entidades relacionada ao CNPJ: " + cnpj );
		}
		
		EntidadeDTO dto = EntidadeMapper.INSTANCE.toDto(listaEntidade); 
		
		return new ResponseEntity<EntidadeDTO>(dto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/findEntidadeByNome/{nomeEntidade}")
	public ResponseEntity<List<EntidadeDTO>> findEntidadeByNome( @PathVariable("nomeEntidade") String nomeEntidade ) throws ExceptionCustomizada{

		List<Entidade> listaEntidade = entidadeService.findEntidadeNome( nomeEntidade );
		
		if(listaEntidade == null) {
			throw new ExceptionCustomizada("Não existe Entidades cadastradas com este nome: " + nomeEntidade);
		}
		
		List<EntidadeDTO> dto = EntidadeMapper.INSTANCE.toListDto(listaEntidade);
		
		return new ResponseEntity<List<EntidadeDTO>>(dto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getIdEntidade/{id}")
	public ResponseEntity<EntidadeDTO> getEntidadesIdEntidade( @PathVariable("id") Long id ) throws ExceptionCustomizada{

		Entidade entidade = entidadeService.getEntidadesId(id);
		
		if(entidade == null) {
			throw new ExceptionCustomizada("Não existe Entidades relacionada ao CNPJ: " + id );
		}
		
		EntidadeDTO dto = EntidadeMapper.INSTANCE.toDto(entidade);
		
		return new ResponseEntity<EntidadeDTO>(dto, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getEntidadeByCidade/{cidade}")
	public ResponseEntity<List<EntidadeDTO>> getEntidadeByCnpj( @PathVariable("cidade") String cidade ) throws ExceptionCustomizada{

		List<Entidade> listaEntidade = entidadeService.listaEntidadeByCidade(cidade);
		
		if(listaEntidade == null) {
			throw new ExceptionCustomizada("Não existe Entidades relacionada ao Cidade: " + cidade );
		}
		
		List<EntidadeDTO> dto = EntidadeMapper.INSTANCE.toListDto(listaEntidade); 
		
		return new ResponseEntity<List<EntidadeDTO>>(dto, HttpStatus.OK);		
	}


}
