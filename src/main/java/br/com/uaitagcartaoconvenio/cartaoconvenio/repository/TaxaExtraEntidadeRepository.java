package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraEntidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxaExtraEntidadeRepository extends JpaRepository<TaxaExtraEntidade, Long> {
}