package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Acesso;

@Repository
public interface AcessoRepository extends JpaRepository<Acesso, Long>{


    @Query("SELECT a FROM Acesso a")
    List<Acesso> buscarListaAcesso();
	
}
