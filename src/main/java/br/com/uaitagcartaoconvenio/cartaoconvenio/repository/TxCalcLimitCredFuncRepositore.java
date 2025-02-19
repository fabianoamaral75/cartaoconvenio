package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaCalcLimiteCreditoFunc;

@Repository
@Transactional
public interface TxCalcLimitCredFuncRepositore  extends JpaRepository<TaxaCalcLimiteCreditoFunc, Long> {

	@Query(value = " select tax.taxaBase                                    "
			     + "   from                                                 "
			     + "       Entidade ent                                     "
			     + "  join ent.taxaCalcLimiteCreditoFunc tax                "
			     + " where ent.idEntidade = ?1                              "
			     + "   and tax.statusTaxaCalcLimiteCredFuncionaro = 'ATUAL' " )
	public BigDecimal getTxCalcLimitCredFunc(Long idEntidade);
	
}
