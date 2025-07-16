package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Produto;

@Repository
@Transactional
public interface ProdutoRepository extends JpaRepository<Produto, Long>{

	@Query(value = "select prd                  "
                 + " from Produto         prd   "
                 + " JOIN prd.conveniados con   "
                 + " where upper(trim(prd.produto)) like upper(concat('%', ?1, '%'))"
                 + "  and con.idConveniados = ?2" 
                 )
    List<Produto> listaProdutoByNomeProduto( String produto, Long idConveniados ) ;  

	@Query(value = "select prd                    "
                 + " from Produto         prd     "
                 + " JOIN prd.conveniados con     "
                 + " where con.idConveniados = ?1 " )
	List<Produto> listaProdutoByIdConveniados( Long idConveniados ) ;  
	
	@Modifying
	@Query("UPDATE Produto p SET p.produto = :novoNome WHERE p.idProduto = :id AND p.conveniados.idConveniados = :idConveniado")
	int atualizarNomeProduto(
	    @Param("id") Long idProduto,
	    @Param("novoNome") String novoNome,
	    @Param("idConveniado") Long idConveniado);

	@Modifying
	@Query("UPDATE Produto p SET p.vlrProduto = :novoValor WHERE p.idProduto = :id AND p.conveniados.idConveniados = :idConveniado")
	int atualizarValorProduto(
	    @Param("id") Long idProduto,
	    @Param("novoValor") BigDecimal novoValor,
	    @Param("idConveniado") Long idConveniado);
}
