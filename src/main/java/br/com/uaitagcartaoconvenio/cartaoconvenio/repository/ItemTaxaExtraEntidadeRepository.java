package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ItemTaxaExtraEntidade;

@Repository
public interface ItemTaxaExtraEntidadeRepository extends JpaRepository<ItemTaxaExtraEntidade, Long> {
    List<ItemTaxaExtraEntidade> findByTaxaExtraEntidadeId(Long taxaExtraId);
    List<ItemTaxaExtraEntidade> findByContasReceberIdContasReceber(Long idContasReceber);
    
    @Query("SELECT i FROM ItemTaxaExtraEntidade i WHERE i.contasReceber.idContasReceber = :idContasReceber")
    List<ItemTaxaExtraEntidade> findByContasReceberId(@Param("idContasReceber") Long idContasReceber);
}