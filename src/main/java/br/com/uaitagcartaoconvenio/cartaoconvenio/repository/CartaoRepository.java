package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Cartao;


@Repository
@Transactional
public interface CartaoRepository extends JpaRepository<Cartao, Long> {

	@Query(value = "select u from Cartao u where u.numeracao = ?1")
	Cartao findByNumeracao(String numeracao);

	
	@Query(value = "select car                    "
                 + " from Cartao          car     "
                 + " JOIN car.funcionario fun     "
                 + " where fun.idFuncionario = ?1 " )
	Cartao listaCartaoByIdFuncionario( Long idFuncionario ) ;  

	@Query(value = "select car                    "
                 + " from Cartao          car     "
                 + " JOIN car.funcionario fun     "
                 + " JOIN fun.pessoa      pes     "
                 + " where upper(trim(pes.nomePessoa)) like upper(concat('%', ?1, '%'))" )
	List<Cartao> listaCartaoByNomePessoa( String nomePessoa ) ;  

	@Query(value = "select car                   "
                 + " from Cartao car             "
                 + " where car.statusCartao = ?1 " )
	List<Cartao> listaCartaoByIdStatus( StatusCartao statusCartao ) ;  

}
