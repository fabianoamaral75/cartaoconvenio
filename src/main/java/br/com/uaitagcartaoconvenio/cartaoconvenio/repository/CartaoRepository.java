package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Cartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Funcionario;


@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {

    @Query("SELECT c FROM Cartao c WHERE c.numeracao = ?1")
    Cartao findByNumeracao(String numeracao);

    @Query("SELECT c FROM Cartao c JOIN c.funcionario f WHERE f.idFuncionario = ?1")
    Cartao listaCartaoByIdFuncionario(Long idFuncionario);

    @Query("SELECT c FROM Cartao c " +
           "JOIN c.funcionario f " +
           "JOIN f.entidade e " +
           "JOIN f.pessoa p " +
           "WHERE UPPER(TRIM(p.nomePessoa)) LIKE UPPER(CONCAT('%', ?1, '%')) " +
           "AND e.idEntidade = ?2")
    List<Cartao> listaCartaoByNomePessoa(String nomePessoa, Long idEntidade);

    @Query("SELECT c FROM Cartao c " +
           "JOIN c.funcionario f " +
           "JOIN f.entidade e " +
           "WHERE c.statusCartao = ?1 " +
           "AND e.idEntidade = ?2")
    List<Cartao> listaCartaoByIdStatus(StatusCartao status, Long idEntidade);
    
    
    @Query("SELECT c FROM Cartao c WHERE c.funcionario = ?1 AND c.statusCartao IN ?2")
    List<Cartao> findByFuncionarioAndStatusCartaoIn(Funcionario funcionario, List<StatusCartao> status);
    
 //   @Query("SELECT c FROM Cartao c WHERE c.numeracao = ?1")
 //   Cartao findByNumeracao(String numeracao);

}