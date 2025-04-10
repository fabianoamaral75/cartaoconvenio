package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.FuncionarioMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Funcionario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.FuncionarioDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.FuncionarioService;

@RestController
public class FuncionarioController {
	
	@Autowired
	private FuncionarioService funcionarioService;

	@ResponseBody
	@GetMapping(value = "/getAllFuncionarios")
	public ResponseEntity<List<FuncionarioDTO>> getAllFuncionarios(  ) throws ExceptionCustomizada{

		List<Funcionario> listaAllFuncionario = funcionarioService.getAllFuncionario();
		
		if(listaAllFuncionario == null) {
			throw new ExceptionCustomizada("Não existe Funcionário cadastradas!" );
		}
		
		List<FuncionarioDTO> dto = FuncionarioMapper.INSTANCE.toListDto(listaAllFuncionario); 
		
		return new ResponseEntity<List<FuncionarioDTO>>(dto, HttpStatus.OK);		
	}

	@ResponseBody
	@GetMapping(value = "/findEntidadeByNome/{nomeFuncionario}")
	public ResponseEntity<List<FuncionarioDTO>> findEntidadeByNome( @PathVariable("nomeFuncionario") String nomeFuncionario ) throws ExceptionCustomizada{

		List<Funcionario> listaFuncionario = funcionarioService.findFuncionarioNome( nomeFuncionario );
		
		if(listaFuncionario == null) {
			throw new ExceptionCustomizada("Não existe Funcionário cadastradas com este nome: " + nomeFuncionario);
		}
		
		List<FuncionarioDTO> dto = FuncionarioMapper.INSTANCE.toListDto(listaFuncionario); 
		
		return new ResponseEntity<List<FuncionarioDTO>>(dto, HttpStatus.OK);		
	}

	@ResponseBody
	@GetMapping(value = "/getIdFuncionario/{id}")
	public ResponseEntity<FuncionarioDTO> getEntidadesIdEntidade( @PathVariable("id") Long id ) throws ExceptionCustomizada{

		Funcionario funcionario = funcionarioService.getFuncionarioId(id);
		
		if(funcionario == null) {
			throw new ExceptionCustomizada("Não existe Funcionário relacionada ao CNPJ: " + id );
		}
		
		FuncionarioDTO dto = FuncionarioMapper.INSTANCE.toDto(funcionario);
		
		return new ResponseEntity<FuncionarioDTO>(dto, HttpStatus.OK);		
	}

}
