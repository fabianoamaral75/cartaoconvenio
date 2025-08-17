package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TipoPeriodo;

public interface TipoPeriodoRepository extends JpaRepository<TipoPeriodo, Long>{
	boolean existsByDescricao(String descricao);
	
	@Query("SELECT t FROM TipoPeriodo t WHERE t.id = :id")
	Optional<TipoPeriodo> findByIdTipoPeriodo(@Param("id") Long id);
	
}
