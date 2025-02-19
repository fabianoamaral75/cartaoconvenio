package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Cartao;


@Repository
@Transactional
public interface CartaoRepository extends JpaRepository<Cartao, Long> {

	@Query(value = "select u from Cartao u where u.numeracao = ?1")
	Cartao findByNumeracao(String numeracao);
	
}
