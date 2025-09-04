// PrestacaoEmprestimoRepository.java
package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusPrestacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PrestacaoEmprestimo;

@Repository
public interface PrestacaoEmprestimoRepository extends JpaRepository<PrestacaoEmprestimo, Long> {

    List<PrestacaoEmprestimo> findByEmprestimoIdEmprestimo(Long idEmprestimo);
    
    List<PrestacaoEmprestimo> findByStatusAndDtVencimentoBefore(StatusPrestacao status, Date data);
    
    Optional<PrestacaoEmprestimo> findByEmprestimoIdEmprestimoAndNumeroParcela(Long idEmprestimo, Integer numeroParcela);
    
    @Query("SELECT p FROM PrestacaoEmprestimo p WHERE p.emprestimo.funcionario.idFuncionario = :idFuncionario AND p.status = 'PENDENTE' AND p.dtVencimento <= CURRENT_DATE")
    List<PrestacaoEmprestimo> findPrestacoesVencidasPorFuncionario(@Param("idFuncionario") Long idFuncionario);
}