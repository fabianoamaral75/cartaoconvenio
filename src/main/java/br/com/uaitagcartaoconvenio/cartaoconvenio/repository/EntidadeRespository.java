package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;


@Repository
@Transactional
public interface EntidadeRespository extends JpaRepository<Entidade, Long> {

	@Query(value = "select u from Entidade u where u.cnpj = ?1")
	Entidade findByCnpj(String cnpj);
	
	
	/*
	 	@Query(value = "select u from Usuario u where u.login = ?1")
	Usuario findByLogin(String login);

	 */
	
}
