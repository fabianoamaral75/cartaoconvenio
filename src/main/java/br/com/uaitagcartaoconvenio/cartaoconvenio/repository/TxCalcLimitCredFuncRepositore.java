package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
                 + " JOIN ent.listaFuncionario      fun "
                 + " JOIN fun.pessoa                pes "
                 + " where upper(trim(pes.nomePessoa)) like upper(concat('%', ?1, '%'))" )
	List<TaxaCalcLimiteCreditoFunc> listaTaxaCalcLimiteCreditoFuncByNomeFuncionario( String nomeFuncionario ) ; 
	

    @Query("SELECT t FROM TaxaCalcLimiteCreditoFunc t WHERE t.entidade.idEntidade = ?1 ORDER BY t.idTaxaCalcLimiteCreditoFunc DESC")
    List<TaxaCalcLimiteCreditoFunc> findAllByEntidadeId(Long idEntidade);

    @Query("SELECT t FROM TaxaCalcLimiteCreditoFunc t WHERE t.entidade.idEntidade = ?1 AND t.statusTaxaCalcLimiteCredFuncionaro = 'ATUAL'")
    Optional<TaxaCalcLimiteCreditoFunc> findAtualByEntidadeId(Long idEntidade);

    @Query("SELECT t FROM TaxaCalcLimiteCreditoFunc t WHERE t.entidade.idEntidade = ?1 AND t.statusTaxaCalcLimiteCredFuncionaro = ?2")
    List<TaxaCalcLimiteCreditoFunc> findByEntidadeIdAndStatus(Long idEntidade, StatusTaxaCalcLimiteCredFuncionaro status);

    @Query("SELECT t FROM TaxaCalcLimiteCreditoFunc t WHERE t.entidade.idEntidade = ?1 ORDER BY t.idTaxaCalcLimiteCreditoFunc DESC")
    List<TaxaCalcLimiteCreditoFunc> findLastByEntidadeId(Long idEntidade);

    @Modifying
    @Query("UPDATE TaxaCalcLimiteCreditoFunc t SET t.statusTaxaCalcLimiteCredFuncionaro = ?2 WHERE t.idTaxaCalcLimiteCreditoFunc = ?1")
    int updateStatusTaxa(Long idTaxaCalcLimiteCreditoFunc, StatusTaxaCalcLimiteCredFuncionaro novoStatus);
	
}
