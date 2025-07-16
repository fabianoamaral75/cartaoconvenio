package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Salario;

@Repository
public interface SalarioRepository extends JpaRepository<Salario, Long> {
	
    @Query("SELECT s FROM Salario s JOIN s.funcionario f JOIN f.entidade e GROUP BY e, f.secretaria")
    List<Salario> findAllGroupByEntidadeAndSecretaria();
    
    @Query("SELECT s FROM Salario s JOIN FETCH s.funcionario f JOIN FETCH f.entidade e WHERE e.idEntidade = :idEntidade GROUP BY e, f.secretaria")
    List<Salario> findByEntidadeIdGrouped(@Param("idEntidade") Long idEntidade);
       
    @Query("SELECT s FROM Salario s JOIN s.funcionario f JOIN f.entidade e WHERE e.idEntidade = :idEntidade")
    List<Salario> findAllByEntidadeIdEntidade(@Param("idEntidade") Long idEntidade);
     
     @Query("SELECT s FROM Salario s WHERE s.funcionario.idFuncionario = :idFuncionario")
    Salario findByFuncionarioIdFuncionario(@Param("idFuncionario") Long idFuncionario);
    
}