package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaEntidade;

@Repository
@Transactional
public interface TaxaEntidadeRepository extends JpaRepository<TaxaEntidade, Long> {

	
	@Query(value = "select te                  "
                 + " from                      "
                 + "      TaxaEntidade te      "
                 + " JOIN te.entidade     ent  "
                 + " where ent.idEntidade = ?1 " )
	List<TaxaEntidade> taxaEntidadeByIdEntidade( Long idEntidade ) ; 

	@Query(value = "select cp                         "
                 + " from TaxaEntidade cp             "
                 + " where cp.statusTaxaEntidade = ?1 " )
    List<TaxaEntidade> listaTaxaEntidadeByStatusTaxaEntidade( StatusTaxaEntidade statusTaxaEntidade ); 

	@Query(value = "select te                             "
                 + " from                                 "
                 + "      TaxaEntidade     te             "
                 + " JOIN te.entidade     ent             "
                 + " where ent.idEntidade = ?1            "
                 + "   and te.statusTaxaEntidade = 'ATUAL'" )
   TaxaEntidade taxaEntidadeAtualByIdEntidade( Long idEntidade ) ; 

	
}
