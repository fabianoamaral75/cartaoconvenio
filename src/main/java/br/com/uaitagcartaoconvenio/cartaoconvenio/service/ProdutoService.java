package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Produto;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ProdutoRepository;

@Service
public class ProdutoService {


	@Autowired
	private ProdutoRepository produtoRepository;
	
	public Produto saveProduto( Produto produto ) {
		return produtoRepository.save(produto);
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Produto> getlistaProdutoByNomeProduto( String nomeProduto, Long idConveniados )  {
		
		List<Produto> listaProduto = produtoRepository.listaProdutoByNomeProduto( nomeProduto, idConveniados );
		
		return listaProduto;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Produto> getlistaProdutoByIdConveniados( Long idConveniados )  {
		
		List<Produto> listaProduto = produtoRepository.listaProdutoByIdConveniados(  idConveniados );
		
		return listaProduto;
		
	}

}
