package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxasExtraordinarias;

@Repository
public interface TaxasExtraordinariasRepository extends JpaRepository<TaxasExtraordinarias, Long> {
    
    // Métodos customizados podem ser adicionados aqui
    // Exemplo: List<TaxasExtraordinarias> findByStatusTaxa(String status);
	List<TaxasExtraordinarias> findByStatusTaxa(String status);
}
