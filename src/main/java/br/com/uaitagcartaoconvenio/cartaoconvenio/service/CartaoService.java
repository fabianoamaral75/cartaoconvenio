package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Cartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.CartaoRepository;

@Service
public class CartaoService {

	@Autowired
	private CartaoRepository cartaoRepository;
	
		
	public String getNovoCartao() {
		
		String novoCartao = null;
		 while(true) {
			 
			  novoCartao = GeradorCartaoDebito.gerarNumeroCartao();
			 
			  Cartao cartao = cartaoRepository.findByNumeracao(novoCartao);
			  
			  if(cartao == null) break;
		 }

		return novoCartao;
	}
	
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public Cartao getCartaoByIdFuncionario( Long idFuncionario )  {
		
		Cartao cartao = cartaoRepository.listaCartaoByIdFuncionario( idFuncionario );
		
		return cartao;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Cartao> getlistaCartaoByNomePessoa( String nomePessoa )  {
		
		List<Cartao> listaCartao = cartaoRepository.listaCartaoByNomePessoa( nomePessoa );
		
		return listaCartao;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Cartao> getlistaCartaoByIdStatus( StatusCartao statusCartao )  {
		
		List<Cartao> listaCartao = cartaoRepository.listaCartaoByIdStatus( statusCartao );
		
		return listaCartao;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public Cartao getCartaoByNumeracao( String numeracao )  {
		
		Cartao cartao = cartaoRepository.findByNumeracao( numeracao );
		
		return cartao;
		
	}
	
	
}
