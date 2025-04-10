package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.UnsupportedEncodingException;

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
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.UsuarioMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Usuario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ConveniadosDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.UauarioDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.UsuarioDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.UsuarioService;

@RestController
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@PostMapping(value = "/salvarUsuarioPessoaFisica")
	public ResponseEntity<UauarioDTO> salvarUsuarioPessoaFisica( @RequestBody Usuario userPF ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( userPF == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Usuário. Valores vazios!");
		
		if( userPF.getPessoa().getPessoaFisica() == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Usuário. Valores referente as dados da Pessoa Fisica estão vazios!");
		
		UauarioDTO uauarioPFDTO = usuarioService.salvarUsuarioPF(userPF);
		
		return new ResponseEntity<UauarioDTO>(uauarioPFDTO, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/findUsuarioPessoaFisica/{login}")
	public ResponseEntity<UauarioDTO> findUsuarioPessoaFisica(  @PathVariable("login") String login ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( login == null ) throw new ExceptionCustomizada("ERRO não foi informado o Login a ser pesquisado!");
		
		
		UauarioDTO uauarioPFDTO = usuarioService.pesquisaUsuarioPFByLongin( login );
		
		return new ResponseEntity<UauarioDTO>(uauarioPFDTO, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@PostMapping(value = "/salvarUsuarioPJConveniada")
	public ResponseEntity<ConveniadosDTO> salvarUsuarioPJConveniada( @RequestBody Usuario userPJ ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( userPJ == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Usuário. Valores vazios!");
		
		if( userPJ.getPessoa().getPessoaFisica() == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Usuário. Valores referente as dados da Pessoa Jurídica estão vazios!");
		
		ConveniadosDTO conveniadosDTO = usuarioService.salvarUsuarioPJConveniada(userPJ);
		
		return new ResponseEntity<ConveniadosDTO>(conveniadosDTO, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@PostMapping(value = "/salvarUserFuncionario")
	public ResponseEntity<UauarioDTO> salvarUserFuncionario( @RequestBody Usuario userFunc ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( userFunc == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Usuário. Valores vazios!");
		
		if( userFunc.getPessoa().getPessoaFisica() == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Usuário. Valores referente as dados da Pessoa Jurídica estão vazios!");
		
		UauarioDTO uauarioPFDTO = usuarioService.salvarUsuarioFuncionario(userFunc);
		
		return new ResponseEntity<UauarioDTO>(uauarioPFDTO, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getUsuarioByLogin/{login}")
	public ResponseEntity<UsuarioDTO> getUsuarioByLogin( @PathVariable("login") String login ) throws ExceptionCustomizada{

		Usuario usuario = usuarioService.getUsuarioByLogin(login.trim());
		
		if(usuario == null) {
			throw new ExceptionCustomizada("Não existe a Usuário com este login: " + login );
		}
		
		UsuarioDTO dto = UsuarioMapper.INSTANCE.toDto(usuario); 
		
		return new ResponseEntity<UsuarioDTO>(dto, HttpStatus.OK);		
	}
	
}
