package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Cartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Funcionario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.CartaoRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.FuncionarioRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;
import jakarta.transaction.Transactional;

@Service
public class CartaoService {

	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Autowired
    private FuncionarioRepository funcionarioRepository;
		
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
	public List<Cartao> getlistaCartaoByNomePessoa( String nomePessoa, Long id )  {
		
		List<Cartao> listaCartao = cartaoRepository.listaCartaoByNomePessoa( nomePessoa, id );
		
		return listaCartao;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Cartao> getlistaCartaoByIdStatus( StatusCartao statusCartao, Long id )  {
		
		List<Cartao> listaCartao = cartaoRepository.listaCartaoByIdStatus( statusCartao, id );
		
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
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @Transactional
    public Cartao criarNovoCartao(Long idFuncionario) throws ExceptionCustomizada {
    	
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new ExceptionCustomizada("Funcionário não encontrado com ID: " + idFuncionario));
        
        // Verifica e cancela cartões ativos/bloqueados existentes
        List<Cartao> cartoesAtivos = cartaoRepository.findByFuncionarioAndStatusCartaoIn(
                funcionario, 
                List.of(StatusCartao.ATIVO, StatusCartao.BLOQUEADA));
        
        cartoesAtivos.forEach(cartao -> {
            cartao.setStatusCartao(StatusCartao.CANCELADO);
            cartaoRepository.save(cartao);
        });
        
        // Cria novo cartão
        Cartao novoCartao = new Cartao();
        novoCartao.setFuncionario(funcionario);
        novoCartao.setNumeracao(getNovoCartao());
        novoCartao.setStatusCartao(StatusCartao.ATIVO);
        novoCartao.setDtValidade(FuncoesUteis.somarAnosADataAtual(5));
        
        return cartaoRepository.save(novoCartao);
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @Transactional
    public Cartao atualizarStatusCartao(Long idCartao, StatusCartao novoStatus) throws ExceptionCustomizada {
        Cartao cartao = cartaoRepository.findById(idCartao)
                .orElseThrow(() -> new ExceptionCustomizada("Cartão não encontrado com ID: " + idCartao));
        
        if (novoStatus == null) {
            throw new ExceptionCustomizada("Novo status não pode ser nulo");
        }
        
        cartao.setStatusCartao(novoStatus);
        return cartaoRepository.save(cartao);
    }	
}
