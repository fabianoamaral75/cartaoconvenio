package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;

@Repository
public interface TaxaExtraConveniadaRepository  extends JpaRepository<TaxaExtraConveniada, Long>{

}
