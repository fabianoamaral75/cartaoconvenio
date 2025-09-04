// EmprestimoRepository.java
package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusEmprestimo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Emprestimo;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByFuncionarioIdFuncionario(Long idFuncionario);
    
    List<Emprestimo> findByStatus(StatusEmprestimo status);
    
    @Query("SELECT e FROM Emprestimo e JOIN FETCH e.prestacoes WHERE e.idEmprestimo = :id")
    Optional<Emprestimo> findByIdWithPrestacoes(@Param("id") Long id);
    
    @Query("SELECT e FROM Emprestimo e JOIN FETCH e.funcionario f JOIN FETCH f.pessoa WHERE e.idEmprestimo = :id")
    Optional<Emprestimo> findByIdWithFuncionario(@Param("id") Long id);
    
 //   @Query("SELECT COUNT(e) > 0 FROM Emprestimo e WHERE e.funcionario.idFuncionario = :idFuncionario AND e.status IN ('SOLICITADO', 'EM_ANDAMENTO')")
    @Query("SELECT COUNT(e) > 0 FROM Emprestimo e WHERE e.funcionario.idFuncionario = :idFuncionario AND e.status IN (br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusEmprestimo.SOLICITADO, br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusEmprestimo.EM_ANDAMENTO)")
    boolean existsEmprestimoAtivo(@Param("idFuncionario") Long idFuncionario);
    
 // No EmprestimoRepository, adicionar este método:
    @Query(" SELECT pe.valorAmortizacao FROM PrestacaoEmprestimo pe " +
           "   JOIN pe.emprestimo e                                 " +
           "   JOIN e.funcionario f                                 " +
           "  WHERE f.idFuncionario = :idFuncionario                " +
           "    AND pe.status       = 'PAGA'                        " +
           "    AND pe.anoMesReferencia = :anoMes                   ")
    List<BigDecimal> findValoresPrestacoesPagasPorFuncionario(@Param("idFuncionario") Long idFuncionario, 
                                                              @Param("anoMes") String anoMes);    
    
}