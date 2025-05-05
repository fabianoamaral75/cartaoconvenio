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
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.UauarioDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.UsuarioDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.UsuarioLogadoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.UsuarioService;

@RestController
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	
    /**
     * Salvar um Usuario de Pessoa Fisica para a diministração e manutenção do sistema
     *    - 1)	Usuário ADMIN Master do Sistema
     *    - 2)	Usuário USER do Sistema
     *
     * @param Objeto Usuario, contendo todas as informações de um usuário.
     * @return Objeto DTO do tipo usuário PF "uauarioPFDTO".
     * @throws ExceptionCustomizada Se os Valores referente as dados da Pessoa Fisica estiverem vazios.
     */
	@ResponseBody
	@PostMapping(value = "/salvarUsuarioPessoaFisica")
	public ResponseEntity<UsuarioDTO> salvarUsuarioPessoaFisica( @RequestBody Usuario userPF ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( userPF == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Usuário. Valores vazios!");
		
		if( userPF.getPessoa().getPessoaFisica() == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Usuário. Valores referente as dados da Pessoa Fisica estão vazios!");
		
		UsuarioDTO dto = usuarioService.salvarUsuarioPF(userPF);
		
		return new ResponseEntity<UsuarioDTO>(dto, HttpStatus.OK);		
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

    /**
     * Salvar um Usuario de Pessoa Juridica para uma Conveniada
     *    - 1)  Usuário ADMIN Conveniado Master
     *
     * @param Objeto Usuario, contendo todas as informações de um usuário.
     * @return Objeto DTO do tipo usuário "UsuarioDTO".
     * @throws ExceptionCustomizada Se os Valores referente as dados da Pessoa Fisica estiverem vazios.
     */
	@ResponseBody
	@PostMapping(value = "/salvarUsuarioPJConveniada")
	public ResponseEntity<UsuarioDTO> salvarUsuarioPJConveniada( @RequestBody Usuario userPJ ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( userPJ == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Usuário. Valores vazios!");
	
		if( userPJ.getPessoa().getPessoaJuridica() == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Usuário. Valores referente as dados da Pessoa Jurídica estão vazios!");
		
		UsuarioDTO uauarioDTO = usuarioService.salvarUsuarioPJConveniada(userPJ);
		
		return new ResponseEntity<UsuarioDTO>(uauarioDTO, HttpStatus.OK);		
	}
	
    /**
     * Salvar um Usuario do tipo Funcionário para uma Entidade.
     *    - 1)  Usuário ADMIN / USER Entidade (Funcionário Entidade)
     *
     * @param Objeto Usuario, contendo todas as informações de um usuário.
     * @return Objeto DTO do tipo usuário "uauarioDTO".
     * @throws ExceptionCustomizada Se os Valores referente as dados da Pessoa Fisica estiverem vazios.
     */
	@ResponseBody
	@PostMapping(value = "/salvarUserFuncionario")
	public ResponseEntity<UsuarioDTO> salvarUserFuncionario( @RequestBody Usuario userFunc ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( userFunc == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Usuário. Valores vazios!");
		
		if( userFunc.getPessoa().getPessoaFisica() == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Usuário. Valores referente as dados da Pessoa Física estão vazios!");
		
		UsuarioDTO uauarioPFDTO = usuarioService.salvarUsuarioFuncionario(userFunc);
		
		return new ResponseEntity<UsuarioDTO>(uauarioPFDTO, HttpStatus.OK);		
	}

    /**
     * Salvar um Usuario do tipo Pessoa Física para uma Conveniada.
     *    - 1)	Usuário ADMIN Conveniado
     *    - 2)	Usuário USER Conveniado
     *    - 3)	Usuário USER Conveniado (Vendedor)
     *
     * @param Objeto Usuario, contendo todas as informações de um usuário.
     * @return Objeto DTO do tipo usuário "uauarioDTO".
     * @throws ExceptionCustomizada Se os Valores referente as dados da Pessoa Fisica estiverem vazios.
     */
	@ResponseBody
	@PostMapping(value = "/salvarUsuarioPFConveniada")
	public ResponseEntity<UsuarioDTO> salvarUsuarioPFConveniada( @RequestBody Usuario userPFConveniada ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( userPFConveniada == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Usuário. Valores vazios!");
		
		if( userPFConveniada.getPessoa().getPessoaFisica() == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Usuário. Valores referente as dados da Pessoa Física estão vazios!");
		
		UsuarioDTO uauarioPFDTO = usuarioService.salvarUsuarioPFConveniada(userPFConveniada);
		
		return new ResponseEntity<UsuarioDTO>(uauarioPFDTO, HttpStatus.OK);		
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
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getUsuarioLogadoById/{idUserLogado}")
	public ResponseEntity<UsuarioLogadoDTO> getUsuarioLogadoById( @PathVariable("idUserLogado") Long idUserLogado ) throws ExceptionCustomizada{

		UsuarioLogadoDTO usuarioLogado = usuarioService.validaUserLogadoByIdOrLogin( idUserLogado, null );
		
		if(usuarioLogado == null) {
			throw new ExceptionCustomizada("Não existe o ID do Usuário: " + usuarioLogado );
		}
		
		return new ResponseEntity<UsuarioLogadoDTO>(usuarioLogado, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getUsuarioLogadoByLogin/{login}")
	public ResponseEntity<UsuarioLogadoDTO> getUsuarioLogadoByLogin( @PathVariable("login") String login ) throws ExceptionCustomizada{
		
		UsuarioLogadoDTO usuarioLogado = usuarioService.validaUserLogadoByIdOrLogin( 0L, login );
		
		if(usuarioLogado == null) {
			throw new ExceptionCustomizada("Não existe o ID do Usuário: " + usuarioLogado );
		}
		
		return new ResponseEntity<UsuarioLogadoDTO>(usuarioLogado, HttpStatus.OK);		
	}
	
	
	// public Usuario validaUserLogadoByIdOrLogin( Long idUserLogado, String login )
	
}
