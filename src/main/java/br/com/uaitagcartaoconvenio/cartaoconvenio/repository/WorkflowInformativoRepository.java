package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.WorkflowInformativo;

@Repository
public interface WorkflowInformativoRepository extends JpaRepository<WorkflowInformativo, Long> {
	
    
    
}
