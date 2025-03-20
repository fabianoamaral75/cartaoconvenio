package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.RamoAtividade;

@Repository
@Transactional
public interface RamoAtividadeRepository extends JpaRepository<RamoAtividade, Long>{

	@Query(value = "select u from RamoAtividade u")
	List<RamoAtividade> findAllRamoAtividade();

	
}
