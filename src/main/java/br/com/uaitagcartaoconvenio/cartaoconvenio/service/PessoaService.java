package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Pessoa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.PessoaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public Pessoa savarPassoa( Pessoa pessoa) {
		
		pessoaRepository.saveAndFlush(pessoa);
		return pessoa;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Pessoa> getPessoaFisicaByCpf( String cpf )  {
		
		String resultCpf = FuncoesUteis.removerCaracteresNaoNumericos( cpf );
		
		List<Pessoa> listaPessoaFisica = pessoaRepository.listaPessoaFisicaByCpf( resultCpf );
		
		return listaPessoaFisica;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Pessoa> getPessoaFisicaByNome( String nome )  {
		
		List<Pessoa> listaPessoaFisica = pessoaRepository.listaPessoaFisicaByNome( nome );
		
		return listaPessoaFisica;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Pessoa> getPessoaJuridicaByCnpj( String cnpj )  {
		
		String resultCnpj = FuncoesUteis.removerCaracteresNaoNumericos( cnpj );
		
		List<Pessoa> listaPessoaFisica = pessoaRepository.listaPessoaJuridicaByCnpj( resultCnpj );
		
		return listaPessoaFisica;
		
	}

}
