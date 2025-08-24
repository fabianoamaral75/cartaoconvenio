package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.FechamentoConvItensVendas;

@Repository
@Transactional
public interface CicloPagamentoVendaRepository extends JpaRepository<CicloPagamentoVenda, Long> {


	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select cp                    "
                 + " from CicloPagamentoVenda cp "
                 + " where cp.anoMes = ?1        " )
    List<CicloPagamentoVenda> listaCicloPagamentoVendaByAnoMes( String anoMes ) ;  

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select cp                          "
                 + " from CicloPagamentoVenda cp       "
                 + " where cp.anoMes              = ?1 "
                 + "   and cp.descStatusPagamento = ?2 " )
    List<CicloPagamentoVenda> listaCicloPagamentoVendaByAnoMesStatus( String anoMes ,StatusCicloPgVenda descStatusPagamento ) ;  
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
/*	
	@Query("SELECT cp                                           "
	     + "  FROM CicloPagamentoVenda         cp               "
	     + " JOIN cp.taxasFaixaVendas          txfv             "
	     + " JOIN cp.conveniados               con              "
	     + " JOIN cp.fechamentoConvItensVendas fcv              "
	     + " JOIN cp.itemTaxaExtraConveniada.taxaExtraConveniada.periodoCobrancaTaxa per "
		 + " WHERE cp.dtCriacao BETWEEN ?1 AND ?2 ")
*/	
	@Query("SELECT DISTINCT cp FROM CicloPagamentoVenda cp " +
		       "JOIN FETCH cp.taxasFaixaVendas txfv " +
		       "JOIN FETCH cp.conveniados con " +
		       "JOIN FETCH cp.fechamentoConvItensVendas fcv " + // Apenas uma coleção com FETCH
		       "JOIN cp.itemTaxaExtraConveniada itec " +        // Sem FETCH
		       "JOIN itec.taxaExtraConveniada tec " +
		       "JOIN tec.periodoCobrancaTaxa per " +
		       "WHERE cp.dtCriacao BETWEEN ?1 AND ?2")
	List<CicloPagamentoVenda> listaCicloPagamentoVendaByDtCriacao(Date dtIni, Date dtFim);
	
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @EntityGraph(attributePaths = {
            "taxasFaixaVendas",
            "conveniados",
            "fechamentoConvItensVendas"
        })
        @Query("SELECT DISTINCT cp FROM CicloPagamentoVenda cp " +
               "WHERE cp.dtCriacao BETWEEN ?1 AND ?2 " +
               "ORDER BY cp.idCicloPagamentoVenda ASC")
		List<CicloPagamentoVenda> findComPeriodoCobranca(Date dtIni, Date dtFim);
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select cp                          "
                 + " from CicloPagamentoVenda cp       "
                 + " where cp.descStatusPagamento = ?1 " )
    List<CicloPagamentoVenda> listaCicloPagamentoVendaByDescStatusPagamento( StatusCicloPgVenda descStatusPagamento ); 
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select cp                     "
                 + " from                         "
                 + "      CicloPagamentoVenda cp  "
                 + " JOIN cp.conveniados     con  "
                 + " where con.idConveniados = ?1 " )
	CicloPagamentoVenda listaCicloPagamentoVendaByIdConveniados( Long idConveniados ) ; 
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select cp                     "
                 + " from                         "
                 + "      CicloPagamentoVenda cp  "
                 + " JOIN cp.conveniados     con  "
                 + " JOIN con.pessoa          pe  "
                 + " where upper(trim(pe.nomePessoa)) like upper(concat('%', ?1, '%'))" )
    List<CicloPagamentoVenda> listaCicloPagamentoVendaByNomeConveniado( String nomeConveniado ); 

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select cp                                                                      "
            + " from CicloPagamentoVenda cp                                                        "
            + " where cp.anoMes              = ?1                                  "
            + "   and cp.descStatusPagamento IN ( 'AGUARDANDO_PAGAMENTO', 'AGUARDANDO_UPLOAD_NF' ) " )
    List<CicloPagamentoVenda> listaCicloPagamentoVendaSelectAntecipacao( String anoMes ); 

	/******************************************************************/
	/*                                                                */
	/* Método para deletar uma lista de ciclos por IDs                */
	/*                                                                */
	/******************************************************************/	
    // @Transactional
    @Modifying
    @Query("DELETE FROM CicloPagamentoVenda c WHERE c.idCicloPagamentoVenda IN :ids")
    void deleteAllByPagamentoIdIn(@Param("ids") List<Long> ids);
        
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select count(1)                                                                     "
                 + " from CicloPagamentoVenda cp                                                        "
                 + " where cp.anoMes              = ?1                                          "
                 + "   and cp.descStatusPagamento IN ( 'AGUARDANDO_PAGAMENTO', 'AGUARDANDO_UPLOAD_NF' ) " )
    Long isExistCicloPagamentoVenda( String anoMes ) ;  

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true, value = "UPDATE ciclo_pagamento_venda SET status = 'PAGAMENTO_CANCELADO' WHERE ano_mes = :anoMes and status IN ( 'AGUARDANDO_PAGAMENTO', 'AGUARDANDO_UPLOAD_NF' )" )
    int updateStatusCicloPagamentoVenda(  @Param("anoMes") String anoMes);

  	/******************************************************************/
  	/*                                                                */
  	/*                                                                */
  	/******************************************************************/	
    @Query("SELECT cp FROM CicloPagamentoVenda cp LEFT JOIN FETCH cp.fechamentoConvItensVendas WHERE cp.idCicloPagamentoVenda = :id")
    Optional<CicloPagamentoVenda> findByIdWithFechamentosPagamentos(@Param("id") Long id);

    /******************************************************************/
  	/*                                                                */
  	/*                                                                */
  	/******************************************************************/	
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true, value = "UPDATE ciclo_pagamento_venda SET status = 'PAGAMENTO_APROVADO' WHERE id_ciclo_pagamento_venda = :id")
    int updateStatusCicloPagamentoAprovado(  @Param("id") Long id);
 
  	/******************************************************************/
  	/*                                                                */
  	/*                                                                */
  	/******************************************************************/	
    @Query(nativeQuery = true, value = " SELECT EXISTS ( select 1 from ciclo_pagamento_venda cp where cp.id_ciclo_pagamento_venda = :id and cp.status = 'PAGAMENTO_APROVADO')")
    Boolean isStatusCicloFinalizarFechamentoOK(  @Param("id") Long id);


    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    @Modifying
    @Query("UPDATE CicloPagamentoVenda cp                                                        "
         + "   SET                                                                               " 
         + "     cp.dtPagamento          = :dtPg                                               , " 
         + "     cp.descStatusPagamento  = :status                                             , " 
         + "     cp.observacao           = CONCAT(COALESCE(cp.observacao, ''), :novaObservacao), " 
         + "     cp.dtAlteracao          = CURRENT_TIMESTAMP                                   , "
         + "     cp.docAutenticacaoBanco = :doc                                                  " 
         + " WHERE cp.idCicloPagamentoVenda = :id                                                ")
    int atualizarPagamento( @Param("id") Long idContasPagamento, @Param("novaObservacao") String novaObservacao, @Param("status") StatusCicloPgVenda status, @Param("doc") String doc, @Param("dtPg") Date dtPg );

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    @Query(value = "select fciv                                 "
                 + " from                                       "
                 + "       CicloPagamentoVenda cp               "
                 + "  JOIN cp.fechamentoConvItensVendas  fciv   "
                 + " where cp.idCicloPagamentoVenda = ?1        "
                 + "   and cp.descStatusPagamento = 'PAGAMENTO' " )
  	List<FechamentoConvItensVendas> listaCicloPagamentoVendaByIdCR( Long id ); 


}






























