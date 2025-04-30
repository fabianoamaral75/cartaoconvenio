package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT f FROM Pessoa f WHERE f.conveniados.idConveniados = :idConveniado and f.pessoaJuridica != null")
    Optional<Pessoa> findPessoaByIdConveniado(@Param("idConveniado") Long idConveniado);
    
  	/******************************************************************/
  	/*                                                                */
  	/*                                                                */
  	/******************************************************************/	
    @Query(nativeQuery = true, 
    		value = "SELECT pe.*                       "
    		      + "  FROM pessoa pe                  "
    		      + "  ,    pessoa_juridica pj         "
    		      + " where pe.id_conveniados = :id    "
    		      + "   and pj.id_pessoa = pe.id_pessoa")
    Optional<Pessoa> getPessoaConveniadaPJ(  @Param("id") Long id);

}
