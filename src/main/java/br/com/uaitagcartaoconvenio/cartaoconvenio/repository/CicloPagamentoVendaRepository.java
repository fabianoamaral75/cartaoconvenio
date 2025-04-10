package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;

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
	@Query(value = "select cp                             "
                 + " from CicloPagamentoVenda cp          "
                 + " where cp.dtCriacao BETWEEN ?1 AND ?2 " )
    List<CicloPagamentoVenda> listaCicloPagamentoVendaByDtCriacao( String dtCriacaoIni, String dtCriacaoFim ) ;  

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
	/* Método para deletar uma lista de ciclos por IDs                */
	/*                                                                */
	/******************************************************************/	
    // @Transactional
    @Modifying
    @Query("DELETE FROM CicloPagamentoVenda c WHERE c.idCicloPagamentoVenda IN :ids")
    void deleteAllByPagamentoIdIn(@Param("ids") List<Long> ids);
    
	/******************************************************************/
	/*                                                                */
	/* MMétodo padrão para deletar uma lista de entidades             */
	/*                                                                */
	/******************************************************************/	
    // @Transactional
    default void deleteAllCiclosPagamento(List<CicloPagamentoVenda> ciclos) {
        deleteAll(ciclos); // O delete em cascata será aplicado automaticamente
    }
    
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select count(1)                                                                     "
                 + " from CicloPagamentoVenda cp                                                        "
                 + " where cp.anoMes              = ?1                                                  "
                 + "   and cp.descStatusPagamento IN ( 'AGUARDANDO_PAGAMENTO', 'AGUARDANDO_UPLOAD_NF' ) " )
    Long isExistCicloPagamentoVenda( String anoMes ) ;  

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true, value = "UPDATE ciclo_pagamento_venda SET status = 'PAGAMENTO_CANCELADO' WHERE ano_mes = :anoMes and status IN ( 'AGUARDANDO_PAGAMENTO', 'AGUARDANDO_UPLOAD_NF' )" )
    int updateStatusCicloPagamentoVenda(  @Param("anoMes") String anoMes);

}






























