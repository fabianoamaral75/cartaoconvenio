package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.LimiteCredito;

@Repository
@Transactional
public interface LimitecreditoRepository extends JpaRepository<LimiteCredito, Long> {
	
	
	   @Modifying(flushAutomatically = true)
	   @Query(nativeQuery = true, value = "update limite_credito set valor_utilizado = ?2 where id_limite_credito = ?1")
	   void updateLimiteCreditoValorUtilizado( Long idVenda, BigDecimal valorUtilizado );		

	
}
