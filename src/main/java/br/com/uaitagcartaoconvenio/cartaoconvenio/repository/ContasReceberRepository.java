package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceber;

@Repository
@Transactional
public interface ContasReceberRepository extends JpaRepository<ContasReceber, Long>{

	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select cr         "
            + " from ContasReceber cr "
            + " where cr.anoMes = ?1  " )
    List<ContasReceber> listaContasReceberByAnoMes( String anoMes ) ;  
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select cr                        "
            + " from ContasReceber cr                "
            + " where cr.dtCriacao BETWEEN ?1 AND ?2 " )
    List<ContasReceber> listaContasReceberByDtCriacao( String dtCriacaoIni, String dtCriacaoFim ) ;  

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select cr                   "
            + " from ContasReceber cr           "
            + " where cr.descStatusReceber = ?1 " )
    List<ContasReceber> listaContasReceberByDescStatusReceber( StatusReceber descStatusReceber ); 

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select cr             "
            + " from                      "
            + "      ContasReceber cr     "
            + " JOIN cr.entidade   ent    "
            + " where ent.idEntidade = ?1 " )
	List<ContasReceber> listaContasReceberByIdEntidade( Long idEntidade ) ; 

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select cp         "
            + " from                  "
            + "      ContasReceber cp "
            + " JOIN cp.entidade  con "
            + " where upper(trim(con.nomeEntidade)) like upper(concat('%', ?1, '%'))" )
    List<ContasReceber> listaContasReceberByNomeEntidade( String nomeEntidade ); 
	
	/******************************************************************/
	/*                                                                */
	/* Método para deletar uma lista de ciclos por IDs                */
	/*                                                                */
	/******************************************************************/	
    // @Transactional
    @Modifying
    @Query("DELETE FROM ContasReceber c WHERE c.idContasReceber IN :ids")
    void deleteAllRecebimentoByIdIn(@Param("ids") List<Long> ids);
    
	/******************************************************************/
	/*                                                                */
	/* MMétodo padrão para deletar uma lista de entidades             */
	/*                                                                */
	/******************************************************************/	
    // @Transactional
    default void deleteAllCiclosRecebimento(List<ContasReceber> ciclos) {
        deleteAll(ciclos); // O delete em cascata será aplicado automaticamente
    }
    
    /******************************************************************/
  	/*                                                                */
  	/*                                                                */
  	/******************************************************************/	
  	@Query(value = "select count(1)                                                                      "
                   + " from ContasReceber cr                                                             "
                   + " where cr.anoMes            = ?1                                                   "
                   + "   and cr.descStatusReceber IN( 'AGUARDANDO_RECEBIMENTO', 'AGUARDANDO_UPLOAD_NF' ) " )
    Long isExistCicloRecebimentoVenda( String anoMes ) ;  

  	/******************************************************************/
  	/*                                                                */
  	/*                                                                */
  	/******************************************************************/	
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true, value = "UPDATE contas_receber SET status = 'RECEBIMENTO_CANCELADO' WHERE ano_mes = :anoMes and status IN( 'AGUARDANDO_RECEBIMENTO', 'AGUARDANDO_UPLOAD_NF' )")
    int updateStatusCicloRecebimentoVenda(  @Param("anoMes") String anoMes);
    
  	/******************************************************************/
  	/*                                                                */
  	/*                                                                */
  	/******************************************************************/	
    @Query(nativeQuery = true, value = " SELECT EXISTS ( select 1  from CICLO_PAGAMENTO_VENDA cp where cp.ano_mes = :anoMes and cp.STATUS = 'PAGAMENTO' )")
    Boolean isStatusCicloRecebimentoVenda(  @Param("anoMes") String anoMes);
   
    
   
	
}
