package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaCalcLimiteCredFuncionaro;
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

	
	@Query(value = "select cp                                         "
                 + " from TaxaCalcLimiteCreditoFunc cp                "
                 + " where cp.statusTaxaCalcLimiteCredFuncionaro = ?1 " )
    List<TaxaCalcLimiteCreditoFunc> listaTaxaCalcLimiteCreditoFuncByDescStatusPagamento( StatusTaxaCalcLimiteCredFuncionaro statusTaxaCalcLimiteCredFuncionaro ); 
	
	@Query(value = "select tcc                          "
                 + " from                               "
                 + "      TaxaCalcLimiteCreditoFunc tcc "
                 + " JOIN tcc.entidade              ent "
                 + " JOIN ent.funcionario           fun "
                 + " JOIN fun.pessoa                pes "
                 + " where upper(trim(pes.nomePessoa)) like upper(concat('%', ?1, '%'))" )
	List<TaxaCalcLimiteCreditoFunc> listaTaxaCalcLimiteCreditoFuncByNomeFuncionario( String nomeFuncionario ) ; 
	
	
}
