package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveiniados;

@Repository
@Transactional
public interface ConveniadosRepository extends JpaRepository<Conveniados, Long>{
	
	@Query(value = "select tx                             "
			     + "  from Conveniados con                "
			     + "   join con.taxaConveiniados tx       "
			     + " where con.idConveniados = ?1         "
			     + "   and tx.descStatusTaxaCon = 'ATUAL' " 
			     )
	public TaxaConveiniados findTxConvByIdconv(Long idConv);
		
	
	@Modifying(flushAutomatically = true)
	@Query(nativeQuery = true, value = "UPDATE taxa_conveiniados SET status = 'DESATUALIZADA' WHERE id_taxa_conveiniados = ?1")
	void updateStatusTaxaConveiniados(Long id);		
	
	@Query(value = "select con                    "
		         + "  from Conveniados con        "
		         + "   join con.pessoa pes        "
		         + " where con.idConveniados = ?1 " 
		     )
   public Conveniados findUserByIdconv(Long idConv);
	

}
