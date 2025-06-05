package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Secretaria;

@Repository
@Transactional
public interface SecretariaRepository extends JpaRepository<Secretaria, Long>{
	
   /******************************************************************/
   /*                                                                */
   /*                                                                */
   /******************************************************************/	
	@Query(value = "select sec                  "
                 + " from                       "
                 + "      Secretaria sec        "
                 + " JOIN sec.entidade   ent    "
                 + " where ent.idEntidade = :id " )
	List<Secretaria> listaSecretariaByIdEntidade( @Param("id") Long id) ;

   /******************************************************************/
   /*                                                                */
   /*                                                                */
   /******************************************************************/	
	@Query(value = "select sec                     "
                 + " from                          "
                 + "      Secretaria sec           "
                 + " JOIN sec.funcionario   func   "
                 + " where func.idFuncionario = :id " )
    List<Secretaria> listaSecretariaByIdFuncionario( @Param("id") Long id) ;

   /******************************************************************/
   /*                                                                */
   /*                                                                */
   /******************************************************************/	
   @Query("SELECT sec FROM Secretaria sec WHERE sec.idSecretaria = :id")
   Optional<Secretaria> findByIdSecretaria(@Param("id") Long id);

   
   /******************************************************************/
   /*                                                                */
   /*                                                                */
   /******************************************************************/	
	@Query(value = "select sec from  Secretaria sec " )
	List<Secretaria> listaAllSecretaria( ) ;

}
