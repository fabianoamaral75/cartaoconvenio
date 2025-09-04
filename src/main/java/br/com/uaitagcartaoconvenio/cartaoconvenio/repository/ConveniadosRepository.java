package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniados;

@Repository
@Transactional
public interface ConveniadosRepository extends JpaRepository<Conveniados, Long>{
	
	@Query(value = "select tx                             "
			     + "  from Conveniados con                "
			     + "   join con.taxaConveniados tx        "
			     + " where con.idConveniados = ?1 "
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

   @Query("SELECT DISTINCT c FROM Conveniados c JOIN c.pessoa p WHERE UPPER(p.nomePessoa) LIKE UPPER(CONCAT('%', :nome, '%'))")
   List<Conveniados> listaConveniadosByNome(@Param("nome") String nome);
    
   @Query("SELECT DISTINCT c FROM Conveniados c JOIN c.pessoa p WHERE p.cidade = :cidade")
   List<Conveniados> listaConveniadosByCidade(@Param("cidade") String cidade);

	@Query(value = "SELECT DIA_PAGAMENTO FROM conveniados where id_conveniados = ?1", nativeQuery = true)
	int getDiasPagamento( Long id );

	@Query(value = "SELECT pes.nome_pessoa                         "
			     + "FROM                                           "
			     + "   conveniados     AS con                      "
			     + " , pessoa          AS pes                      "
			     + " , pessoa_juridica AS pju                      "
			     + " WHERE con.id_conveniados = ?1             "
			     + "   AND pes.id_conveniados = con.id_conveniados "
			     + "   and pju.id_pessoa  = pes.id_pessoa          ", nativeQuery = true)
	String getNomeConveniada( Long id );

	
	// Método para atualizar em lote para múltiplos IDs
    @Modifying
    @Query("UPDATE Conveniados e SET e.anoMesUltinoFechamento = :mesRecebimento, e.dtAlteracao = CURRENT_TIMESTAMP WHERE e.idConveniados IN :ids")
    int updateMesRecebimentoPosFechamentoEmLote(@Param("ids") List<Long> ids, @Param("mesRecebimento") String mesRecebimento);

    @Modifying
    @Query("UPDATE Conveniados c SET c.descStatusConveniada = :status WHERE c.idConveniados = :id")
    void atualizarStatus(@Param("id") Long id, @Param("status") StatusConveniada status);

    
    @Query("SELECT DISTINCT c FROM Conveniados c LEFT JOIN FETCH c.pessoa ORDER BY c.idConveniados")
    List<Conveniados> findTop10ByOrderByIdConveniados(Pageable pageable);
    
       
    @Query("SELECT DISTINCT c FROM Conveniados c LEFT JOIN FETCH c.pessoa p " +
    	       "WHERE UPPER(p.nomePessoa) LIKE UPPER(CONCAT('%', :parteNome, '%')) " +
    	       "ORDER BY c.idConveniados")
    	List<Conveniados> findTop10ByPessoaNomePessoaContainingIgnoreCase(@Param("parteNome") String parteNome, Pageable pageable);
    
 
    
    @EntityGraph(attributePaths = {"pessoa"})
    @Query("SELECT c FROM Conveniados c JOIN c.pessoa p " +
           "ORDER BY c.idConveniados")
    Page<Conveniados> findAllByOrderByIdConveniados(Pageable pageable);
   
    

    @EntityGraph(attributePaths = {"pessoa"})
    @Query("SELECT c FROM Conveniados c JOIN c.pessoa p " +
           "WHERE UPPER(p.nomePessoa) LIKE UPPER(CONCAT('%', :parteNome, '%')) " +
           "ORDER BY c.idConveniados")
    Page<Conveniados> findByPessoaNomePessoaContainingIgnoreCase(@Param("parteNome") String parteNome, Pageable pageable);
        
 // No repositório
    @Query("SELECT c FROM Conveniados c " +
           "WHERE c.idConveniados = :id")
    Optional<Conveniados> findByIdWithBasicRelationships(@Param("id") Long id);

    // Método para buscar conveniada por ID com pessoa jurídica (usando JPQL)
    @Query("SELECT c FROM Conveniados c " +
           "JOIN FETCH c.pessoa p " +
           "LEFT JOIN FETCH p.pessoaJuridica pj " +
           "WHERE c.idConveniados = :idConveniados")
    Optional<Conveniados> findConveniadaComPessoaJuridicaById(@Param("idConveniados") Long idConveniados);
}
