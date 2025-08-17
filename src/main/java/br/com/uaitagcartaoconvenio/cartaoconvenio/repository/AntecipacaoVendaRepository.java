package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.AntecipacaoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.AuditoriaAntecipacao;

@Repository
public interface AntecipacaoVendaRepository extends JpaRepository<AntecipacaoVenda, Long> {

	@Query("SELECT av FROM AntecipacaoVenda av WHERE av.antecipacao.id = :idAntecipacao")
	List<AntecipacaoVenda> findByAntecipacaoId(@Param("idAntecipacao") Long idAntecipacao);
	
 // Versão com paginação (mantenha esta se precisar de paginação)
    @Query("SELECT a FROM AuditoriaAntecipacao a WHERE a.antecipacao.id = :idAntecipacao ORDER BY a.dataAuditoria DESC")
    Page<AuditoriaAntecipacao> findByAntecipacaoId(@Param("idAntecipacao") Long idAntecipacao, Pageable pageable);    
    
    
    @Query("SELECT av.venda.idVenda FROM AntecipacaoVenda av WHERE av.antecipacao.idAntecipacao = :idAntecipacao")
    List<Long> findVendaIdsByAntecipacaoId(@Param("idAntecipacao") Long idAntecipacao);
    
 // Adicionar no repositório
    @Query("SELECT av FROM AntecipacaoVenda av JOIN FETCH av.venda WHERE av.antecipacao.idAntecipacao = :idAntecipacao")
    List<AntecipacaoVenda> findByAntecipacaoWithVendas(@Param("idAntecipacao") Long idAntecipacao);    
}
