package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaEntidade;

@Repository
@Transactional
public interface TaxaEntidadeRepository extends JpaRepository<TaxaEntidade, Long> {

	
	@Query(value = "select te                  "
                 + " from                      "
                 + "      TaxaEntidade te      "
                 + " JOIN te.entidade     ent  "
                 + " where ent.idEntidade = ?1 " )
	List<TaxaEntidade> taxaEntidadeByIdEntidade( Long idEntidade ) ; 

	@Query(value = "select cp                         "
                 + " from TaxaEntidade cp             "
                 + " where cp.statusTaxaEntidade = ?1 " )
    List<TaxaEntidade> listaTaxaEntidadeByStatusTaxaEntidade( StatusTaxaEntidade statusTaxaEntidade ); 

	@Query(value = "select te                             "
                 + " from                                 "
                 + "      TaxaEntidade     te             "
                 + " JOIN te.entidade     ent             "
                 + " where ent.idEntidade = ?1"
                 + "   and te.statusTaxaEntidade = 'ATUAL'" )
   TaxaEntidade taxaEntidadeAtualByIdEntidade( Long idEntidade ) ; 

    @Query("SELECT te FROM TaxaEntidade te JOIN te.entidade ent WHERE ent.idEntidade = ?1 AND te.statusTaxaEntidade = ?2")
    List<TaxaEntidade> findByEntidadeIdAndStatus(Long idEntidade, StatusTaxaEntidade status);

    @Query("SELECT te FROM TaxaEntidade te WHERE te.entidade.idEntidade = ?1 ORDER BY te.idTaxaEntidade DESC")
    List<TaxaEntidade> findLastByEntidadeId(Long idEntidade);

    @Modifying
    @Query("UPDATE TaxaEntidade te SET te.statusTaxaEntidade = ?2 WHERE te.idTaxaEntidade = ?1")
    int updateStatusTaxa(Long idTaxaEntidade, StatusTaxaEntidade novoStatus);
	
}
