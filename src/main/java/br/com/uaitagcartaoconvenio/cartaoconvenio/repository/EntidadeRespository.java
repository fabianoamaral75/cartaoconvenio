package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;


@Repository
@Transactional
public interface EntidadeRespository extends JpaRepository<Entidade, Long> {

	@Query(value = "select u from Entidade u where u.cnpj = ?1")
	Entidade findByCnpj(String cnpj);
	
	@Query(value = "select en from Entidade en " )
	List<Entidade> listaTodasEntidade( );
	
	@Query(value = "select e from Entidade e where upper(trim(e.nomeEntidade)) like upper(concat('%', ?1, '%'))")
	List<Entidade> findEntidadeNome( String nomeEntidade );
	
	@Query(value = "select u from Entidade u where u.idEntidade = ?1")
	Entidade findByIdEntidade(Long idEntidade);

	@Query(value = "select en            "
                 + " from Entidade    en "
                 + " where upper(trim(en.cidade)) like upper(concat('%', ?1, '%'))" )
    List<Entidade> listaEntidadeByCidade( String cidade) ;  
	
	@Query(value = "select ent                "
                 + " from Cartao ca           "
                 + " JOIN ca.funcionario fun  "
                 + " JOIN fun.entidade   ent  "
                 + " where ca.numeracao = ?1  " )
    Entidade findEntidadeByNumCartao( String numCartao ); 

	@Query(value = "SELECT dia_recebimento FROM entidade where id_entidade = ?1", nativeQuery = true)
	int diasRecebimento( Long id );

    // Método para atualizar apenas o mesRecebimentoPosFechamento para um único ID
    @Modifying
    @Query("UPDATE Entidade e SET e.anoMesUltinoFechamento = :mesRecebimento, e.dtAlteracao = CURRENT_TIMESTAMP WHERE e.idEntidade = :id")
    void updateMesRecebimentoPosFechamento(@Param("id") Long id, @Param("mesRecebimento") String mesRecebimento);
    
    // Método para atualizar em lote para múltiplos IDs
    @Modifying
    @Query("UPDATE Entidade e SET e.anoMesUltinoFechamento = :mesRecebimento, e.dtAlteracao = CURRENT_TIMESTAMP WHERE e.idEntidade IN :ids")
    int updateMesRecebimentoPosFechamentoEmLote(@Param("ids") List<Long> ids, @Param("mesRecebimento") String mesRecebimento);
    
    // Alternativa usando o método save padrão do JPA para múltiplos IDs
    default void atualizarMesRecebimentoEmLote(List<Long> ids, String mesRecebimento) {
        List<Entidade> entidades = findAllById(ids);
        entidades.forEach(entidade -> {
            entidade.setAnoMesUltinoFechamento(mesRecebimento);
        });
        saveAll(entidades);
    }

    
    /**
     * Verifica se já existe uma entidade com o CNPJ (exceto a entidade com o ID especificado)
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Entidade e WHERE e.cnpj = :cnpj AND e.idEntidade != :id")
    boolean existsByCnpjAndIdNot(@Param("cnpj") String cnpj, @Param("id") Long id);
    
    /**
     * Verifica se existe uma entidade com o CNPJ
     */
    boolean existsByCnpj(String cnpj);
    
    /**
     * Busca entidade por ID com todos os relacionamentos carregados
     */
    @Query("SELECT e FROM Entidade e " +
           "LEFT JOIN FETCH e.taxaEntidade " +
           "LEFT JOIN FETCH e.taxaCalcLimiteCreditoFunc " +
           "LEFT JOIN FETCH e.contratoEntidade ce " +
           "LEFT JOIN FETCH ce.arquivos " +
           "LEFT JOIN FETCH ce.vigencias " +
           "LEFT JOIN FETCH ce.servicos " +
           "WHERE e.idEntidade = :id")
    Entidade findByIdWithRelationships(@Param("id") Long id);    
 
    
    // Pesquisar todas as entidades ordenadas por idEntidade (limit 10)
    List<Entidade> findTop10ByOrderByIdEntidade();
    
    // Pesquisar entidades por parte do nome, ordenadas por idEntidade (limit 10)
    List<Entidade> findTop10ByNomeEntidadeContainingIgnoreCaseOrderByIdEntidade(String parteNome);
    
    // Alternativa com paginação (mais flexível)
    Page<Entidade> findAllByOrderByIdEntidade(Pageable pageable);
    
    Page<Entidade> findByNomeEntidadeContainingIgnoreCaseOrderByIdEntidade(String parteNome, Pageable pageable);
    
    // Alternativa com JPQL
    @Query("SELECT e FROM Entidade e WHERE UPPER(e.nomeEntidade) LIKE UPPER(CONCAT('%', :parteNome, '%')) ORDER BY e.idEntidade")
    List<Entidade> pesquisarPorParteDoNome(@Param("parteNome") String parteNome, Pageable pageable);
}
