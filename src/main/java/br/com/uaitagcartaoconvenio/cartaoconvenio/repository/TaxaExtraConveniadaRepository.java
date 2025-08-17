package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;

@Repository
public interface TaxaExtraConveniadaRepository extends JpaRepository<TaxaExtraConveniada, Long> {

	@Query("SELECT t FROM TaxaExtraConveniada t         " +
		   "    LEFT JOIN FETCH t.periodoCobrancaTaxa p " +
		   "    LEFT JOIN FETCH p.tipoPeriodo           " +
		   "    LEFT JOIN FETCH t.conveniados           " +
		   " WHERE t.conveniados.id = :idConveniado     ")
		List<TaxaExtraConveniada> findByConveniadoId( @Param("idConveniado") Long idConveniado );

    @Query("SELECT t FROM TaxaExtraConveniada t WHERE t.conveniados.id = :idConveniado AND t.id = :idTaxa")
    Optional<TaxaExtraConveniada> findByConveniadoIdAndTaxaId( @Param("idConveniado") Long idConveniado, @Param("idTaxa") Long idTaxa );

    @Query("SELECT t FROM TaxaExtraConveniada t WHERE t.conveniados.id = :idConveniado AND t.statusTaxa = :status")
    List<TaxaExtraConveniada> findByConveniadoIdAndStatus( @Param("idConveniado") Long idConveniado, @Param("status") String status );

}