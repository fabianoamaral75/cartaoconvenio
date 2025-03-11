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
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Funcionario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.FuncionarioService;

@RestController
public class FuncionarioController {
	
	@Autowired
	private FuncionarioService funcionarioService;

	@ResponseBody
	@GetMapping(value = "/getAllFuncionarios")
	public ResponseEntity<List<Funcionario>> getAllFuncionarios(  ) throws ExceptionCustomizada{

		List<Funcionario> listaAllFuncionario = funcionarioService.getAllFuncionario();
		
		if(listaAllFuncionario == null) {
			throw new ExceptionCustomizada("Não existe Funcionário cadastradas!" );
		}
		return new ResponseEntity<List<Funcionario>>(listaAllFuncionario, HttpStatus.OK);		
	}

	@ResponseBody
	@GetMapping(value = "/findEntidadeByNome/{nomeFuncionario}")
	public ResponseEntity<List<Funcionario>> findEntidadeByNome( @PathVariable("nomeFuncionario") String nomeFuncionario ) throws ExceptionCustomizada{

		List<Funcionario> listaFuncionario = funcionarioService.findFuncionarioNome( nomeFuncionario );
		
		if(listaFuncionario == null) {
			throw new ExceptionCustomizada("Não existe Funcionário cadastradas com este nome: " + nomeFuncionario);
		}
		return new ResponseEntity<List<Funcionario>>(listaFuncionario, HttpStatus.OK);		
	}

	@ResponseBody
	@GetMapping(value = "/getIdFuncionario/{id}")
	public ResponseEntity<Funcionario> getEntidadesIdEntidade( @PathVariable("id") Long id ) throws ExceptionCustomizada{

		Funcionario funcionario = funcionarioService.getFuncionarioId(id);
		
		if(funcionario == null) {
			throw new ExceptionCustomizada("Não existe Funcionário relacionada ao CNPJ: " + id );
		}
		return new ResponseEntity<Funcionario>(funcionario, HttpStatus.OK);		
	}

}
