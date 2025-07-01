package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraEntidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxaExtraEntidadeRepository extends JpaRepository<TaxaExtraEntidade, Long> {

    @Query("SELECT t "
         + " FROM TaxaExtraEntidade t                "
		 + " LEFT JOIN FETCH t.periodoCobrancaTaxa p " 
		 + " LEFT JOIN FETCH p.tipoPeriodo           " 
		 + " LEFT JOIN FETCH t.entidade              " 
         + "WHERE t.entidade.id = :idEntidade        "
         )
    List<TaxaExtraEntidade> findByEntidadeId(@Param("idEntidade") Long idEntidade);
    
    
    

    @Query("SELECT t FROM TaxaExtraEntidade t WHERE t.entidade.id = :idEntidade AND t.id = :idTaxa")
    Optional<TaxaExtraEntidade> findByEntidadeIdAndTaxaId(@Param("idEntidade") Long idEntidade, 
                                              @Param("idTaxa") Long idTaxa);

    @Query("SELECT t FROM TaxaExtraEntidade t WHERE t.entidade.id = :idEntidade AND t.status = :status")
    List<TaxaExtraEntidade> findByEntidadeIdAndStatus(@Param("idEntidade") Long idEntidade, 
                                                    @Param("status") String status);
    
}