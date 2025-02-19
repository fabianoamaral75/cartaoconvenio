package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.RamoAtividade;

@Repository
@Transactional
public interface RamoAtividadeRepository extends JpaRepository<RamoAtividade, Long>{

}
