package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Funcionario;

@Repository
@Transactional
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

	@Query(value = "select en from Funcionario en " )
	List<Funcionario> listaTodasFuncionario( );
	
	@Query(value = "select u from Funcionario u where u.idFuncionario = ?1")
	Funcionario findByIdFuncionario(Long id);
	
	@Query(value = "select f               "
			     + "  from                 "
			     + "    Funcionario f      "
			     + "    JOIN f.pessoa p    "
			     + "   where upper(trim(p.nomePessoa)) like upper(concat('%', ?1, '%'))")
	List<Funcionario> findFuncionarioNome( String nome );

    @Query("SELECT f FROM Funcionario f WHERE f.pessoa.idPessoa = :idPessoa")
    Optional<Funcionario> findByIdPessoa(@Param("idPessoa") Long idPessoa);


}
