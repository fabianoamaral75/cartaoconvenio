package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusAntecipacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Antecipacao;

@Repository
public interface AntecipacaoRepository extends JpaRepository<Antecipacao, Long> {

    List<Antecipacao> findByConveniadosIdConveniados(Long idConveniados);
    
    List<Antecipacao> findByConveniadosIdConveniadosAndDescStatusAntecipacao(Long idConveniados, StatusAntecipacao status);
    
    Optional<Antecipacao> findByIdAntecipacaoAndDescStatusAntecipacao(Long idAntecipacao, StatusAntecipacao status);
    
    @Query("SELECT a FROM Antecipacao a WHERE a.cicloPagamentoVenda.idCicloPagamentoVenda = :idCicloPagamentoVenda")
    List<Antecipacao> findByCicloPagamentoVendaId(@Param("idCicloPagamentoVenda") Long idCicloPagamentoVenda);
    
    @Query("SELECT a FROM Antecipacao a WHERE a.cicloPagamentoVenda.idCicloPagamentoVenda = :idCicloPagamentoVenda AND a.descStatusAntecipacao = :status")
    List<Antecipacao> findByCicloPagamentoVendaIdAndStatus(
        @Param("idCicloPagamentoVenda") Long idCicloPagamentoVenda, 
        @Param("status") StatusAntecipacao status);
    
    List<Antecipacao> findByDescStatusAntecipacao(StatusAntecipacao status);
}