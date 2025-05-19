package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoEntidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratoEntidadeRepository extends JpaRepository<ContratoEntidade, Long> {
}