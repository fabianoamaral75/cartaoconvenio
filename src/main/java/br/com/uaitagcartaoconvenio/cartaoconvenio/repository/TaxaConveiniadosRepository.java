package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveiniados;

@Repository
@Transactional
public interface TaxaConveiniadosRepository extends JpaRepository<TaxaConveiniados, Long>{
	

	@Query(value = "select tc                     "
                 + " from                         "
                 + "      TaxaConveiniados tc     "
                 + " JOIN tc.conveniados   con    "
                 + " where con.idConveniados = ?1 " )
	TaxaConveiniados taxaConveiniadosByIdConveniados( Long idConveniados ) ; 
	
	@Query(value = "select tc                        "
                 + " from TaxaConveiniados tc        "
                 + " where tc.descStatusTaxaCon = ?1 " )
    List<TaxaConveiniados> listaTaxaConveiniadosByStatusTaxaConv( StatusTaxaConv descStatusTaxaCon ); 

	@Query(value = "select tc                            "
                 + " from                                "
                 + "      TaxaConveiniados tc            "
                 + " JOIN tc.conveniados   con           "
                 + " where con.idConveniados = ?1        "
                 + "   and tc.descStatusTaxaCon = 'ATUAL'" )
    TaxaConveiniados taxaConveiniadosAtualByIdConveniados( Long idConveniados ) ; 

}
