package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Funcionario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	public List<Funcionario> getAllFuncionario( )  {
		
		List<Funcionario> listaAllFuncionarios = funcionarioRepository.listaTodasFuncionario();
		
		return listaAllFuncionarios;		
	}
	
	public List<Funcionario> findFuncionarioNome( String nomeFuncionario )  {
		
		List<Funcionario> listaAllFuncionarios = funcionarioRepository.findFuncionarioNome( nomeFuncionario );
		
		return listaAllFuncionarios;
		
	}	

	public Funcionario getFuncionarioId( Long id )  {
				
		Funcionario funcionario = funcionarioRepository.findByIdFuncionario( id );
		
		return funcionario;
		
	}
	
	
}
