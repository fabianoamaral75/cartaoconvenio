package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaPg;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaReceb;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.LimiteCredito;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Venda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.DadosFechamentoRecebimentoCicloDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection.DadosFechamentoPagamentoCicloProjection;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection.DadosFechamentoRecebimentoCicloProjection;

@Repository
@Transactional
public interface VendaRepository extends JpaRepository<Venda, Long> {

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select ven from Venda ven where ven.idVenda = ?1 and ven.descStatusVendas in ('AGUARDANDO_PAGAMENTO', 'PAGAMENTO_NAO_APROVADO')")
	Venda findVendaByIdVenda(Long idVenda);
	
	@Query("SELECT v FROM Venda v WHERE v.idVenda = :idVendaLista")
	List<Venda> findVendasByIdVendaLista(@Param("idVendaLista") Long idVendaLista);

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select ven             "
                 + " from Venda ven        "
                 + " where ven.anoMes = ?1 " )
    List<Venda> listaVendaByAnoMes( String anoMes ) ;  
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select ven                           "
                 + " from Venda ven                      "
                 + " where ven.dtVenda BETWEEN ?1 AND ?2 " )
    List<Venda> listaVendaByDtVenda( Date dtVendaIni, Date dtVendaFim ) ; 
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select ven                "
                 + " from Venda ven           "
                 + " where ven.loginUser = ?1 " )
    List<Venda> listaVendaByLoginUser( String loginUser ) ;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select ven                           "
                 + "  from Venda ven                     "
                 + " where ven.descStatusVendaReceb = ?1 " )
    List<Venda> listaVendaByDescStatusVendaReceb( StatusVendaReceb descStatusVendaReceb ); 

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select ven                        "
                 + "  from Venda ven                  "
                 + " where ven.descStatusVendaPg = ?1 " )
    List<Venda> listaVendaByDescStatusVendaPg( StatusVendaPg descStatusVendaPg ); 

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select ven                       "
                 + "  from Venda ven                 "
                 + " where ven.descStatusVendas = ?1 " )
   List<Venda> listaVendaByDescStatusVendas( StatusVendas descStatusVendas ); 
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select ven                    "
                 + " from                         "
                 + "       Venda             ven  "
                 + "  JOIN ven.conveniados   con  "
                 + " where con.idConveniados = ?1 " )
	List<Venda> listaVendaByIdConveniados( Long idConveniados ) ; 
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select ven                    "
                 + " from                         "
                 + "       Venda             ven  "
                 + "  JOIN ven.conveniados   con  "
                 + " where ven.anoMes        = ?1 "
                 + "   and con.idConveniados = ?2 " )
    List<Venda> listaVendaByIdConveniadosAnoMes( String anoMes, Long idConveniados ) ; 
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select ven                           "
                 + " from                                "
                 + "       Venda             ven         "
                 + "  JOIN ven.conveniados   con         "
                 + " where ven.dtVenda BETWEEN ?1 AND ?2 "
                 + "   and con.idConveniados = ?3        " )
    List<Venda> listaVendaByIdConveniadosDtVenda( Date dtVendaIni, Date dtVendaFim, Long idConveniados ) ; 
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Query(value = "select ven                       "
                 + " from                            "
                 + "       Venda             ven     "
                 + "  JOIN ven.conveniados   con     "
                 + " where ven.descStatusVendas = ?1 "
                 + "   and con.idConveniados    = ?2 " )
   List<Venda> listaVendaByIdConveniadosDescStatusVendas( StatusVendas descStatusVendas, Long idConveniados ) ; 
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Query(value = "select ven                                            "
                + " from                                                 "
                + "       Venda             ven                          "
                + "  JOIN ven.conveniados   con                          "
                + " where ven.anoMes            = ?1                     "
                + "   and ven.descStatusVendas  = ?2                     "
                + "   and ven.descStatusVendaPg = 'AGUARDANDO_PAGAMENTO' "
                + "   and con.idConveniados     = ?3                     " )
   List<Venda> listaVendaByIdConveniadosStatusAnoMes( String anoMes, StatusVendas descStatusVendas, Long idConveniados ) ; 

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Query(value = "select ven.*                                                 "
   		        + "  FROM                                                       "
   		        + "   venda       ven                                           "
   		        + "	, cartao      car                                           "
   		        + "	, funcionario fun                                           "
   		        + " WHERE ven.ano_mes           = ?1                            "
   		        + "   AND ven.status            = ?2                            "
   		        + "   AND ven.status_venda_recebida = 'AGURARDANDO_RECEBIMENTO' "
   		        + "   AND car.id_cartao         = ven.id_cartao                 "
   		        + "   AND fun.id_funcionario    = car.id_funcionario            "
   		        + "   AND fun.id_entidade       = ?3                            ", nativeQuery = true )
   List<Venda> listaVendaByIdEntidadeStatusAnoMes( String anoMes, String descStatusVendas, Long idEntidade ) ; 

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Query(value = "select ven                        "
                + " from                             "
                + "       Venda             ven      "
                + "  JOIN ven.conveniados   con      "
                + " where ven.descStatusVendaPg = ?1 "
                + "   and con.idConveniados     = ?2 " )
   List<Venda> listaVendaByIdConveniadosDescStatusVendaPg( StatusVendaPg descStatusVendaPg, Long idConveniados ) ; 

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Query(value = "select ven                        "
                + " from                             "
                + "       Venda             ven      "
                + "  JOIN ven.conveniados   con      "
                + " where ven.descStatusVendaReceb = ?1 "
                + "   and con.idConveniados        = ?2 " )
   List<Venda> listaVendaByIdConveniadosDescStatusVendaReceb( StatusVendaReceb descStatusVendaReceb, Long idConveniados ) ; 

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Query(value = "select ven                     "
                + "  from                         "
                + "       Venda ven               "
                + "  JOIN ven.conveniados     con "
                + "  JOIN con.pessoa          pe  "
                + " where upper(trim(pe.nomePessoa)) like upper(concat('%', ?1, '%'))" )
   List<Venda> listaVendaByNomeConveniado( String nomeConveniado ); 
 
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Query(value = "select ven                "
                + "  from                    "
                + "       Venda          ven "
                + "  JOIN ven.cartao     car "
                + " where car.numeracao = ?1 " )
   List<Venda> listaVendaByCartao( String numCartao ); 
   
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Query(value = "select ven                       "
                + "  from                           "
                + "       Venda      ven            "
                + "  JOIN ven.cartao car            "
                + " where ven.descStatusVendas = ?1 "
                + "   and car.numeracao        = ?2 " )
   List<Venda> listaVendaByCartaoDescStatusVendas( StatusVendas descStatusVendas, String numCatao );
   
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Query(value = "select tax                              "
                + "  from                                  "
                + "       Cartao           car             "
                + "  JOIN car.funcionario  fun             "
                + "  JOIN fun.entidade     ent             "
                + "  JOIN ent.taxaEntidade tax             "
                + " where car.numeracao          = ?1      "
                + "   and tax.statusTaxaEntidade = 'ATUAL' " )
   TaxaEntidade taxaEntidadeByNumeroCatao( String numCatao );

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Query(value = "select lim                  "
                + "  from                      "
                + "       Cartao           car "
                + " JOIN car.funcionario   fun "
                + " JOIN fun.limiteCredito lim "
                + " JOIN fun.pessoa        pes "
                + " JOIN pes.usuario       usu "
                + " where car.numeracao = ?1"
                + "   and usu.senha     = ?2    " )
   LimiteCredito validaVendaByCartaoSenha( String numCatao, String pass ); 
   
   @Query(value = "select lim                  "
                + "  from                      "
                + "       Cartao           car "
                + " JOIN car.funcionario   fun "
                + " JOIN fun.limiteCredito lim "
                + " JOIN fun.pessoa        pes "
                + " JOIN pes.usuario       usu "
                + " where car.numeracao = ?1")
   LimiteCredito findByCartaoNumero(String numCartao);

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Modifying(flushAutomatically = true)
   @Query(nativeQuery = true, value = "UPDATE venda SET status = ?2 WHERE id_venda = ?1")
   void updateStatusVendas(Long id, String descStatusVendas );		

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Modifying(flushAutomatically = true)
   @Query(nativeQuery = true, value = "UPDATE venda SET status_venda_paga = ?2 WHERE id_venda = ?1")
   void updateStatusVendaPg(Long id, String descStatusVendaPg );	
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Modifying(flushAutomatically = true)
   @Query(nativeQuery = true, value = "UPDATE venda SET status_venda_recebida = ?2 WHERE id_venda = ?1")
   void updateStatusVendaReceb(Long id, String descStatusVendaReceb );		


	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Modifying(flushAutomatically = true)
   @Query(nativeQuery = true, value = "UPDATE limite_credito SET valor_utilizado = ?2 WHERE id_limite_credito = ?1")
   void updateValorLimiteCredito(Long idLimiteCredito, BigDecimal valorAtualizado );		
 
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Modifying(flushAutomatically = true)
   @Query(nativeQuery = true, value = "UPDATE venda SET valor_calc_taxa_entidade = ?2 WHERE id_venda = ?1")
   void updateValorCalcTaxaEntidade(Long idVenda, BigDecimal valorAtualizado );		

   /******************************************************************/
   /*                                                                */
   /*                                                                */
   /******************************************************************/	
   @Modifying(flushAutomatically = true)
   @Query(nativeQuery = true, value = "UPDATE                                              "
   		                            + " venda SET                                          "
   		                            + "   status_venda_recebida = 'AGUARDANDO_FECHAMENTO'  "
   		                            + " WHERE ano_mes = ?1                         "
   		                            + "   AND status  = 'PAGAMENTO_APROVADO'               " )
   void updateStatusVendaRecebFechamentoAutomatico( String anoMes );		

   /******************************************************************/
   /*                                                                */
   /*                                                                */
   /******************************************************************/	
   @Modifying(flushAutomatically = true)
   @Query(nativeQuery = true, value = " update venda                                          "
   		                            + "   set                                                 "
   		                            + "    STATUS_VENDA_RECEBIDA = 'AGURARDANDO_RECEBIMENTO'  "
   		                            + "  , STATUS_VENDA_PAGA     = 'AGUARDANDO_PAGAMENTO'     "
   		                            + "  , STATUS                = 'PAGAMENTO_APROVADO'       "
   		                            + " where ano_mes               = ?1              "
   		                            + "   and STATUS_VENDA_RECEBIDA = 'AGUARDANDO_FECHAMENTO' "
   		                            + "   and STATUS_VENDA_PAGA     = 'AGUARDANDO_FECHAMENTO' "
   		                            + "   and STATUS                = 'AGUARDANDO_FECHAMENTO' " )
   void updateStatusVendaReprocessamentoFechamento( String anoMes );		

   
   /******************************************************************/
   /*                                                                */
   /*                                                                */
   /******************************************************************/	
   @Query(nativeQuery = true, value = " select count(1) from venda                      "
   		                            + "  where ano_mes = :anoMes                        "
   		                            + "    and STATUS_VENDA_RECEBIDA = 'VENDA_RECEBIDA' "
   		                            + "     or STATUS_VENDA_PAGA     = 'VENDA_PAGA'     " )
   void isStatusVendaReprocessamentoFechamento( @Param("anoMes") String anoMes );	
   
   
   
   /******************************************************************/
   /*                                                                */
   /*                                                                */
   /******************************************************************/	
   @Query(nativeQuery = true, value = " SELECT EXISTS (  SELECT 1 FROM public.venda  WHERE ano_mes = ?1                                        "
   		                            + "                     AND STATUS                    IN ( 'PAGAMENTO_APROVADO', 'AGUARDANDO_FECHAMENTO', 'AGUARDANDO_FECHAMENTO_MANUAL' ) "
   		                            + "                     AND STATUS_VENDA_RECEBIDA NOT IN ( 'VENDA_RECEBIDA'    , 'FECHAMENTO_CONCLUIDO'  ) "
   		                            + "                     AND STATUS_VENDA_PAGA     NOT IN ( 'VENDA_PAGA'        , 'FECHAMENTO_CONCLUIDO'  ) "
   		                            + "               )                                                                                        " )
   Boolean isStatusVendaFechamento( String anoMes );		

   /******************************************************************/
   /*                                                                */
   /*                                                                */
   /******************************************************************/	
   @Modifying(flushAutomatically = true)
   @Query(nativeQuery = true, value = "UPDATE                                         "
   		                            + " venda SET                                     "
   		                            + "   status_venda_paga = 'AGUARDANDO_FECHAMENTO' "
   		                            + " WHERE ano_mes = ?1                    "
   		                            + "   AND status  = 'PAGAMENTO_APROVADO'          " )
   void updateStatusVendaPgFechamentoAutomatico( String anoMes );		

   /******************************************************************/
   /*                                                                */
   /*                                                                */
   /******************************************************************/	
   @Modifying(flushAutomatically = true, clearAutomatically = true)
   @Query(nativeQuery = true, value = "UPDATE                                                               "
                                    + " venda SET                                                           "
                                    + "   status_limite_credito_restabelecido = 'LIMITE_RESTABELECIDO'      "
                                    + " WHERE ano_mes = ?1                                                  "
                                    + "   AND status  = 'PAGAMENTO_APROVADO'                                "
                                    + "   AND status_limite_credito_restabelecido <> 'LIMITE_RESTABELECIDO' " )
   int updateStatusLimiteRestabelecido( String anoMes );
 
   /******************************************************************/
   /*                                                                */
   /*                                                                */
   /******************************************************************/	
   @Modifying(flushAutomatically = true)
   @Query(nativeQuery = true, value = "UPDATE                                "
   		                            + " venda SET                            "
   		                            + "   status = 'AGUARDANDO_FECHAMENTO'   "
   		                            + " WHERE ano_mes = ?1                   "
   		                            + "   AND status  = 'PAGAMENTO_APROVADO' " )
   void updateStatusVendasFechamentoAutomatico( String anoMes );		

   /******************************************************************/
   /*                                                                */
   /*                                                                */
   /******************************************************************/	
   @Modifying(flushAutomatically = true)
   @Query(nativeQuery = true, value = "UPDATE                                     "
   		                            + " venda SET                                 "
   		                            + "   status = 'AGUARDANDO_FECHAMENTO_MANUAL' "
   		                            + " WHERE ano_mes = ?1                        "
   		                            + "   AND status  = 'PAGAMENTO_APROVADO'      " )
   void updateStatusVendasFechamentoManual( String anoMes );		

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/
   @Transactional(readOnly = true)
   @Query(value = "SELECT                                                                 "
                + "    ven.ano_mes AS anoMes                                            , "
                + "    SUM(ven.valor_venda) AS somatorioValorVenda                      , "
                + "    SUM(ven.valor_calc_taxa_conveniado) AS somatorioVlrCalcTxConv    , "
                + "    ven.id_conveniados                  AS idConveniados             , "
                + "    ven.id_taxa_conveiniados            AS idTaxaConveniados         , "
                + "    ven.id_taxa_conveniados_entidate    AS idTaxaConveniadosEntidate   "
                + " FROM public.venda ven                                                 "
                + " WHERE ven.ano_mes           = :anoMes                                 "
                + "   AND ven.status            = 'PAGAMENTO_APROVADO'                    "
                + "   AND ven.status_venda_paga = 'AGUARDANDO_PAGAMENTO'                  " 
                + " GROUP BY                                                              "
                + "    ven.ano_mes,                                                       "
                + "    ven.id_conveniados,                                                "
                + "    ven.id_taxa_conveiniados,                                          "
                + "    ven.id_taxa_conveniados_entidate                                   ", 
        nativeQuery = true)
    List<DadosFechamentoPagamentoCicloProjection> listaFechamentoVendaPorMesAutomatica(@Param("anoMes") String anoMes);  
   
    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @Transactional(readOnly = true)
	@Query(value = " select                                                         "
			     + "     ven.ano_mes                                                "
			     + "   , sum(ven.valor_venda) as somatorioValorVenda                "
			     + "   , sum(ven.valor_calc_taxa_entidade) as somatorioVlrCalcTxEnt "
			     + "   , fun.id_entidade                                            "
			     + "   , ven.id_taxa_entidade                                       "
			     + "  FROM                                                          "
			     + "      venda       ven                                           "
			     + "	, cartao      car                                           "
			     + "	, funcionario fun                                           "
			     + " WHERE ven.ano_mes               = ?1                           "
			     + "   AND ven.status                = 'PAGAMENTO_APROVADO'         "
			     + "   AND ven.status_venda_recebida = 'AGURARDANDO_RECEBIMENTO'    "
			     + "   AND car.id_cartao             = ven.id_cartao                "
			     + "   AND fun.id_funcionario        = car.id_funcionario           "
			     + " GROUP BY                                                       "
			     + "     ven.ano_mes                                                "
			     + "   , fun.id_entidade                                            "
			     + "   , ven.id_taxa_entidade                                       " , nativeQuery = true )
    List<DadosFechamentoRecebimentoCicloDTO> listaFechamentoRecebimentoPorMesAutomatica( String anoMes ) ;
    
    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @Transactional(readOnly = true)
    @Query(value = " select                                                         "
                 + "     ven.ano_mes AS anoMes                                      "
                 + "   , sum(ven.valor_venda) AS somatorioValorVenda                "
                 + "   , sum(ven.valor_calc_taxa_entidade) AS somatorioVlrCalcTxEnt "
                 + "   , fun.id_entidade AS idEntidade                              "
                 + "   , ven.id_taxa_entidade AS idTaxaEntidade                     "
                 + "  FROM                                                          "
                 + "      venda       ven                                           "
                 + "    , cartao      car                                           "
                 + "    , funcionario fun                                           "
                 + " WHERE ven.ano_mes               = ?1                   "
                 + "   AND ven.status                = 'PAGAMENTO_APROVADO'         "
                 + "   AND ven.status_venda_recebida = 'AGURARDANDO_RECEBIMENTO'    "
                 + "   AND car.id_cartao             = ven.id_cartao                "
                 + "   AND fun.id_funcionario        = car.id_funcionario           "
                 + " GROUP BY                                                       "
                 + "     ven.ano_mes                                                "
                 + "   , fun.id_entidade                                            "
                 + "   , ven.id_taxa_entidade                                       ", 
         nativeQuery = true)
    List<DadosFechamentoRecebimentoCicloProjection> listaFechamentoRecebimentoPorMes(String anoMes); 
    
    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @Modifying(flushAutomatically = true)
    @Query("UPDATE Venda v SET v.descStatusVendaPg = :status,  v.dtAlteracao = CURRENT_TIMESTAMP WHERE v.idVenda IN :ids")
    int atualizarStatusVendaPgEmMassa(@Param("ids") List<Long> ids, @Param("status") StatusVendaPg status);
    
    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @Modifying(flushAutomatically = true)
    @Query("UPDATE Venda v SET v.descStatusVendaReceb = :status,  v.dtAlteracao = CURRENT_TIMESTAMP WHERE v.idVenda IN :ids")
    int atualizarStatusVendaRecebEmMassa(@Param("ids") List<Long> ids, @Param("status") StatusVendaReceb status);
    
}

