package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	
}
