package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.ProdutoMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Produto;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ProdutoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ProdutoRepository;
import jakarta.transaction.Transactional;


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
    // @Transactional(readOnly = true)
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
    // @Transactional(readOnly = true)
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
    // @Transactional
    public ProdutoDTO saveProduto(Produto produto) {
        // Produto produto = produtoMapper.toEntity(produtoDTO);
        Produto produtoSalvo = produtoRepository.save(produto);
        return produtoMapper.toDTO(produtoSalvo);
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    // @Transactional
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
    // @Transactional
    public void delete(Long id) {
        produtoRepository.deleteById(id);
    }	

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	    
    @Transactional
    public ProdutoDTO atualizarNomeProduto(Long idProduto, String novoNome, Long idConveniado) {
        int updated = produtoRepository.atualizarNomeProduto(idProduto, novoNome, idConveniado);
        
        if (updated == 0) {
            throw new IllegalArgumentException("Produto não encontrado ou não pertence ao conveniado informado");
        }
        
        return produtoRepository.findById(idProduto)
                .map(produtoMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Erro ao recuperar produto atualizado"));
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Transactional
    public ProdutoDTO atualizarValorProduto(Long idProduto, BigDecimal novoValor, Long idConveniado) {
        if (novoValor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor do produto não pode ser negativo");
        }
        
        int updated = produtoRepository.atualizarValorProduto(idProduto, novoValor, idConveniado);
        
        if (updated == 0) {
            throw new IllegalArgumentException("Produto não encontrado ou não pertence ao conveniado informado");
        }
        
        return produtoRepository.findById(idProduto)
                .map(produtoMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Erro ao recuperar produto atualizado"));
    }
   
   public Produto atualizarProduto(Long id, Produto produtoAtualizado) {
       Produto produtoExistente = produtoRepository.findById(id)
           .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + id));

       // Atualiza todos os campos
       produtoExistente.setProduto(produtoAtualizado.getProduto());
       produtoExistente.setIdProduto(produtoAtualizado.getIdProduto());
       produtoExistente.setVlrProduto(produtoAtualizado.getVlrProduto());
       produtoExistente.setConveniados(produtoAtualizado.getConveniados());

       return produtoRepository.save(produtoExistente);
   }

}
