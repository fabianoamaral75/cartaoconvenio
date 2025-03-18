package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Pessoa;

@Repository
@Transactional
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	@Query(value = "select pe                "
                 + " from                    "
                 + "      Pessoa          pe "
                 + " JOIN pe.pessoaFisica pf "
                 + " where pf.cpf = ?1       " )
    List<Pessoa> listaPessoaFisicaByCpf( String cpf) ;  

	@Query(value = "select pe                 "
                 + " from Pessoa    pe        "
                 + " where upper(trim(pe.nomePessoa)) like upper(concat('%', ?1, '%'))" )
    List<Pessoa> listaPessoaFisicaByNome( String Nome) ;  

	@Query(value = "select pe                  "
                 + " from                      "
                 + "      Pessoa  pe           "
                 + " JOIN pe.pessoaJuridica pj "
                 + " where pj.cnpj = ?1        " )
    List<Pessoa> listaPessoaJuridicaByCnpj( String cnpj) ;  


}
