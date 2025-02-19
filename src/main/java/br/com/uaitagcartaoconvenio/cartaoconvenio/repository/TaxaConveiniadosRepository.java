package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveiniados;

@Repository
@Transactional
public interface TaxaConveiniadosRepository extends JpaRepository<TaxaConveiniados, Long>{
	

}
