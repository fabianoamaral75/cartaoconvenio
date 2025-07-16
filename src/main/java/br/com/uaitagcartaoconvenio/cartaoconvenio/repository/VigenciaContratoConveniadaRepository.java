package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusContrato;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoConveniada;

@Repository
public interface VigenciaContratoConveniadaRepository  extends JpaRepository<VigenciaContratoConveniada, Long>{
	
    @Query("SELECT v FROM VigenciaContratoConveniada v WHERE v.contratoConveniado.conveniados.idConveniados = :idConveniada")
    List<VigenciaContratoConveniada> findByConveniadaId(Long idConveniada);

    @Query("SELECT v FROM VigenciaContratoConveniada v WHERE v.contratoConveniado.conveniados.idConveniados = :idConveniada AND v.descStatusContrato = :status")
    List<VigenciaContratoConveniada> findByConveniadaIdAndStatus(Long idConveniada, StatusContrato status);

    @Query("SELECT v FROM VigenciaContratoConveniada v WHERE v.contratoConveniado.conveniados.idConveniados = :idConveniada AND :currentDate BETWEEN v.dataInicio AND v.dataFinal ORDER BY v.id DESC")
    List<VigenciaContratoConveniada> findCurrentVigencia(Long idConveniada, LocalDate currentDate);

    @Query("SELECT v FROM VigenciaContratoConveniada v WHERE v.contratoConveniado.conveniados.idConveniados = :idConveniada ORDER BY v.id DESC LIMIT 1")
    VigenciaContratoConveniada findLatestVigencia(Long idConveniada);

}
