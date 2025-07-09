package br.com.uaitagcartaoconvenio.cartaoconvenio.service;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaExtraConveniadaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.FechamentoConvItensVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.FechamentoEntContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ItemTaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PeriodoCobrancaTaxa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxasFaixaVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TipoPeriodo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Venda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContatoWorkflowDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.DadosFechamentoPagamentoCicloDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.DadosFechamentoRecebimentoCicloDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PeriodoCobrancaTaxaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RestabelecerLimitCreditoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaExtraConveniadaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.WorkflowInformativoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection.DadosFechamentoPagamentoCicloProjection;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection.DadosFechamentoRecebimentoCicloProjection;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ConveniadosRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.EntidadeRespository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaConveniadosRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TipoPeriodoRepository;
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
	private ConveniadosService conveniadosService;
	
	@Autowired
	private TaxasFaixaVendasService taxasFaixaVendasService;
	
	@Autowired
    private TaxaExtraConveniadaMapper mapper;
	
	@Autowired
    private TipoPeriodoRepository tipoPeriodoRepository;
	
	private static final Logger logger = LogManager.getLogger(FecahementoCicloService.class);
	
	
	private String enviaEmailFechamentoCiclo(List<CicloPagamentoVenda> lCPV, List<ContasReceber> lCRV, String periodo, String tipoExecução )  {
		
		List<String> emails = new ArrayList<String>();
		
		WorkflowInformativoDTO wi  = workflowService.buscarPorId( 1L ); 
		
		for(ContatoWorkflowDTO ck : wi.getContatoWorkflow()) {
			String email = ck.getNomeContato() + "<" + ck.getEmail() + ">";
			emails.add(email);
		}
		
		Map<String, String> datas = FuncoesUteis.getFirstAndLastDayOfMonthSafe(periodo);
		
		String dataInicio = datas.get("primeiroDia");
		String dataFim    = datas.get("ultimoDia");
		List<Map<String, String>> mapCPV = preparaInfoEmail.convertCPVListToMapList( lCPV );
		List<Map<String, String>> mapCRV = preparaInfoEmail.convertCRListToMapList ( lCRV );		
		
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
	    //	 throw new BusinessException( "Não foi possível enviar o e-mail de fechamento",e.getMessage());
	    	 
	    	 throw new IllegalArgumentException( "Não foi possível enviar o e-mail de fechamento: " + e.getMessage() );

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
	    Boolean rollback = false;
	    
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
	        /* Bloco separado para envio de email - não afeta o rollback */
	        try {
	            enviaEmailFechamentoCiclo(listaCicloPagamentoVenda, listaCicloReceberVenda, anoMes, tipoExec);
	        } catch (Exception emailEx) {
	            logger.error("Erro no envio de email (não crítico): {}", emailEx.getMessage(), emailEx);
	            // Adiciona informação ao retorno sem causar rollback
	            msnFechamento += " | AVISO: O fechamento foi concluído, mas ocorreu um erro no envio do email: " + emailEx.getMessage();
	        }
		} catch (BusinessException e) {
		    logger.error("Erro de negócio durante fechamento: {}", e.getMessage(), e);
		    msnFechamento = e.getMessage();
		    rollback = true;
		} catch (NullPointerException e) {
		    logger.error("NullPointerException durante fechamento", e);
		    msnFechamento = "Erro interno: " + e.getMessage();
		    rollback = true;
		} catch (Exception e) {
		    logger.error("Erro inesperado durante fechamento", e);
		    msnFechamento = "Erro inesperado: " + e.getMessage();
		    rollback = true;
		} finally {
			if( rollback ) {
				// Realiza Rollback em caso de erro em qualquer uma das etapas.
				logger.info("Rollback: " + msnFechamento);
		   		if( listaCicloPagamentoVenda.size()      > 0 ) cicloPagamentoVendaService.deletarListaCiclos(listaCicloPagamentoVenda);
		   		if( listaRestabelecerLimitCredito.size() > 0 ) restabelecerLimiteCreditoFuncionariosRollback( listaRestabelecerLimitCredito );
	   		    if( listaCicloReceberVenda.size()        > 0 ) contasReceberService.deletarListaCiclos(listaCicloReceberVenda);
	   		    // verifica se já existe um Ciclo de fechamento para o pagamento e recebimento.
	   		    validaFechamentoCiclo( anoMes );
			}
		}
 		return msnFechamento;		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	private void validaFechamentoCiclo( String anoMesAnterior ) {
		
		if( cicloPagamentoVendaService.existCicloFechamentoPagamento(anoMesAnterior) ) cicloPagamentoVendaService.updateCancelamentoStatusCicloPagamentoVenda( anoMesAnterior );
		if( contasReceberService.existCicloFechamentoRecebimento(anoMesAnterior)     ) contasReceberService.updateCancelamentoStatusCicloRecebimentoVenda    ( anoMesAnterior );
		vendaRepository.updateStatusVendaReprocessamentoFechamento( anoMesAnterior );
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	private List<RestabelecerLimitCreditoDTO> restabelecerLimiteCreditoFuncionarios( String msn, String anoMes ) {
		try {		
			// String anoMesAnterior = FuncoesUteis.getPreviousMonthFormatted();
			List<RestabelecerLimitCreditoDTO> listaRestabelecerLimitCredito = limitecreditoService.listaRestabelecerLimiteCredito(anoMes);
			
			for(RestabelecerLimitCreditoDTO lrlc : listaRestabelecerLimitCredito) {
				limitecreditoService.updateRestabelecerLimiteCredito( lrlc.getIdFuncionario(), lrlc.getValorRestituir());
			}
			// Atualizar o status do limite de credito da tabela vendas para o status de aplicado e restabelecido.
			vendaRepository.updateStatusLimiteRestabelecido( anoMes );
			
			return listaRestabelecerLimitCredito;
			
    	} catch (Exception e) {
		    // Garante que a mensagem nunca será null
			msn = e.getMessage() != null ? e.getMessage() : "Erro sem mensagem específica";
	        logger.error("Erro no fechamentoConveniado do ciclo para conveniada: {}", msn, e);
	        throw new IllegalArgumentException( msn );
	        
		}		
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
		// String mensagemErro = "Erro desconhecido ao processar fechamento";
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

				cPgVenda.setAnoMes             ( anoMes                                                               ); // Informação recebida como paramentro da função 
				cPgVenda.setDescStatusPagamento( StatusCicloPgVenda.AGUARDANDO_UPLOAD_NF                              ); // Informação passa Explicitamente ==> Enum StatusCicloPgVenda
				cPgVenda.setDtAlteracao        ( dataAtual                                                            ); // Data atual do sistema
				cPgVenda.setDtCriacao          ( dataAtual                                                            ); // Data atual do sistema
				cPgVenda.setDtPagamento        ( dtPagamento                                                          ); // informação calculda pela função FuncoesUteis.getDateNextMonth( diasParaPagamento ); apartir do dia extraido na base, informando o dia de pagamento para cada conveniada
				cPgVenda.setVlrCicloBruto      ( lv.getSomatorioValorVenda()                                          ); // Valor Bruto total do ciclo para a uma Conviniadas.
				cPgVenda.setVlrTaxaSecundaria  ( lv.getSomatorioVlrCalcTxConv()                                       ); // Valor calculado do valor da taxa secundaria.
				cPgVenda.setVlrLiquido         ( lv.getSomatorioValorVenda().subtract(lv.getSomatorioVlrCalcTxConv()) ); // Valor liquido após aplicar a taxa secundaria. Neste valor ainda será aplicado as taxas extras, caso exista.
				cPgVenda.setConveniados        ( conveniadosRepository.findById(lv.getIdConveniados()).orElse(null)   ); // Informação da conveniada em processamento para associar a tabela cabeçalho no banco de dados.
				
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
				
                // Trás a relação das Taxas Extra a serem aplicadas por Conveniadas.
				List<TaxaExtraConveniada>     taxaExtraConveniada          = taxaExtraConveniadaService.findAllTaxaByConveniadoId( lv.getIdConveniados() );
				List<ItemTaxaExtraConveniada> listaItemTaxaExtraConveniada = new ArrayList<ItemTaxaExtraConveniada>();
				BigDecimal                    vlrTxTotalizado              = BigDecimal.ZERO;
				BigDecimal                    vlrTxTotalizadoPercetual     = BigDecimal.ZERO;
			    BigDecimal                    vlrTaxasFaixaVendas          = BigDecimal.ZERO; 
								
				/* ***************************************************************************************************** */
				/*                        Tratamento das Taxas Extras por Conveniadas                                    */
				/* ***************************************************************************************************** */
				for( TaxaExtraConveniada tec : taxaExtraConveniada ) {
					                     
					 if( !tec.getStatusTaxa().equals("ATIVA") ) continue;
					
					 if( deveCobrarTaxaExtra(tec) ) continue;

					 if( tec.getTipoCobrancaPercentual() ) {
						 
						 if( tec.getCobrancaValorBruto() ) vlrTxTotalizadoPercetual = vlrTxTotalizadoPercetual.add( FuncoesUteis.calcularPorcentagem( cPgVenda.getVlrCicloBruto(), tec.getValorTaxa() ) ).setScale(2, RoundingMode.HALF_UP); // Valor calculado será em cima do valor bruto.
						 else                              vlrTxTotalizadoPercetual = vlrTxTotalizadoPercetual.add( FuncoesUteis.calcularPorcentagem( cPgVenda.getVlrLiquido()   , tec.getValorTaxa() ) ).setScale(2, RoundingMode.HALF_UP); // Valor calculado será em cima do valor líquido.
						 
					 }else { // Somariza o valor da taxa extra.
						 vlrTxTotalizado = vlrTxTotalizado.add( tec.getValorTaxa() ).setScale(2, RoundingMode.HALF_UP);					 
					 }

					 // Atualiza a quantidade de cobrança realizada.
					 tec.getPeriodoCobrancaTaxa().setQtyCobranca(
							    (tec.getPeriodoCobrancaTaxa().getQtyCobranca() != null ? 
							     tec.getPeriodoCobrancaTaxa().getQtyCobranca() : 0) + 1
							);
					 // Atualiza as datas de últimas cobranças e próxima cobrança.
					 tec.getPeriodoCobrancaTaxa().setDtUltimaCobranca ( LocalDate.now()                  ); 
					 tec.getPeriodoCobrancaTaxa().setDtProximaCobranca(LocalDate.now().withDayOfMonth(1) );
					 
					 // Cria o item para lincar com o cabeçalho da venda.
					 ItemTaxaExtraConveniada itemTaxaExtraConveniada = new ItemTaxaExtraConveniada();
					 itemTaxaExtraConveniada.setTaxaExtraConveniada   ( tec                             );
					 itemTaxaExtraConveniada.setCicloPagamentoVenda   ( cPgVenda                        );
					 itemTaxaExtraConveniada.setValorTaxa             ( tec.getValorTaxa()              );
					 itemTaxaExtraConveniada.setTipoCobrancaPercentual( tec.getTipoCobrancaPercentual() );
					 itemTaxaExtraConveniada.setCobrancaValorBruto    ( tec.getCobrancaValorBruto()     );
					 listaItemTaxaExtraConveniada.add(itemTaxaExtraConveniada);

				}
				
				// Valida as taxas de faixas de vendas.
				if(cPgVenda.getConveniados().getIsTaxasFaixaVendas()) {
					List<TaxasFaixaVendas> listaTaxasFaixaVendas = taxasFaixaVendasService.findAtivasOrderByFaixasAsc( );
				    TaxasFaixaVendas taxasFaixaVendas = encontrarTaxaAplicavel( listaTaxasFaixaVendas, cPgVenda.getVlrCicloBruto() );
				    if( taxasFaixaVendas != null ) {
					    vlrTaxasFaixaVendas = calcularValorTaxa( taxasFaixaVendas, cPgVenda.getVlrCicloBruto() );
					    // taxasFaixaVendas.setCiclosPagamento(listaCicloPagamentoVenda);
					    cPgVenda.setTaxasFaixaVendas(taxasFaixaVendas);					    
				    }
				}                
				
				// Valores somarizado referente a todas as Taxas Extras e faixa etarias por valores de vendas, por Conveniada
				cPgVenda.setVlrTaxaExtraValor     ( vlrTxTotalizado          );
				cPgVenda.setVlrTaxaExtraPercentual( vlrTxTotalizadoPercetual );
				cPgVenda.setVlrTaxasFaixaVendas   ( vlrTaxasFaixaVendas      );
				
				// Realiza o calculo do valor liquido a pagar para a conveniada
			    BigDecimal vlrLiquidoPagamentoConveniada = FuncoesUteis.calcularSubtracao( cPgVenda.getVlrLiquido(), vlrTxTotalizado, vlrTxTotalizadoPercetual, vlrTaxasFaixaVendas );
				cPgVenda.setVlrLiquidoPagamento( vlrLiquidoPagamentoConveniada );
				
		        if (vlrLiquidoPagamentoConveniada.compareTo(BigDecimal.ZERO) < 0) {
		        	try {
		        		
			            TaxaExtraConveniadaDTO dtoTaxaExtra = criarTaxaExtraConveniadaDTO( vlrLiquidoPagamentoConveniada, cPgVenda.getConveniados().getIdConveniados() );
			            TaxaExtraConveniada entity = mapper.toEntity(dtoTaxaExtra);
			            Conveniados con            = conveniadosRepository.findById( lv.getIdConveniados()).orElse(null);
			            TipoPeriodo tipoP          = tipoPeriodoRepository.findById( dtoTaxaExtra.getPeriodoCobrancaTaxa().getTipoPeriodoId()).orElse(null);
			            
			            entity.getPeriodoCobrancaTaxa().setTipoPeriodo(tipoP);
			            entity.setConveniados( con );
			            taxaExtraConveniadaService.save(entity);
			            
		        	} catch (Exception e) {
		    		    // Garante que a mensagem nunca será null
		    			msn = e.getMessage() != null ? e.getMessage() : "Erro sem mensagem específica";
		    	        logger.error("Erro no fechamentoConveniado do ciclo para conveniada: {}", msn, e);
		    	        throw new IllegalArgumentException( msn );
		    	        
		    		}
		        } 
                // atibui as informações ao Ciclo Pagamento Venda
				listaIdsConveniados.add              ( lv.getIdConveniados()        );
				cPgVenda.setItemTaxaExtraConveniada  ( listaItemTaxaExtraConveniada );
				cPgVenda.setFechamentoConvItensVendas( listaFciv                    );
				listaCicloPagamentoVenda.add         ( cPgVenda                     );

			}
			
//			System.out.println(listaCicloPagamentoVenda);
			
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
			// Garante que a mensagem nunca será null
			msn = e.getMessage() != null ? e.getMessage() : "Erro sem mensagem específica";
	 
	        logger.error("Erro no fechamentoConveniado: {}", msn, e);
	        
	        deletarCiclosComValidacao(listaCicloPagamentoVenda);
	        // Lança exceção com mensagem garantida
	        logger.error("Erro no fechamentoConveniado do ciclo para conveniada: {}", msn, e);
	        throw new IllegalArgumentException( msn );

			
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
				 listaIdsEntidades.add(idEntidade);
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
	private List<DadosFechamentoPagamentoCicloDTO> buscarFechamentosPorMes(String anoMes) {
    	
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
    private List<DadosFechamentoRecebimentoCicloDTO> buscarFechamentoRecebimentoCiclo(String anoMes) {
    	
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

     /**
     * Calcula a taxa aplicável com base no valor de comparação e nas faixas de taxas ativas.
     * 
     * A função verifica se o valor está dentro de alguma faixa ativa. Se não encontrar,
     * verifica se o valor é maior que o valor máximo da última faixa ativa.
     * 
     * @param taxas Lista de objetos TaxasFaixaVendas contendo as configurações das faixas
     * @param valorComparacao Valor a ser comparado com as faixas de taxas
     * @return BigDecimal com o valor da taxa aplicável (percentual ou fixo) conforme o tipo de cobrança,
     *         ou null se não encontrar nenhuma faixa válida
     * @throws IllegalArgumentException Se a lista de taxas for nula ou vazia, ou se o valor de comparação for nulo
     */
    public BigDecimal calcularTaxaAplicavel(List<TaxasFaixaVendas> taxas, BigDecimal valorComparacao) {
        // Validação dos parâmetros de entrada
        if (taxas == null || taxas.isEmpty()) {
            throw new IllegalArgumentException("A lista de taxas não pode ser nula ou vazia");
        }
        
        if (valorComparacao == null) {
            throw new IllegalArgumentException("O valor de comparação não pode ser nulo");
        }

        // Variável para armazenar a última taxa ativa encontrada
        TaxasFaixaVendas ultimaTaxaAtiva = null;

        // Percorre todas as taxas da lista
        for (TaxasFaixaVendas taxa : taxas) {
            // Verifica se a taxa está ativa
            if ("ATIVO".equalsIgnoreCase(taxa.getStatusTaxa())) {
                // Atualiza a última taxa ativa
                ultimaTaxaAtiva = taxa;
                
                // Verifica se o valor de comparação está dentro da faixa atual
                boolean dentroDaFaixa = valorComparacao.compareTo(taxa.getValorFaixaTaxaMin()) >= 0 && 
                                        valorComparacao.compareTo(taxa.getValorFaixaTaxaMax()) <= 0;
                
                if (dentroDaFaixa) {
                    // Retorna o valor da taxa conforme o tipo de cobrança
                    return taxa.getTipoCobrancaPercentual() ? 
                           taxa.getValorTaxaPercentual() : 
                           taxa.getValorTaxa();
                }
            }
        }
        
        // Se chegou aqui, não encontrou nenhuma faixa que contenha o valor
        // Verifica se existe uma última taxa ativa e se o valor é maior que seu máximo
        if (ultimaTaxaAtiva != null && 
            valorComparacao.compareTo(ultimaTaxaAtiva.getValorFaixaTaxaMax()) > 0) {
            // Retorna o valor da última taxa ativa conforme o tipo de cobrança
            return ultimaTaxaAtiva.getTipoCobrancaPercentual() ? 
                   ultimaTaxaAtiva.getValorTaxaPercentual() : 
                   ultimaTaxaAtiva.getValorTaxa();
        }
        
        // Se não encontrou nenhuma taxa válida, retorna null
        return null;
    }    
    
 
    
    /**
     * Cria e retorna um TaxaExtraConveniadaDTO preenchido com valores padrão,
     * utilizando o valor absoluto fornecido para o campo valorTaxa.
     * 
     * @param valorTaxa Valor absoluto que será atribuído ao campo valorTaxa
     * @return TaxaExtraConveniadaDTO preenchido
     * @throws IllegalArgumentException Se o valorTaxa for nulo
     */
    public static TaxaExtraConveniadaDTO criarTaxaExtraConveniadaDTO(BigDecimal valorTaxa, Long idConveniada) {
        if (valorTaxa == null) {
            throw new IllegalArgumentException("O valor da taxa não pode ser nulo");
        }

        TaxaExtraConveniadaDTO dto = new TaxaExtraConveniadaDTO();
        
        // Preenche os atributos básicos com valores padrão
        dto.setDescricaoTaxa         ( "Taxa devido a último faturamento ser negativa para a conveniada.");
        dto.setDataCriacao           ( LocalDateTime.now() );
        dto.setValorTaxa             ( valorTaxa.abs()     ); // Usa o valor absoluto do parâmetro
        dto.setStatusTaxa            ( "ATIVA"             );
        dto.setTipoCobrancaPercentual( false               );
        dto.setCobrancaValorBruto    ( false               );
        dto.setConveniadosId         ( idConveniada        );
        
        // Cria e preenche o PeriodoCobrancaTaxaDTO com valores padrão
        PeriodoCobrancaTaxaDTO periodoDTO = new PeriodoCobrancaTaxaDTO();
        periodoDTO.setDescricao            ( "Cobrança Única"                                           );
        periodoDTO.setDataInicio           ( LocalDate.now()                                            );
        periodoDTO.setDataFim              ( LocalDate.now().plusMonths(1)                              );
        periodoDTO.setObservacao           ( "Cobrança automática devido a último faturamento negatigo.");
        periodoDTO.setDataCriacao          ( LocalDateTime.now()                                        );
        periodoDTO.setDtUltimaCobranca     ( null                                                       );
        periodoDTO.setDtProximaCobranca    ( LocalDate.now().plusMonths(1)                              );
        periodoDTO.setQtyCobranca          ( 0L                                                         );
        periodoDTO.setTipoPeriodoId        ( 7L                                                         );
        // Campos ignorados conforme relacionamento:
        periodoDTO.setTaxaExtraConveniadaId( idConveniada                                               );
        periodoDTO.setTaxaExtraEntidadeId  ( null                                                       );
        
        dto.setPeriodoCobrancaTaxa(periodoDTO);
        
        // Campos explicitamente ignorados:
        // - id (não setado)
        // - itemTaxaExtraConveniada (não setado, ficará como null)
        
        return dto;
    }

    /**
     * Encontra a taxa aplicável com base no valor de comparação e faixas de taxas ativas.
     * 
     * A função percorre a lista de taxas para encontrar aquela cuja faixa (valor mínimo e máximo)
     * contém o valor informado, considerando apenas taxas com status "ATIVO". Caso não encontre
     * uma faixa que contenha o valor, verifica se o valor é maior que o valor máximo da última
     * taxa ativa encontrada.
     * 
     * @param taxas Lista de TaxasFaixaVendas a serem verificadas
     * @param valorComparacao Valor a ser comparado com as faixas de taxas
     * @return TaxasFaixaVendas correspondente à faixa encontrada ou última taxa ativa se o valor
     *         for maior que o máximo, ou null se nenhuma taxa ativa for encontrada
     * @throws IllegalArgumentException Se a lista de taxas for nula ou vazia, ou se o valor de
     *         comparação for nulo
     */
    public TaxasFaixaVendas encontrarTaxaAplicavel(List<TaxasFaixaVendas> taxas, BigDecimal valorComparacao) {
        // Validação dos parâmetros de entrada
        if (taxas == null || taxas.isEmpty()) {
            throw new IllegalArgumentException("A lista de taxas não pode ser nula ou vazia");
        }
        
        if (valorComparacao == null) {
            throw new IllegalArgumentException("O valor de comparação não pode ser nulo");
        }

        // Variável para armazenar a última taxa ativa encontrada
        TaxasFaixaVendas ultimaTaxaAtiva = null;

        // Percorre todas as taxas da lista
        for (TaxasFaixaVendas taxa : taxas) {
            // Verifica se a taxa está ativa
            if ("ATIVO".equalsIgnoreCase(taxa.getStatusTaxa())) {
                // Atualiza a referência da última taxa ativa
                ultimaTaxaAtiva = taxa;
                
                // Verifica se o valor está dentro da faixa atual
                boolean dentroDaFaixa = valorComparacao.compareTo(taxa.getValorFaixaTaxaMin()) >= 0 && 
                                        valorComparacao.compareTo(taxa.getValorFaixaTaxaMax()) <= 0;
                
                if (dentroDaFaixa) {
                    // Retorna a taxa cuja faixa contém o valor
                    return taxa;
                }
            }
        }
        
        // Se não encontrou nenhuma faixa que contenha o valor
        // Verifica se existe uma última taxa ativa e se o valor é maior que seu máximo
        if (ultimaTaxaAtiva != null && 
            valorComparacao.compareTo(ultimaTaxaAtiva.getValorFaixaTaxaMax()) > 0) {
            return ultimaTaxaAtiva;
        }
        
        // Se não encontrou nenhuma taxa válida, retorna null
        return null;
    }  
    
     /**
     * Calcula o valor da taxa conforme o tipo de cobrança definido no objeto TaxasFaixaVendas.
     * 
     * Se tipoCobrancaPercentual for true, calcula o valor percentual aplicando a porcentagem
     * (valorTaxaPercentual) sobre o valor base passado como parâmetro.
     * Se tipoCobrancaPercentual for false, retorna o valor fixo da taxa (valorTaxa).
     * 
     * @param taxa Objeto TaxasFaixaVendas contendo as configurações da taxa
     * @param valorBase Valor base para cálculo quando a taxa for percentual
     * @return BigDecimal contendo o valor da taxa calculada
     * @throws IllegalArgumentException Se algum dos parâmetros for nulo ou se valorTaxaPercentual
     *         for negativo quando tipoCobrancaPercentual for true
     */
    public BigDecimal calcularValorTaxa(TaxasFaixaVendas taxa, BigDecimal valorBase) {
        // Validação dos parâmetros de entrada
        if (taxa == null) {
            throw new IllegalArgumentException("O objeto TaxasFaixaVendas não pode ser nulo");
        }
        
        if (valorBase == null) {
            throw new IllegalArgumentException("O valor base não pode ser nulo");
        }
        
        if (valorBase.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor base não pode ser negativo");
        }

        // Verifica o tipo de cobrança
        if (taxa.getTipoCobrancaPercentual()) {
            // Validação adicional para taxa percentual
            if (taxa.getValorTaxaPercentual() == null || taxa.getValorTaxaPercentual().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("O valor da taxa percentual não pode ser negativo");
            }
            
            // Cálculo do valor percentual: (valorBase * percentual) / 100
            BigDecimal percentual = taxa.getValorTaxaPercentual().divide(new BigDecimal("100"));
            return valorBase.multiply(percentual).setScale(2, RoundingMode.HALF_UP);

        } else {
            // Retorna o valor fixo da taxa
            return taxa.getValorTaxa();
        }
    }
    
    /**
     * Verifica se uma taxa extra deve ser cobrada com base no período de cobrança.
     * 
     * @param taxaExtraConveniada Objeto contendo as informações da taxa e período de cobrança
     * @return true se a taxa deve ser cobrada, false caso contrário
     */
    public static boolean deveCobrarTaxaExtra(TaxaExtraConveniada taxaExtraConveniada) {
    	
        // Valida se o objeto e seus componentes necessários existem
        if (taxaExtraConveniada == null || 
            taxaExtraConveniada.getPeriodoCobrancaTaxa() == null || 
            taxaExtraConveniada.getPeriodoCobrancaTaxa().getTipoPeriodo() == null) {
            return true;
        }

        PeriodoCobrancaTaxa periodo     = taxaExtraConveniada.getPeriodoCobrancaTaxa();
        TipoPeriodo         tipoPeriodo = periodo.getTipoPeriodo();
        String              tipo        = tipoPeriodo.getTipo();
        LocalDate           dataAtual   = LocalDate.now();

        // 1ª Regra: Cobrança Única ("U")
        if ("U".equals(tipo)) {
            // Verifica se a quantidade de cobranças é maior que zero
            return periodo.getQtyCobranca() != null && periodo.getQtyCobranca() > 0;
        }

        // 2ª Regra: Cobrança Anual ("A")
        if ("A".equals(tipo)) {
            // Verifica se o ano atual é diferente do ano da última cobrança
            return periodo.getDtUltimaCobranca() == null || 
                   periodo.getDtUltimaCobranca().getYear() != dataAtual.getYear();
        }

        // 3ª Regra: Cobrança por Período ("P")
        if ("P".equals(tipo)) {
            // Verifica se a data atual está dentro do período de cobrança
            return dataAtual.isAfter(periodo.getDataInicio()) && 
                   dataAtual.isBefore(periodo.getDataFim());
        }

        // Caso o tipo não seja reconhecido ou não atenda a nenhuma condição
        return false;
    }
    
}

