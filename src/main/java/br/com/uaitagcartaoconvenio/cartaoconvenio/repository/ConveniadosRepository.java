package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniados;

@Repository
@Transactional
public interface ConveniadosRepository extends JpaRepository<Conveniados, Long>{
	
	@Query(value = "select tx                             "
			     + "  from Conveniados con                "
			     + "   join con.taxaConveniados tx       "
			     + " where con.idConveniados = ?1         "
			     + "   and tx.descStatusTaxaCon = 'ATUAL' " 
			     )
    TaxaConveniados findTxConvByIdconv(Long idConv);
	
	@Modifying(flushAutomatically = true)
	@Query(nativeQuery = true, value = "UPDATE taxa_conveiniados SET status = 'DESATUALIZADA' WHERE id_taxa_conveiniados = ?1")
	void updateStatusTaxaConveniados(Long id);		
	
	@Query(value = "select con                    "
		         + "  from Conveniados con        "
		         + "   join con.pessoa pes        "
		         + " where con.idConveniados = ?1 " 
		     )
   Conveniados findUserByIdconv(Long idConv);
	
   @Query(value = "select con                 "
                 + " from                      "
                 + "      Conveniados      con "
                 + " JOIN con.pessoa        pe "
                 + " JOIN pe.pessoaJuridica pj "
                 + " where pj.cnpj = ?1        " )
   Conveniados conveniadosByCnpj( String cnpj) ;  
/*
   @Query(value = " select con                "
                + " from                      "
                + "      Conveniados      con "
                + " JOIN con.pessoa        pe "
                + " where upper(trim(pe.nomePessoa)) like upper(concat('%', ?1, '%'))" )
   List<Conveniados> listaConveniadosByNome( String nome) ; 
*/   
   @Query("SELECT c FROM Conveniados c WHERE UPPER(c.pessoa.nomePessoa) LIKE UPPER(CONCAT('%', :nome, '%'))")
   List<Conveniados> listaConveniadosByNome(@Param("nome") String nome);
   
/*
   @Query(value = "select con                 "
                + " from                      "
                + "      Conveniados      con "
                + " JOIN con.pessoa        pe "
                + " where upper(trim(pe.cidade)) like upper(concat('%', ?1, '%'))" )
   List<Conveniados> listaConveniadosByCidade( String cidade) ; 
 */
   
   @Query("SELECT c FROM Conveniados c WHERE c.pessoa.cidade = :cidade")
   List<Conveniados> listaConveniadosByCidade(@Param("cidade") String cidade);

	@Query(value = "SELECT qty_dias_pagamento FROM conveniados where id_conveniados = ?1", nativeQuery = true)
	int qtyDiasPagamento( Long id );

}
