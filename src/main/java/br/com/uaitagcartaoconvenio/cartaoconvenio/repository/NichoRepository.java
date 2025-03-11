package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Nicho;

@Repository
@Transactional
public interface NichoRepository extends JpaRepository<Nicho, Long>{

	@Query(value = "select u from Nicho u")
	List<Nicho> getAllNicho();
	
	@Query(value = "select e from Nicho e where upper(trim(e.descNicho)) like upper(concat('%', ?1, '%'))")
	List<Nicho> findNichoNome( String nomeNicho );
	
	@Query(value = "select e from Nicho e where e.idNicho = ?1")
	Nicho findNichoById( Long id );

}
