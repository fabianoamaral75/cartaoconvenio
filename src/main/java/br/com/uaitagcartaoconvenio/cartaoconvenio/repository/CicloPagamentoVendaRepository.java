package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;

@Repository
@Transactional
public interface CicloPagamentoVendaRepository extends JpaRepository<CicloPagamentoVenda, Long> {


	@Query(value = "select cp                    "
                 + " from CicloPagamentoVenda cp "
                 + " where cp.anoMes = ?1        " )
    List<CicloPagamentoVenda> listaCicloPagamentoVendaByAnoMes( String anoMes ) ;  

	
	@Query(value = "select cp                             "
                 + " from CicloPagamentoVenda cp          "
                 + " where cp.dtCriacao BETWEEN ?1 AND ?2 " )
    List<CicloPagamentoVenda> listaCicloPagamentoVendaByDtCriacao( String dtCriacaoIni, String dtCriacaoFim ) ;  

	@Query(value = "select cp                          "
                 + " from CicloPagamentoVenda cp       "
                 + " where cp.descStatusPagamento = ?1 " )
    List<CicloPagamentoVenda> listaCicloPagamentoVendaByDescStatusPagamento( StatusCicloPgVenda descStatusPagamento ); 
	
	@Query(value = "select cp                     "
                 + " from                         "
                 + "      CicloPagamentoVenda cp  "
                 + " JOIN cp.conveniados     con  "
                 + " where con.idConveniados = ?1 " )
	CicloPagamentoVenda listaCicloPagamentoVendaByIdConveniados( Long idConveniados ) ; 
	
	@Query(value = "select cp                     "
            + " from                         "
            + "      CicloPagamentoVenda cp  "
            + " JOIN cp.conveniados     con  "
            + " JOIN con.pessoa          pe  "
            + " where upper(trim(pe.nomePessoa)) like upper(concat('%', ?1, '%'))" )
    List<CicloPagamentoVenda> listaCicloPagamentoVendaByNomeConveniado( String nomeConveniado ); 

}
