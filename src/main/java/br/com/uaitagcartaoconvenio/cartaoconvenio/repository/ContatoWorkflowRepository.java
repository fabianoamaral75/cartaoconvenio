package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContatoWorkflow;

@Repository
public interface ContatoWorkflowRepository extends JpaRepository<ContatoWorkflow, Long> {
	
    List<ContatoWorkflow> findByWorkflowInformativoIdWorkflowInformativo(Long id);

}

