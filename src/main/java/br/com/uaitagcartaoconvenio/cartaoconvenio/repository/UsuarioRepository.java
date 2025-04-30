package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PessoaFisica;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PessoaJuridica;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Usuario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.UsuarioAcesso;


@Repository
@Transactional
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


	/******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
	@Query("SELECT pf FROM PessoaFisica pf WHERE pf.cpf = :cpf")
    PessoaFisica pesquisaPorCpfPF(@Param("cpf") String cpf);
	
	/******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
	@Query("SELECT pj FROM PessoaJuridica pj WHERE pj.cnpj = :cnpj")
    PessoaJuridica pesquisaPorCnpjPJ(@Param("cnpj") String cnpj);
    
	/******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    @Query("SELECT u FROM Usuario u WHERE u.login = :login")
    Optional<Usuario> findByLogin(@Param("login") String login);
	
	/******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    @Query("SELECT u FROM Usuario u WHERE u.login = :login")
    UserDetails findByLoginDet(String login);

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    @Modifying
    @Query("UPDATE Usuario us SET us.senha = :pass WHERE us.login = :login")
    int updatePass( @Param("login") String login, @Param("pass") String pass );
	
	/******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    @Query(nativeQuery = true, value = "SELECT EXISTS ( SELECT * FROM usuario where login = :login )" )
    Boolean isLogin( @Param("login") String login );		
	
	/******************************************************************/
   /*                                                                */
   /*                                                                */
   /******************************************************************/	
    @Query(value = " select ua                    "
                 + " from                         "
                 + "      Usuario          us     "
                 + " JOIN us.usuarioAcesso ua     "
                 + " where us.idUsuario = :idUser " )
    List<UsuarioAcesso> getUsuarioAcessoByIdUser(@Param("idUser") Long idUser);
    
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.usuarioAcesso ua LEFT JOIN FETCH ua.acesso WHERE u.login = :login")
    Optional<Usuario> findByLoginWithAcessos(@Param("login") String login);
    
}














