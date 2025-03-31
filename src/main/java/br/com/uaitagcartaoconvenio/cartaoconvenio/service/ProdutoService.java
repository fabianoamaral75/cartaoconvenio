package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.ProdutoMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Produto;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ProdutoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ProdutoRepository;


@Service
public class ProdutoService {


	@Autowired
	private ProdutoRepository produtoRepository;
    
    @Autowired
    private ProdutoMapper produtoMapper;

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @Transactional(readOnly = true)
    public List<ProdutoDTO> getlistaProdutoByIdConveniados(Long id) {
    	
    	return produtoRepository.listaProdutoByIdConveniados( id ) 
        .stream()
        .map(produtoMapper::toDTO)
        .collect(Collectors.toList());

    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @Transactional(readOnly = true)
    public List<ProdutoDTO> getlistaProdutoByNomeProduto(String nomeProduto, Long idConveniados) {
        return produtoRepository.listaProdutoByNomeProduto(nomeProduto, idConveniados)
                .stream()
                .map(produtoMapper::toDTO)
                .collect(Collectors.toList());
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @Transactional
    public ProdutoDTO saveProduto(ProdutoDTO produtoDTO) {
        Produto produto = produtoMapper.toEntity(produtoDTO);
        Produto produtoSalvo = produtoRepository.save(produto);
        return produtoMapper.toDTO(produtoSalvo);
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @Transactional
    public ProdutoDTO update(Long id, ProdutoDTO produtoDTO) {
        return produtoRepository.findById(id)
                .map(existingProduto -> {
                    produtoDTO.setIdProduto(id); // Garante que o ID seja o mesmo
                    Produto produto = produtoMapper.toEntity(produtoDTO);
                    Produto produtoAtualizado = produtoRepository.save(produto);
                    return produtoMapper.toDTO(produtoAtualizado);
                })
                .orElse(null);
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @Transactional
    public void delete(Long id) {
        produtoRepository.deleteById(id);
    }	
	
	/*		
	public Produto saveProduto( Produto produto ) {
		
		
		return produtoRepository.save(produto);

	}
	
	public List<Produto> getlistaProdutoByNomeProduto( String nomeProduto, Long idConveniados )  {
		
		List<Produto> listaProduto = produtoRepository.listaProdutoByNomeProduto( nomeProduto, idConveniados );
		
		return listaProduto;
		
	}
	

	public List<Produto> getlistaProdutoByIdConveniados( Long idConveniados )  {
		
		List<Produto> listaProduto = produtoRepository.listaProdutoByIdConveniados(  idConveniados );
		
		return listaProduto;
		
	}
*/
}
