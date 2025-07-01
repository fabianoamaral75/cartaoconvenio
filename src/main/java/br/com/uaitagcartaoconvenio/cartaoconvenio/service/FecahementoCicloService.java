package br.com.uaitagcartaoconvenio.cartaoconvenio.service;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.FechamentoConvItensVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.FechamentoEntContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ItemTaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ItemTaxaExtraEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Venda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContatoWorkflowDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.DadosFechamentoPagamentoCicloDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.DadosFechamentoRecebimentoCicloDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RestabelecerLimitCreditoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.WorkflowInformativoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection.DadosFechamentoPagamentoCicloProjection;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection.DadosFechamentoRecebimentoCicloProjection;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ConveniadosRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.EntidadeRespository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaConveniadosRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.VendaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.BusinessException;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.EmailFechamentoException;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.EmailService;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.PreparaInfoEmail;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class FecahementoCicloService {

	@Autowired
	private CicloPagamentoVendaService cicloPagamentoVendaService;
	
	@Autowired
	private VendaRepository vendaRepository;
	
	@Autowired
	private ConveniadosRepository conveniadosRepository;
	
	@Autowired
	private TaxaConveniadosRepository taxaConveniadosRepository;
	
	@Autowired
	private EntidadeRespository entidadeRespository;
	
	@Autowired
	private EntidadeService entidadeService;
		
	@Autowired
	private ContasReceberService contasReceberService;
	
	@Autowired
	private LimitecreditoService limitecreditoService;

	@Autowired
	private final EmailService emailService;
	
	@Autowired
	private final PreparaInfoEmail preparaInfoEmail;
	
	@Autowired
	private final WorkflowService workflowService;
		
	@Autowired
	private TaxaExtraConveniadaService taxaExtraConveniadaService;
	
	@Autowired
	private TaxaExtraEntidadeService taxaExtraEntidadeService;
	
	@Autowired
	private ConveniadosService conveniadosService;
	
	
	private static final Logger logger = LogManager.getLogger(FecahementoCicloService.class);
	
	
	public String enviaEmailFechamentoCiclo(List<CicloPagamentoVenda> lCPV, List<ContasReceber> lCRV, String periodo, String tipoExecução )  {
		
		List<String> emails = new ArrayList<String>();
		
		WorkflowInformativoDTO wi  = workflowService.buscarPorId( 1L ); 
		
		for(ContatoWorkflowDTO ck : wi.getContatoWorkflow()) {
			String email = ck.getNomeContato() + "<" + ck.getEmail() + ">";
			emails.add(email);
		}
		
		Map<String, String> datas = FuncoesUteis.getFirstAndLastDayOfMonthSafe(periodo);
		
		String dataInicio = datas.get("primeiroDia");
		String dataFim    = datas.get("ultimoDia");
		List<Map<String, String>> mapCPV = preparaInfoEmail.convertCPVListToMapList(lCPV);
		List<Map<String, String>> mapCRV = preparaInfoEmail.convertCRListToMapList(lCRV);
		
		
		try {
			emailService.enviarEmailFechamento(tipoExecução, emails, periodo, dataInicio, dataFim, mapCPV, mapCRV);
		} catch (EmailFechamentoException e) {
		    logger.error("Falha no envio de e-mail de fechamento. Tipo: {}, Detalhes: {}",
		               e.getTipoErro(), e.getDetalhes(), e);
		    
		    // Ações específicas por tipo de erro
		    if ("SMTP".equals(e.getTipoErro())) {
		        // Tentar reenviar ou notificar admin
		    } else if ("TEMPLATE".equals(e.getTipoErro())) {
		        // Alertar equipe de desenvolvimento
		    }
		    
		    // Opcional: lançar outra exceção ou tratar de outra forma
		 //   throw new BusinessException("Não foi possível enviar o e-mail de fechamento", e);
	    	 throw new BusinessException( "Não foi possível enviar o e-mail de fechamento",e.getMessage());

		}		
		return "ENVIO_EMAIL_OK";
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public String fechamentoCiclo( String anoMes, Boolean execManual )  {
		
	    // Validação de parâmetros
	    if (anoMes == null) {
	        throw new IllegalArgumentException("O parâmetro anoMes não pode ser nulo");
	    }
	    if (execManual == null) {
	        throw new IllegalArgumentException("O parâmetro execManual não pode ser nulo");
	    }
	    
	    String tipoExec = execManual ? "Execução Manual" : "Execução Automática";
	    
	    // Verifica se exite venda a ser processada no fechamento Automatico.
	    Boolean existeFechamento = vendaRepository.isStatusVendaFechamento(anoMes);
	    if (existeFechamento == null || !existeFechamento) {
	        return "NÃO EXISTE CICLO PARA SER PROCESSADO PARA O PERÍODO: " + anoMes;
	    }
	    
		// verifica se já existe um Ciclo de fechamento para o pagamento e recebimento.
		validaFechamentoCiclo( anoMes );
		
		String msnFechamento = "FECHAMENTO_AUTOMATICO_OK";
		// Gera as informaçoes do ciclo de pagamento para as conveniadas
		List<CicloPagamentoVenda> listaCicloPagamentoVenda = new ArrayList<CicloPagamentoVenda>();
		// Volta o limite de creditos dos funcionarios.
		List<RestabelecerLimitCreditoDTO> listaRestabelecerLimitCredito = new ArrayList<RestabelecerLimitCreditoDTO>();
		// Gera as informaçoes do ciclo de controle de recebimento das Encitidades.
		List<ContasReceber> listaCicloReceberVenda = new ArrayList<ContasReceber>();

		try {
			listaCicloPagamentoVenda      = this.fechamentoConveniado( msnFechamento, anoMes ); 
			listaRestabelecerLimitCredito = this.restabelecerLimiteCreditoFuncionarios( msnFechamento, anoMes );
	        listaCicloReceberVenda        = this.fechamentoEntidade( msnFechamento, anoMes, execManual );
			/* Envia E-mail fim de Ciclo. */
			enviaEmailFechamentoCiclo(listaCicloPagamentoVenda, listaCicloReceberVenda, anoMes, tipoExec );

		} catch (BusinessException e) {
		    logger.error("Erro de negócio durante fechamento: {}", e.getMessage(), e);
		    msnFechamento = e.getMessage();
		} catch (NullPointerException e) {
		    logger.error("NullPointerException durante fechamento", e);
		    msnFechamento = "Erro interno: " + e.getMessage();
		} catch (Exception e) {
		    logger.error("Erro inesperado durante fechamento", e);
		    msnFechamento = "Erro inesperado: " + e.getMessage();
		} finally {
			 // Realiza Rollback em caso de erro em qualquer uma das etapas.
			logger.info("Rollback: " + msnFechamento);
	   		 if( listaCicloPagamentoVenda.size()      > 0 ) cicloPagamentoVendaService.deletarListaCiclos(listaCicloPagamentoVenda);
	   		 if( listaRestabelecerLimitCredito.size() > 0 ) restabelecerLimiteCreditoFuncionariosRollback( listaRestabelecerLimitCredito );
   		     if( listaCicloReceberVenda.size()        > 0 ) contasReceberService.deletarListaCiclos(listaCicloReceberVenda);
   		     // verifica se já existe um Ciclo de fechamento para o pagamento e recebimento.
   		     validaFechamentoCiclo( anoMes );
		}
 		return msnFechamento;		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public void validaFechamentoCiclo( String anoMesAnterior ) {
		
		if( cicloPagamentoVendaService.existCicloFechamentoPagamento(anoMesAnterior) ) cicloPagamentoVendaService.updateCancelamentoStatusCicloPagamentoVenda( anoMesAnterior );
		if( contasReceberService.existCicloFechamentoRecebimento(anoMesAnterior)     ) contasReceberService.updateCancelamentoStatusCicloRecebimentoVenda    ( anoMesAnterior );
		vendaRepository.updateStatusVendaReprocessamentoFechamento( anoMesAnterior );
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	private List<RestabelecerLimitCreditoDTO> restabelecerLimiteCreditoFuncionarios( String msn, String anoMes ) {
		
		// String anoMesAnterior = FuncoesUteis.getPreviousMonthFormatted();
		List<RestabelecerLimitCreditoDTO> listaRestabelecerLimitCredito = limitecreditoService.listaRestabelecerLimiteCredito(anoMes);
		
		for(RestabelecerLimitCreditoDTO lrlc : listaRestabelecerLimitCredito) {
			limitecreditoService.updateRestabelecerLimiteCredito( lrlc.getIdFuncionario(), lrlc.getValorRestituir());
		}
		// Atualizar o status do limite de credito da tabela vendas para o status de aplicado e restabelecido.
		vendaRepository.updateStatusLimiteRestabelecido( anoMes );
		
		return listaRestabelecerLimitCredito;		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	private List<RestabelecerLimitCreditoDTO> restabelecerLimiteCreditoFuncionariosRollback( List<RestabelecerLimitCreditoDTO> listaRestabelecerLimitCredito ) {

		for(RestabelecerLimitCreditoDTO lrlc : listaRestabelecerLimitCredito) {
			limitecreditoService.updateRestabelecerLimiteCredito( lrlc.getIdFuncionario(), lrlc.getValorRestituir());
		}
		return null;
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	private List<CicloPagamentoVenda> fechamentoConveniado( String msn, String anoMes ) {
		
		msn = null;
		String mensagemErro = "Erro desconhecido ao processar fechamento";
		List<CicloPagamentoVenda> listaCicloPagamentoVenda = new ArrayList<CicloPagamentoVenda>();

		try {
			
			// Busca todas as vendas para o periodo de fechamento.
			List<DadosFechamentoPagamentoCicloDTO> listaVendasFechamento = buscarFechamentosPorMes( anoMes );			
			List<Long>                             listaIdsConveniados   = new ArrayList<Long>();

			// Trata as vendas por Conveniadas
			for( DadosFechamentoPagamentoCicloDTO lv: listaVendasFechamento ) {

				CicloPagamentoVenda cPgVenda = new CicloPagamentoVenda();
				int diasParaPagamento        = conveniadosRepository.getDiasPagamento( lv.getIdConveniados() );
				Date dtPagamento             = FuncoesUteis.getDateNextMonth( diasParaPagamento );
				Date dataAtual               = Calendar.getInstance().getTime();

				cPgVenda.setAnoMes             ( anoMes                                                             );
				cPgVenda.setDescStatusPagamento( StatusCicloPgVenda.AGUARDANDO_UPLOAD_NF                            );
				cPgVenda.setDtAlteracao        ( dataAtual                                                          );
				cPgVenda.setDtCriacao          ( dataAtual                                                          );
				cPgVenda.setDtPagamento        ( dtPagamento                                                        );
				cPgVenda.setVlrCicloBruto      ( lv.getSomatorioValorVenda()                                        ); // Valor Bruto total do ciclo para a uma Conviniadas.
				cPgVenda.setVlrTaxaSecundaria  ( lv.getSomatorioVlrCalcTxConv()                                     ); // Valor calculado do valor da taxa secundaria.
				cPgVenda.setConveniados        ( conveniadosRepository.findById(lv.getIdConveniados()).orElse(null) );
				
				Long idTaxa = lv.getIdTaxaConveniados();
				if (idTaxa == null) cPgVenda.setIdTaxaConveniadosEntidate( lv.getIdTaxaConveniadosEntidate() );
				else cPgVenda.setTaxaConveniados( taxaConveniadosRepository.findById(lv.getIdTaxaConveniados()).orElse(null)   );

				List<Venda>                     listaVenda = vendaRepository.listaVendaByIdConveniadosStatusAnoMes(anoMes, StatusVendas.PAGAMENTO_APROVADO, lv.getIdConveniados() );
				List<FechamentoConvItensVendas> listaFciv  = new ArrayList<FechamentoConvItensVendas>();
				
				for(Venda venda : listaVenda) {
					FechamentoConvItensVendas fciv = new FechamentoConvItensVendas();
					fciv.setVenda              ( venda    );
					fciv.setCicloPagamentoVenda( cPgVenda );
					listaFciv.add( fciv );
				}

				List<TaxaExtraConveniada>     taxaExtraConveniada          = taxaExtraConveniadaService.findAllTaxaByConveniadoId( lv.getIdConveniados() );
				List<ItemTaxaExtraConveniada> listaItemTaxaExtraConveniada = new ArrayList<ItemTaxaExtraConveniada>();
				BigDecimal                    vlrTotalizado                = new BigDecimal(0);
				
				for( TaxaExtraConveniada tec : taxaExtraConveniada ) {

					 Set<String> tiposPermitidos = Set.of("P", "U", "M", "A", "D", "F");
					 
					 if (tiposPermitidos.contains( tec.getPeriodoCobrancaTaxa().getTipoPeriodo().getTipo() ) ) {
						 // Verifica se é cobrança unica e se já foi realizado a cobrança.
						 if (tec.getPeriodoCobrancaTaxa().getTipoPeriodo().getTipo().equals("U") 
								    && tec.getPeriodoCobrancaTaxa().getQtyCobranca() != null 
								    && tec.getPeriodoCobrancaTaxa().getQtyCobranca() > 0) {
								    continue;
						 }
						 
						 if( tec.getPeriodoCobrancaTaxa().getTipoPeriodo().getTipo().equals("A") ) {
							 
							 if( !FuncoesUteis.verificarDataECobranca(tec.getPeriodoCobrancaTaxa().getDataInicio()  , // Data Ínicio de cobrança da Taxa Extra
									                                  tec.getPeriodoCobrancaTaxa().getDataFim()     , // Data Fim de cobrança da Taxa Extra
									                                  LocalDate.now()                               , // Data do faturamento
									                                  tec.getPeriodoCobrancaTaxa().getQtyCobranca())  // Quantidade de vezes que houve o faturamento.
							 ) continue;
							 
						 }
						 
						 Set<String> tiposmensais = Set.of("P", "M");
						 
						 if (tiposmensais.contains( tec.getPeriodoCobrancaTaxa().getTipoPeriodo().getTipo() ) ) {
							 if( !FuncoesUteis.verificarDataCobrancaMensal(tec.getPeriodoCobrancaTaxa().getDataInicio()  , // Data Ínicio de cobrança da Taxa Extra
	                                                                       tec.getPeriodoCobrancaTaxa().getDataFim()     , // Data Fim de cobrança da Taxa Extra
	                                                                       LocalDate.now()                                 // Data do faturamento  
	                                                                      )                        
	                           ) continue;
						 }
						 
					 }
					
					 ItemTaxaExtraConveniada itemTaxaExtraConveniada = new ItemTaxaExtraConveniada();
					
					 //Somariza o valor da taxa extra.
					 vlrTotalizado.add( tec.getValorTaxa() ).setScale(2, RoundingMode.HALF_UP);
					 // Atualiza a quantidade de cobrança realizada.
					 tec.getPeriodoCobrancaTaxa().setQtyCobranca(
							    (tec.getPeriodoCobrancaTaxa().getQtyCobranca() != null ? 
							     tec.getPeriodoCobrancaTaxa().getQtyCobranca() : 0) + 1
							);
					 tec.getPeriodoCobrancaTaxa().setDtUltimaCobranca ( LocalDate.now()                  ); 
					 tec.getPeriodoCobrancaTaxa().setDtProximaCobranca(LocalDate.now().withDayOfMonth(1) );
					 
					 itemTaxaExtraConveniada.setTaxaExtraConveniada( tec                );
					 itemTaxaExtraConveniada.setCicloPagamentoVenda( cPgVenda           );
					 itemTaxaExtraConveniada.setValorTaxa          ( tec.getValorTaxa() );
					 listaItemTaxaExtraConveniada.add(itemTaxaExtraConveniada);

				}

				listaIdsConveniados.add(lv.getIdConveniados());
				cPgVenda.setItemTaxaExtraConveniada(listaItemTaxaExtraConveniada);
				cPgVenda.setFechamentoConvItensVendas(listaFciv);
				listaCicloPagamentoVenda.add(cPgVenda);

			}
			
			// Grava na base as informaçoes de conta a Pagar.
			if(listaCicloPagamentoVenda.size() > 30000) listaCicloPagamentoVenda = cicloPagamentoVendaService.salvarListaGrande(listaCicloPagamentoVenda, msn);
			else  listaCicloPagamentoVenda = cicloPagamentoVendaService.salvarListaCiclos(listaCicloPagamentoVenda, msn );
			
			if( listaCicloPagamentoVenda != null ) {
			    // Atualiza Status referente ao fechamento dos pagamento (Fechamento) das empresas conveniadas.
			    vendaRepository.updateStatusVendaPgFechamentoAutomatico( anoMes );
			    
			    // Atualizata a tabela de Conveniados com a informação da última data de faturamento para a Conveniadas.
			    conveniadosService.atualizarAnoMesRecebimentoPosFechamentoEmLote(listaIdsConveniados, anoMes);
			}else {
			    msn = "Error: erro na gerração do cilco de contadas a pagar!\n" + msn;
			    return null;
			}			

		} catch (Exception e) {
			msn = e.getMessage();
			System.err.println(e.getMessage());
			
	        // Garante que a mensagem nunca será null
	        mensagemErro = e.getMessage() != null ? e.getMessage() : "Erro sem mensagem específica";
	        logger.error("Erro no fechamentoConveniado: {}", mensagemErro, e);
	        
	        deletarCiclosComValidacao(listaCicloPagamentoVenda);
	        // Lança exceção com mensagem garantida
	        throw new BusinessException("Erro ao processar o Fechamento Conveniado", mensagemErro);

			
	//		throw new BusinessException( "Error ao processar o Fechamento Conveniado", msn);
		}
		
		return listaCicloPagamentoVenda;
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	private void deletarCiclosComValidacao(List<CicloPagamentoVenda> ciclos) {
		
	    if (ciclos == null || ciclos.isEmpty()) {
	        return; // ou lançar exceção apropriada
	    }
	    
	    // Verifica todos os IDs, não apenas o primeiro
	    if (ciclos.stream().anyMatch(c -> c == null || c.getIdCicloPagamentoVenda() == null)) {
	        throw new IllegalArgumentException("Lista contém elementos ou IDs nulos");
	    }
	    
	    cicloPagamentoVendaService.deletarListaCiclos(ciclos);
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	private List<ContasReceber> fechamentoEntidade( String msn, String anoMes, Boolean execManual ){
		
		msn = null;
		Long idEntidade = 0L;
		List<ContasReceber> listaContasReceberVenda =  new ArrayList<ContasReceber>();
		
		try {
			
			List<DadosFechamentoRecebimentoCicloDTO> listaFechamentoRecebimentoCiclo = buscarFechamentoRecebimentoCiclo( anoMes );
			List<Long> listaIdsEntidades = new ArrayList<Long>();
			
			for( DadosFechamentoRecebimentoCicloDTO lrv: listaFechamentoRecebimentoCiclo ) {
				 
				 ContasReceber contasReceber = new ContasReceber();
				 int diasParaRecebimento     = entidadeRespository.diasRecebimento( lrv.getIdEntidade() );
				 Date dtPagamento            = FuncoesUteis.getDateNextMonth( diasParaRecebimento );

				 contasReceber.setAnoMes                          ( anoMes                             );
				 contasReceber.setDescStatusReceber               ( StatusReceber.AGUARDANDO_UPLOAD_NF );
				 contasReceber.setValorReceber                    ( lrv.getSomatorioValorVenda()       );
				 contasReceber.setValorCalcTaxaEntidadeCiclo      ( lrv.getSomatorioVlrCalcTxEnt()     );
				 contasReceber.setDtPrevisaoRecebimento           ( dtPagamento                        );
				 contasReceber.getEntidade().setIdEntidade        ( lrv.getIdEntidade()                );
				 contasReceber.getTaxaEntidade().setIdTaxaEntidade( lrv.getIdTaxaEntidade()            );

				 List<Venda>                      listaVenda = vendaRepository.listaVendaByIdEntidadeStatusAnoMes( anoMes, "PAGAMENTO_APROVADO", lrv.getIdEntidade() );
				 List<FechamentoEntContasReceber> listaFecr  =  new ArrayList<FechamentoEntContasReceber>();
				 
				 for( Venda venda :listaVenda ) {
					 FechamentoEntContasReceber fecr = new FechamentoEntContasReceber();
					 fecr.setContasReceber( contasReceber );
					 fecr.setVenda        ( venda         );
					 listaFecr.add(fecr);
				 }
				 
				 idEntidade                                             = lrv.getIdEntidade();
				 List<TaxaExtraEntidade>     taxaExtra                  = taxaExtraEntidadeService.findAllByEntidadeId( idEntidade );
				 List<ItemTaxaExtraEntidade> listaItemTaxaExtraEntidade = new ArrayList<ItemTaxaExtraEntidade>();
				 BigDecimal                  vlrTotalizado              = new BigDecimal(0);  
				 listaIdsEntidades.add(idEntidade);

				 for(TaxaExtraEntidade tee : taxaExtra) {
					 Set<String> tiposPermitidos = Set.of("P", "U", "M", "A", "D", "F");
					 
					 if (tiposPermitidos.contains( tee.getPeriodoCobrancaTaxa().getTipoPeriodo().getTipo() ) ) {
						 // Verifica se é cobrança unica e se já foi realizado a cobrança.
						 if (tee.getPeriodoCobrancaTaxa().getTipoPeriodo().getTipo().equals("U") 
								    && tee.getPeriodoCobrancaTaxa().getQtyCobranca() != null 
								    && tee.getPeriodoCobrancaTaxa().getQtyCobranca() > 0) {
								    continue;
						 }
						 
						 if( tee.getPeriodoCobrancaTaxa().getTipoPeriodo().getTipo().equals("A") ) {
							 
							 if( !FuncoesUteis.verificarDataECobranca(tee.getPeriodoCobrancaTaxa().getDataInicio()  , // Data Ínicio de cobrança da Taxa Extra
									                                  tee.getPeriodoCobrancaTaxa().getDataFim()     , // Data Fim de cobrança da Taxa Extra
									                                  LocalDate.now()                               , // Data do faturamento
									                                  tee.getPeriodoCobrancaTaxa().getQtyCobranca())  // Quantidade de vezes que houve o faturamento.
							 ) continue;
							 
						 }
						 
						 Set<String> tiposmensais = Set.of("P", "M");
						 
						 if (tiposmensais.contains( tee.getPeriodoCobrancaTaxa().getTipoPeriodo().getTipo() ) ) {
							 if( !FuncoesUteis.verificarDataCobrancaMensal(tee.getPeriodoCobrancaTaxa().getDataInicio()  , // Data Ínicio de cobrança da Taxa Extra
									                                       tee.getPeriodoCobrancaTaxa().getDataFim()     , // Data Fim de cobrança da Taxa Extra
	                                                                       LocalDate.now()                                 // Data do faturamento  
	                                                                      )                        
	                           ) continue;
						 }
						 
						 ItemTaxaExtraEntidade itemTaxaExtraEntidade = new ItemTaxaExtraEntidade();
						//Somariza o valor da taxa extra.
						 vlrTotalizado.add( tee.getValor() ).setScale(2, RoundingMode.HALF_UP);
						 // Atualiza a quantidade de cobrança realizada.
						 tee.getPeriodoCobrancaTaxa().setQtyCobranca(
								    (tee.getPeriodoCobrancaTaxa().getQtyCobranca() != null ? 
								     tee.getPeriodoCobrancaTaxa().getQtyCobranca() : 0) + 1
								);
						 tee.getPeriodoCobrancaTaxa().setDtUltimaCobranca ( LocalDate.now()                  ); 
						 tee.getPeriodoCobrancaTaxa().setDtProximaCobranca(LocalDate.now().withDayOfMonth(1) );
						 // 
						 itemTaxaExtraEntidade.setTaxaExtraEntidade( tee            );
						 itemTaxaExtraEntidade.setContasReceber    ( contasReceber  );
						 itemTaxaExtraEntidade.setValorTaxa        ( tee.getValor() );
						 
						 listaItemTaxaExtraEntidade.add( itemTaxaExtraEntidade );

					 }

				 }

				 contasReceber.setItensTaxasExtras          ( listaItemTaxaExtraEntidade );
				 contasReceber.setFechamentoEntContasReceber( listaFecr                  );				 
				 listaContasReceberVenda.add                ( contasReceber              );
				 
			}
			
			// Grava na base as informaçoes de conta a Receber.
			if(listaContasReceberVenda.size() > 30000) listaContasReceberVenda = contasReceberService.salvarListaGrande(listaContasReceberVenda, msn);
			else  listaContasReceberVenda = contasReceberService.salvarListaCiclos(listaContasReceberVenda, msn);
			
		    if( listaContasReceberVenda != null ) {
				// Atualiza Status referente ao fechamento dos recebimentod (Fechamento) das Entidades.
				vendaRepository.updateStatusVendaRecebFechamentoAutomatico( anoMes );
				if( execManual )vendaRepository.updateStatusVendasFechamentoManual( anoMes ); 
				else vendaRepository.updateStatusVendasFechamentoAutomatico( anoMes );
				// Atualizata a tabela de Entidade com a informação da última data de faturamento para a Entidade.
				entidadeService.atualizarAnoMesRecebimentoPosFechamentoEmLote(listaIdsEntidades, anoMes);
			    		     	
		    }else {
		    	msn = "Error: erro na gerração do cilco de contadas a pagar!\n" + msn;	
		    	return null;
		    }
		
		} catch (Exception e) {
			msn = e.getMessage();
			System.err.println(e.getMessage());
	    	throw new BusinessException(
	    			    "Não foi possível processar o Fechamento do Ciclo para as Entidades!",
	    			    "Falha ao gerar Fechamento do Ciclo a Receber!")
	    			    .addDetail("ID Entitidade", idEntidade)
	    			    .addDetail("Periódo", anoMes);
			
		}
		
		return listaContasReceberVenda;
		
	}	
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    public List<DadosFechamentoPagamentoCicloDTO> buscarFechamentosPorMes(String anoMes) {
    	
        List<DadosFechamentoPagamentoCicloProjection> projections = 
        		vendaRepository.listaFechamentoVendaPorMesAutomatica( anoMes );
        
        if (projections == null || projections.isEmpty()) {
            return Collections.emptyList();
        }

        return projections.stream()
            .map(proj -> new DadosFechamentoPagamentoCicloDTO(  proj.getAnoMes(),
												                proj.getSomatorioValorVenda(),
												                proj.getSomatorioVlrCalcTxConv(),
												                proj.getIdConveniados(),
												                proj.getIdTaxaConveniados(),
												                proj.getidTaxaConveniadosEntidate()
												              )
            	)
            .collect(Collectors.toList());
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    public List<DadosFechamentoRecebimentoCicloDTO> buscarFechamentoRecebimentoCiclo(String anoMes) {
    	
        List<DadosFechamentoRecebimentoCicloProjection> projections = 
                vendaRepository.listaFechamentoRecebimentoPorMes(anoMes);
        
        if (projections == null || projections.isEmpty()) {
            return Collections.emptyList();
        }
        
        return projections.stream()
            .map(proj -> new DadosFechamentoRecebimentoCicloDTO(
                        proj.getAnoMes(),
                        proj.getSomatorioValorVenda(),
                        proj.getSomatorioVlrCalcTxEnt(),
                        proj.getIdEntidade(),
                        proj.getIdTaxaEntidade()))
            .collect(Collectors.toList());
    }
}

