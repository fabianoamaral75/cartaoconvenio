package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TipoPeriodo;

public interface TipoPeriodoRepository extends JpaRepository<TipoPeriodo, Long>{
	boolean existsByDescricao(String descricao);
}
