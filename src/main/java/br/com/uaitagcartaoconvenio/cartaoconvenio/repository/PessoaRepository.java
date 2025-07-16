package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query("UPDATE Pessoa p SET p.nomePessoa = :nome, p.logradoro = :logradouro, p.uf = :uf, p.cidade = :cidade, p.cep = :cep, p.numero = :numero, p.complemento = :complemento, p.bairro = :bairro, p.email = :email, p.telefone = :telefone WHERE p.idPessoa = :id")
    void atualizarPessoaCompleta(
        @Param("id") Long idPessoa,
        @Param("nome") String nomePessoa,
        @Param("logradouro") String logradoro,
        @Param("uf") String uf,
        @Param("cidade") String cidade,
        @Param("cep") String cep,
        @Param("numero") String numero,
        @Param("complemento") String complemento,
        @Param("bairro") String bairro,
        @Param("email") String email,
        @Param("telefone") String telefone);

    @Modifying
    @Query("UPDATE Pessoa p SET p.conveniados.idConveniados = :idConveniados WHERE p.idPessoa = :id")
    void atualizarConveniadoPessoa(@Param("id") Long idPessoa, @Param("idConveniados") Long idConveniados);
    
    @Modifying
    @Query("UPDATE Pessoa p SET p.nomePessoa = :nome, p.email = :email, p.telefone = :telefone WHERE p.idPessoa = :id")
    void atualizarDadosBasicosPessoa(
        @Param("id") Long idPessoa,
        @Param("nome") String nomePessoa,
        @Param("email") String email,
        @Param("telefone") String telefone);

    @Modifying
    @Query("UPDATE Pessoa p SET p.logradoro = :logradouro, p.uf = :uf, p.cidade = :cidade, p.cep = :cep, " +
           "p.numero = :numero, p.complemento = :complemento, p.bairro = :bairro WHERE p.idPessoa = :id")
    void atualizarEnderecoPessoa(
        @Param("id") Long idPessoa,
        @Param("logradouro") String logradoro,
        @Param("uf") String uf,
        @Param("cidade") String cidade,
        @Param("cep") String cep,
        @Param("numero") String numero,
        @Param("complemento") String complemento,
        @Param("bairro") String bairro);
}
