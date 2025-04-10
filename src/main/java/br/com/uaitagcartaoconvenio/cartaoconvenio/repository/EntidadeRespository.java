package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

	@Query(value = "SELECT qty_dias_recebimento FROM entidade where id_entidade = ?1", nativeQuery = true)
	int qtyDiasRecebimento( Long id );

}
