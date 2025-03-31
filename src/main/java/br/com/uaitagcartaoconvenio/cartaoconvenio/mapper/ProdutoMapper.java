package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ProdutoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Produto;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ConveniadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ProdutoMapper {

    @Autowired
    private ConveniadosRepository conveniadosRepository;

    // Converte DTO para Entidade
    public Produto toEntity(ProdutoDTO dto) {
        Produto produto = new Produto();
        produto.setIdProduto(dto.getIdProduto());
        produto.setProduto(dto.getProduto());
        produto.setVlrProduto(dto.getVlrProduto());
        produto.setDtCadastro(dto.getDtCadastro());
        
        if (dto.getIdConveniado() != null) {
            produto.setConveniados(conveniadosRepository.findById(dto.getIdConveniado()).orElse(null));
        }
        
        return produto;
    }

    // Converte Entidade para DTO
    public ProdutoDTO toDTO(Produto produto) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setIdProduto(produto.getIdProduto());
        dto.setProduto(produto.getProduto());
        dto.setVlrProduto(produto.getVlrProduto());
        dto.setDtCadastro(produto.getDtCadastro());
        
        if (produto.getConveniados() != null) {
            dto.setIdConveniado(produto.getConveniados().getIdConveniados());
        }
        
        return dto;
    }


	
	
}