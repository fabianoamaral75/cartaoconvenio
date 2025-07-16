package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.LimiteCredito;

@Repository
@Transactional
public interface LimitecreditoRepository extends JpaRepository<LimiteCredito, Long> {
	
	
	   @Modifying(flushAutomatically = true, clearAutomatically = true)
	   @Query(nativeQuery = true, value = "update limite_credito set valor_utilizado = ?2 where id_limite_credito = ?1")
	   void updateLimiteCreditoValorUtilizado( Long idLimiteCredito, BigDecimal valorUtilizado );		

	   /******************************************************************/
	   /*                                                                */
	   /*                                                                */
	   /******************************************************************/	
	   @Transactional(readOnly = true)
	   @Query(value = " select                                                               "
	                + "     ven.id_cartao                                                    "
	                + "   , fun.id_funcionario                                               "
	                + "   , SUM(ven.valor_venda) as sum_valor_venda                          "
	                + " FROM                                                                 "
	                + "   venda       ven                                                    "
	                + "	, cartao      car                                                    "
	                + "	, funcionario fun                                                    "
	                + " WHERE ven.ano_mes                             = ?1                   "
	                + "   AND ven.status                              = 'PAGAMENTO_APROVADO' "
	                + "   AND ven.status_limite_credito_restabelecido = 'VENDA_REALIZADA'    "
	                + "   AND car.id_cartao                           = ven.id_cartao        "
	                + "   AND fun.id_funcionario                      = car.id_funcionario   "
	                + " GROUP BY ven.id_cartao, fun.id_funcionario                           "
	                + " ORDER BY ven.id_cartao                                               " , nativeQuery = true )
//	   List<RestabelecerLimitCreditoDTO> listaRestabelecerLimiteCredito( String anoMes ) ;
	   List<Object[]> listaRawRestabelecerLimiteCredito(String anoMes);
	   
	   @Modifying(flushAutomatically = true, clearAutomatically = true)
	   @Query(nativeQuery = true, value = "UPDATE limite_credito SET valor_utilizado = GREATEST(0, (valor_utilizado - :valor)) WHERE id_funcionario = :id")
	   int updateRestabelecerLimiteCredito( @Param("id") Long idFuncionario, @Param("valor") BigDecimal valorUtilizado);
	   
	   @Modifying(flushAutomatically = true, clearAutomatically = true)
	   @Query(nativeQuery = true, value = "UPDATE limite_credito SET valor_utilizado = GREATEST(0, (valor_utilizado + :valor)) WHERE id_funcionario = :id")
	   int updateRollbackRestabelecerLimiteCredito( @Param("id") Long idFuncionario, @Param("valor") BigDecimal valorUtilizado);

	   @Modifying(flushAutomatically = true, clearAutomatically = true)
	   @Query("UPDATE LimiteCredito lc SET lc.limite = :novoLimite WHERE lc.idLimiteCredito = :id")
	   int updateLimite(@Param("id") Long idLimiteCredito, @Param("novoLimite") BigDecimal novoLimite);

	   @Modifying(flushAutomatically = true, clearAutomatically = true)
	   @Query("UPDATE LimiteCredito lc SET lc.valorUtilizado = :novoValorUtilizado WHERE lc.idLimiteCredito = :id")
	   int updateValorUtilizado(@Param("id") Long idLimiteCredito, @Param("novoValorUtilizado") BigDecimal novoValorUtilizado);
}
