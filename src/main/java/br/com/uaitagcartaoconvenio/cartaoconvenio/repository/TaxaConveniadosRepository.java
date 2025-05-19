package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniados;

@Repository
@Transactional
public interface TaxaConveniadosRepository extends JpaRepository<TaxaConveniados, Long>{
	

	@Query(value = "select tc                     "
                 + " from                         "
                 + "      TaxaConveniados tc     "
                 + " JOIN tc.conveniados   con    "
                 + " where con.idConveniados = ?1 " )
	TaxaConveniados taxaConveniadosByIdConveniados( Long idConveniados ) ; 
	
	@Query(value = "select tc                        "
                 + " from TaxaConveniados tc        "
                 + " where tc.descStatusTaxaCon = ?1 " )
    List<TaxaConveniados> listaTaxaConveniadosByStatusTaxaConv( StatusTaxaConv descStatusTaxaCon ); 

	@Query(value = "select tc                            "
                 + " from                                "
                 + "      TaxaConveniados tc            "
                 + " JOIN tc.conveniados   con           "
                 + " where con.idConveniados = ?1        "
                 + "   and tc.descStatusTaxaCon = 'ATUAL'" )
    TaxaConveniados taxaConveniadosAtualByIdConveniados( Long idConveniados ) ; 

}
