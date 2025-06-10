package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoConveniado;

@Repository
public interface ContratoConveniadoRepository extends JpaRepository<ContratoConveniado, Long> {

    @Query("SELECT c FROM ContratoConveniado c WHERE c.conveniados.idConveniados = :idConveniado")
    List<ContratoConveniado> findByConveniadoId(@Param("idConveniado") Long idConveniado);
/*
    @Query("SELECT c FROM ContratoConveniado c WHERE c.conveniados.idConveniados = :idConveniado AND c.idContratoConveniado = :idContrato")
    ContratoConveniado findByConveniadoIdAndContratoId(@Param("idConveniado") Long idConveniado, 
                                                      @Param("idContrato") Long idContrato);
*/    
    @Query("SELECT c FROM ContratoConveniado c WHERE c.conveniados.idConveniados = :idConveniado AND c.idContratoConveniado = :idContrato")
    Optional<ContratoConveniado> findByConveniadoIdAndContratoId(@Param("idConveniado") Long idConveniado, 
                                                               @Param("idContrato") Long idContrato);
}