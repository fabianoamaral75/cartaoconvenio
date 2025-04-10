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
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.PessoaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Pessoa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PessoaDTO;
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
	public ResponseEntity<List<PessoaDTO>> getPessoaFisicaByCpf( @PathVariable("cpf") String cpf ) throws ExceptionCustomizada{

		List<Pessoa> listPessoa = pessoaService.getPessoaFisicaByCpf(cpf);
		
		if(listPessoa == null) {
			throw new ExceptionCustomizada("Não existe a Pessoa com este CPJ: " + cpf );
		}
		
		List<PessoaDTO> dto = PessoaMapper.INSTANCE.toListDto(listPessoa); 
		
		return new ResponseEntity<List<PessoaDTO>>(dto, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getPessoaFisicaByNome/{nomePessoa}")
	public ResponseEntity<List<PessoaDTO>> getPessoaFisicaByNome( @PathVariable("nomePessoa") String nomePessoa ) throws ExceptionCustomizada{

		List<Pessoa> listPessoa = pessoaService.getPessoaFisicaByNome(nomePessoa);
		
		if(listPessoa == null) {
			throw new ExceptionCustomizada("Não existe a Pessoa com este nome: " + nomePessoa );
		}
		
		List<PessoaDTO> dto = PessoaMapper.INSTANCE.toListDto(listPessoa); 
		
		return new ResponseEntity<List<PessoaDTO>>(dto, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getPessoaJuridicaByCnpj/{cnpj}")
	public ResponseEntity<List<PessoaDTO>> getPessoaJuridicaByCnpj( @PathVariable("cnpj") String cnpj ) throws ExceptionCustomizada{

		List<Pessoa> listPessoa = pessoaService.getPessoaJuridicaByCnpj(cnpj);
		
		if(listPessoa == null) {
			throw new ExceptionCustomizada("Não existe a Pessoa com este CNPJ: " + cnpj );
		}
		
		List<PessoaDTO> dto = PessoaMapper.INSTANCE.toListDto(listPessoa); 
		
		return new ResponseEntity<List<PessoaDTO>>(dto, HttpStatus.OK);		
	}
	


}
