package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniadaEntidade;
import jakarta.transaction.Transactional;

@Repository
public interface TaxaConveniadaEntidadeRepository extends JpaRepository<TaxaConveniadaEntidade, Long> {
    
    List<TaxaConveniadaEntidade> findByEntidadeIdEntidade(Long idEntidade);
    
    List<TaxaConveniadaEntidade> findByConveniadosIdConveniados(Long idConveniados);
    
    List<TaxaConveniadaEntidade> findByEntidadeIdEntidadeAndConveniadosIdConveniados(Long idEntidade, Long idConveniados);
    
    Optional<TaxaConveniadaEntidade> findByIdAndEntidadeIdEntidadeAndConveniadosIdConveniados(
        Long idTaxa, Long idEntidade, Long idConveniados);
    
    @Transactional
    @Modifying
    @Query("UPDATE TaxaConveniadaEntidade t SET t.statusTaxaConEnt = :status " +
           "WHERE t.id = :idTaxa and t.entidade.idEntidade = :idEntidade AND t.conveniados.idConveniados = :idConveniados")
    int updateStatusByEntidadeAndConveniados(
            StatusTaxaConv status,
            Long idTaxa,
            Long idEntidade,
            Long idConveniados
    );
    
    @Transactional
    @Modifying
    @Query("UPDATE TaxaConveniadaEntidade t SET t.statusTaxaConEnt = :status " +
           "WHERE t.entidade.idEntidade = :idEntidade AND t.conveniados.idConveniados = :idConveniados")
    int updateStatusByIdEntConv(
            StatusTaxaConv status,
            Long idEntidade,
            Long idConveniados
    );
    
    // Busca por idEntidade, idConveniados e Status
    List<TaxaConveniadaEntidade> findByEntidadeIdEntidadeAndConveniadosIdConveniadosAndStatusTaxaConEnt(
            Long idEntidade,
            Long idConveniados,
            StatusTaxaConv status
    );
    
	@Query(value = "SELECT t.vlr_taxa FROM taxa_conveniada_entidade t WHERE t.id_taxa_conveniada_entidade = ?1", nativeQuery = true)
	BigDecimal getTaxaConvEnti( Long id );
    
    
  	/******************************************************************/
  	/*                                                                */
  	/*                                                                */
  	/******************************************************************/	
    @Query("SELECT tce FROM TaxaConveniadaEntidade tce WHERE tce.statusTaxaConEnt = :status and tce.conveniados.idConveniados = :idConveniados and tce.entidade.idEntidade = :idEntidade")
    Optional<TaxaConveniadaEntidade> findByIdAndEntidadeIdEntidadeAndStatusConveniados( 
    		@Param("status"       ) StatusTaxaConv status      , 
    		@Param("idEntidade"   ) Long           idEntidade  , 
    		@Param("idConveniados") Long           idConveniados);


}