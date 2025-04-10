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
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.ConveniadosMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.PessoaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Pessoa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ConveniadosDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PessoaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.ConveniadosService;

@Controller
public class ConveniadosController {

	@Autowired
	private ConveniadosService conveniadosService;
	
//	@Autowired
//	private PessoaService pessoaService;
	
	@ResponseBody
	@PostMapping(value = "/salvarConveniados")
	public ResponseEntity<PessoaDTO> salvarConveniados( @RequestBody Pessoa pessoa ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( pessoa == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Conveniada. Valores vazios!");

		pessoa = conveniadosService.salvarConveniadosService(pessoa);
		
		PessoaDTO dto = PessoaMapper.INSTANCE.toDto( pessoa);
		
		return new ResponseEntity<PessoaDTO>(dto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getConveniadosByCnpj/{cnpj}")
	public ResponseEntity<ConveniadosDTO> getConveniadosByCnpj( @PathVariable("cnpj") String cnpj ) throws ExceptionCustomizada{

		Conveniados conveniada = conveniadosService.getConveniadosByCnpj(cnpj.trim());
		
		if(conveniada == null) {
			throw new ExceptionCustomizada("Não existe a Conveniada com este CNPJ: " + cnpj );
		}
		
		ConveniadosDTO dto = ConveniadosMapper.INSTANCE.toDto(conveniada); 
		
		return new ResponseEntity<ConveniadosDTO>(dto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getConveniadosByNome/{nome}")
	public ResponseEntity<List<ConveniadosDTO>> getConveniadosByNome( @PathVariable("nome") String nome ) throws ExceptionCustomizada{

		List<Conveniados> listaConveniada = conveniadosService.getConveniadosByNome(nome.trim());
		
		if(listaConveniada == null) {
			throw new ExceptionCustomizada("Não existe a Conveniada com este Nome: " + nome );
		}
		
		List<ConveniadosDTO> dto = ConveniadosMapper.INSTANCE.toListDto(listaConveniada);
		
		return new ResponseEntity<List<ConveniadosDTO>>(dto, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getConveniadosByCidade/{cidade}")
	public ResponseEntity<List<ConveniadosDTO>> getConveniadosByCidade( @PathVariable("cidade") String cidade ) throws ExceptionCustomizada{

		List<Conveniados> listaConveniada = conveniadosService.getConveniadosByCidade(cidade.trim());
		
		if(listaConveniada == null) {
			throw new ExceptionCustomizada("Não existe Conveniada(os) para esta Cidade: " + cidade );
		}
		
		List<ConveniadosDTO> dto = ConveniadosMapper.INSTANCE.toListDto(listaConveniada);
		
		return new ResponseEntity<List<ConveniadosDTO>>(dto, HttpStatus.OK);		
	}

}
