package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaPg;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaReceb;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.LimiteCredito;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Venda;

@Repository
@Transactional
public interface VendaRepository extends JpaRepository<Venda, Long> {

	@Query(value = "select ven from Venda ven where ven.idVenda = ?1")
	Venda findVendaByIdVenda(Long idVenda);

	@Query(value = "select ven             "
                 + " from Venda ven        "
                 + " where ven.anoMes = ?1 " )
    List<Venda> listaVendaByAnoMes( String anoMes ) ;  
	
	@Query(value = "select ven                           "
                 + " from Venda ven                      "
                 + " where ven.dtVenda BETWEEN ?1 AND ?2 " )
    List<Venda> listaVendaByDtVenda( Date dtVendaIni, Date dtVendaFim ) ; 
	
	@Query(value = "select ven                "
                 + " from Venda ven           "
                 + " where ven.loginUser = ?1 " )
    List<Venda> listaVendaByLoginUser( String loginUser ) ;
	
	@Query(value = "select ven                           "
                 + "  from Venda ven                     "
                 + " where ven.descStatusVendaReceb = ?1 " )
    List<Venda> listaVendaByDescStatusVendaReceb( StatusVendaReceb descStatusVendaReceb ); 

	@Query(value = "select ven                        "
                 + "  from Venda ven                  "
                 + " where ven.descStatusVendaPg = ?1 " )
    List<Venda> listaVendaByDescStatusVendaPg( StatusVendaPg descStatusVendaPg ); 

	@Query(value = "select ven                       "
                 + "  from Venda ven                 "
                 + " where ven.descStatusVendas = ?1 " )
   List<Venda> listaVendaByDescStatusVendas( StatusVendas descStatusVendas ); 
	
	@Query(value = "select ven                    "
                 + " from                         "
                 + "       Venda             ven  "
                 + "  JOIN ven.conveniados   con  "
                 + " where con.idConveniados = ?1 " )
	List<Venda> listaVendaByIdConveniados( Long idConveniados ) ; 
	
	@Query(value = "select ven                    "
                 + " from                         "
                 + "       Venda             ven  "
                 + "  JOIN ven.conveniados   con  "
                 + " where ven.anoMes        = ?1 "
                 + "   and con.idConveniados = ?2 " )
    List<Venda> listaVendaByIdConveniadosAnoMes( String anoMes, Long idConveniados ) ; 
	
	@Query(value = "select ven                           "
                 + " from                                "
                 + "       Venda             ven         "
                 + "  JOIN ven.conveniados   con         "
                 + " where ven.dtVenda BETWEEN ?1 AND ?2 "
                 + "   and con.idConveniados = ?3        " )
    List<Venda> listaVendaByIdConveniadosDtVenda( Date dtVendaIni, Date dtVendaFim, Long idConveniados ) ; 
	
	@Query(value = "select ven                       "
                 + " from                            "
                 + "       Venda             ven     "
                 + "  JOIN ven.conveniados   con     "
                 + " where ven.descStatusVendas = ?1 "
                 + "   and con.idConveniados    = ?2 " )
   List<Venda> listaVendaByIdConveniadosDescStatusVendas( StatusVendas descStatusVendas, Long idConveniados ) ; 

   @Query(value = "select ven                        "
                + " from                             "
                + "       Venda             ven      "
                + "  JOIN ven.conveniados   con      "
                + " where ven.descStatusVendaPg = ?1 "
                + "   and con.idConveniados     = ?2 " )
   List<Venda> listaVendaByIdConveniadosDescStatusVendaPg( StatusVendaPg descStatusVendaPg, Long idConveniados ) ; 

   @Query(value = "select ven                        "
                + " from                             "
                + "       Venda             ven      "
                + "  JOIN ven.conveniados   con      "
                + " where ven.descStatusVendaReceb = ?1 "
                + "   and con.idConveniados        = ?2 " )
   List<Venda> listaVendaByIdConveniadosDescStatusVendaReceb( StatusVendaReceb descStatusVendaReceb, Long idConveniados ) ; 

   @Query(value = "select ven                     "
                + "  from                         "
                + "       Venda ven               "
                + "  JOIN ven.conveniados     con "
                + "  JOIN con.pessoa          pe  "
                + " where upper(trim(pe.nomePessoa)) like upper(concat('%', ?1, '%'))" )
   List<Venda> listaVendaByNomeConveniado( String nomeConveniado ); 
 
   @Query(value = "select ven                "
                + "  from                    "
                + "       Venda          ven "
                + "  JOIN ven.cartao     car "
                + " where car.numeracao = ?1 " )
   List<Venda> listaVendaByCartao( String numCartao ); 
   
   @Query(value = "select ven                       "
                + "  from                           "
                + "       Venda      ven            "
                + "  JOIN ven.cartao car            "
                + " where ven.descStatusVendas = ?1 "
                + "   and car.numeracao        = ?2 " )
   List<Venda> listaVendaByCartaoDescStatusVendas( StatusVendas descStatusVendas, String numCatao );
   
   @Query(value = "select tax                              "
                + "  from                                  "
                + "       Cartao           car             "
                + "  JOIN car.funcionario  fun             "
                + "  JOIN fun.entidade     ent             "
                + "  JOIN ent.taxaEntidade tax             "
                + " where car.numeracao          = ?1      "
                + "   and tax.statusTaxaEntidade = 'ATUAL' " )
   TaxaEntidade taxaEntidadeByNumeroCatao( String numCatao );

   @Query(value = "select lim                  "
           + "  from                      "
           + "       Cartao           car "
           + " JOIN car.funcionario   fun "
           + " JOIN fun.limiteCredito lim "
           + "  JOIN fun.pessoa       pes "
           + " JOIN pes.usuario       usu "
           + " where car.numeracao = ?1   "
           + "   and usu.senha     = ?2   " )
   LimiteCredito validaVendaByCartaoSenha( String numCatao, String pass ); 

   @Modifying(flushAutomatically = true)
   @Query(nativeQuery = true, value = "UPDATE venda SET status = ?2 WHERE id_venda = ?1")
   void updateStatusVendas(Long id, String descStatusVendas );		

   @Modifying(flushAutomatically = true)
   @Query(nativeQuery = true, value = "UPDATE venda SET status_venda_paga = ?2 WHERE id_venda = ?1")
   void updateStatusVendaPg(Long id, String descStatusVendaPg );	
	
   @Modifying(flushAutomatically = true)
   @Query(nativeQuery = true, value = "UPDATE venda SET status_venda_recebida = ?2 WHERE id_venda = ?1")
   void updateStatusVendaReceb(Long id, String descStatusVendaReceb );		


   @Modifying(flushAutomatically = true)
   @Query(nativeQuery = true, value = "UPDATE limite_credito SET valor_utilizado = ?2 WHERE id_limite_credito = ?1")
   void updateValorLimiteCredito(Long idLimiteCredito, BigDecimal valorAtualizado );		
 
   @Modifying(flushAutomatically = true)
   @Query(nativeQuery = true, value = "UPDATE venda SET valor_calc_taxa_entidade = ?2 WHERE id_venda = ?1")
   void updateValorCalcTaxaEntidade(Long idVenda, BigDecimal valorAtualizado );		

}

