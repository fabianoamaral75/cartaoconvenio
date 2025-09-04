// EmprestimoFechamentoRepository.java
package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Emprestimo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.FechamentoEmprestimoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RestabelecerLimitCreditoEmprestimoDTO;

@Repository
public interface EmprestimoFechamentoRepository extends JpaRepository<Emprestimo, Long> {
 
	@Query("SELECT new br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.FechamentoEmprestimoDTO(         " +
		       "  e.idEmprestimo, f.idFuncionario, p.nomePessoa, pe.valorParcela, pe.numeroParcela, :anoMes) " +
		       "   FROM Emprestimo e                                                                         " +
		       "   JOIN e.funcionario f                                                                      " +
		       "   JOIN f.pessoa p                                                                           " +
		       "   JOIN e.prestacoes pe                                                                      " +
		       "  WHERE pe.status = 'PENDENTE'                                                               " +
		       "    AND pe.dtVencimento <= CURRENT_DATE                                                      " +
		       "    AND e.status = 'EM_ANDAMENTO'                                                            ")
	List<FechamentoEmprestimoDTO> findPrestacoesVencidasParaFechamento(@Param("anoMes") String anoMes); 
	
	@Query("SELECT new br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.FechamentoEmprestimoDTO(         " +
		       "  e.idEmprestimo, f.idFuncionario, p.nomePessoa, pe.valorParcela, pe.numeroParcela, :anoMes) " +
		       "   FROM Emprestimo e                                                                         " +
		       "   JOIN e.funcionario f                                                                      " +
		       "   JOIN f.pessoa p                                                                           " +
		       "   JOIN e.prestacoes pe                                                                      " +
		       "  WHERE pe.status = 'PENDENTE'                                                               " +
		       "    AND pe.dtVencimento <= CURRENT_DATE                                                      " +
		       "    AND e.status = 'APROVADO'                                                                ")
	List<FechamentoEmprestimoDTO> findPrestacoesVencidasFechamento(@Param("anoMes") String anoMes);    
	
    
    @Query("SELECT new br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RestabelecerLimitCreditoEmprestimoDTO( " +
    	       "  f.idFuncionario, SUM(pe.valorAmortizacao), p.nomePessoa, pf.cpf)                                 " +
    	       "  FROM PrestacaoEmprestimo pe                                                                      " +
    	       "  JOIN pe.emprestimo e                                                                             " +
    	       "  JOIN e.funcionario f                                                                             " +
    	       "  JOIN f.pessoa p                                                                                  " +
    	       "  JOIN p.pessoaFisica pf                                                                           " + 
    	       " WHERE pe.status = 'PAGA'                                                                          " +
    	       "   AND pe.dtPagamento IS NOT NULL                                                                  " +
    	       "   AND pe.anoMesReferencia = :anoMes                                                               " +
    	       " GROUP BY f.idFuncionario, p.nomePessoa, pf.cpf                                                    ")
    	List<RestabelecerLimitCreditoEmprestimoDTO> findValoresParaRestabelecerLimite(@Param("anoMes") String anoMes);    

}

