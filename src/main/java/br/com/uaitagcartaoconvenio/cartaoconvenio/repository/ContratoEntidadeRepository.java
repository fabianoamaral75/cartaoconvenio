package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoEntidade;

@Repository
public interface ContratoEntidadeRepository extends JpaRepository<ContratoEntidade, Long> {
    
    List<ContratoEntidade> findByEntidadeIdEntidade(Long idEntidade);
    
    List<ContratoEntidade> findByEntidadeIdEntidadeAndStatus(Long idEntidade, Boolean status);
    
    Optional<ContratoEntidade> findByIdContratoEntidadeAndEntidadeIdEntidade(Long idContratoEntidade, Long idEntidade);
}