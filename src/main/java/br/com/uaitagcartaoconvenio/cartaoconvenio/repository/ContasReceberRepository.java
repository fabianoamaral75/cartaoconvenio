package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceber;

@Repository
@Transactional
public interface ContasReceberRepository extends JpaRepository<ContasReceber, Long>{

	
	@Query(value = "select cr         "
            + " from ContasReceber cr "
            + " where cr.anoMes = ?1  " )
    List<ContasReceber> listaContasReceberByAnoMes( String anoMes ) ;  
	
	@Query(value = "select cr                        "
            + " from ContasReceber cr                "
            + " where cr.dtCriacao BETWEEN ?1 AND ?2 " )
    List<ContasReceber> listaContasReceberByDtCriacao( String dtCriacaoIni, String dtCriacaoFim ) ;  

	@Query(value = "select cr                   "
            + " from ContasReceber cr           "
            + " where cr.descStatusReceber = ?1 " )
    List<ContasReceber> listaContasReceberByDescStatusReceber( StatusReceber descStatusReceber ); 

	@Query(value = "select cr             "
            + " from                      "
            + "      ContasReceber cr     "
            + " JOIN cr.entidade   ent    "
            + " where ent.idEntidade = ?1 " )
    ContasReceber listaContasReceberByIdEntidade( Long idEntidade ) ; 

	@Query(value = "select cp         "
            + " from                  "
            + "      ContasReceber cp "
            + " JOIN cp.entidade  con "
            + " where upper(trim(con.nomeEntidade)) like upper(concat('%', ?1, '%'))" )
    List<ContasReceber> listaContasReceberByNomeEntidade( String nomeEntidade ); 
	
}
