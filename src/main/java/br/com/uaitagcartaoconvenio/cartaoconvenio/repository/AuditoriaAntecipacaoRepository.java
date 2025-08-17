package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // Este é o import correto


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.AuditoriaAntecipacao;

public interface AuditoriaAntecipacaoRepository extends JpaRepository<AuditoriaAntecipacao, Long> {

	@Query("SELECT a FROM AuditoriaAntecipacao a WHERE a.antecipacao.idAntecipacao = :antecipacaoId")
	List<AuditoriaAntecipacao> findByAntecipacaoId(@Param("antecipacaoId") Long antecipacaoId);

    @Query("SELECT a FROM AuditoriaAntecipacao a WHERE a.dataAuditoria BETWEEN :inicio AND :fim")
    List<AuditoriaAntecipacao> findByPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    @Query("SELECT a FROM AuditoriaAntecipacao a WHERE a.usuario = :usuario ORDER BY a.dataAuditoria DESC")
    List<AuditoriaAntecipacao> findByUsuario(@Param("usuario") String usuario);

    @Query("SELECT COUNT(a) FROM AuditoriaAntecipacao a WHERE a.ipOrigem = :ip AND a.dataAuditoria > :dataLimite")
    long countByIpAndDataAfter(
            @Param("ip") String ip,
            @Param("dataLimite") LocalDateTime dataLimite);
    
    @Query("SELECT a FROM AuditoriaAntecipacao a WHERE a.antecipacao.id = :idAntecipacao ORDER BY a.dataAuditoria DESC")
    Page<AuditoriaAntecipacao> findByAntecipacaoIdPaginado( @Param("idAntecipacao") Long idAntecipacao, Pageable pageable);   
    
    
    
}