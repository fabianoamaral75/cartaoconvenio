package br.com.uaitagcartaoconvenio.cartaoconvenio.service;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.FechamentoConvItensVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.FechamentoEntContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Venda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.DadosFechamentoPagamentoCicloDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.DadosFechamentoRecebimentoCicloDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RestabelecerLimitCreditoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ConveniadosRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.EntidadeRespository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaConveiniadosRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.VendaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.BusinessException;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;


@Service
public class FecahementoCicloService {

	@Autowired
	private CicloPagamentoVendaService cicloPagamentoVendaService;
	
	@Autowired
	private VendaRepository vendaRepository;
	
	@Autowired
	private ConveniadosRepository conveniadosRepository;
	
	@Autowired
	private TaxaConveiniadosRepository taxaConveiniadosRepository;
	
	@Autowired
	private EntidadeRespository entidadeRespository;
		
	@Autowired
	private ContasReceberService contasReceberService;
	
	@Autowired
	private LimitecreditoService limitecreditoService;


	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public String fechamentoCiclo( String anoMesAnterior, Boolean execManual )  {
		
		// String anoMesAnterior = FuncoesUteis.getPreviousMonthFormatted();
		if( !vendaRepository.isStatusVendaFechamento(anoMesAnterior) ) return "NÃO EXISTE CICLO PARA SER PROCESSADO PARA O PERÍODO: " + anoMesAnterior;
	    
		// verifica se já existe um Ciclo de fechamento para o pagamento e recebimento.
		validaFechamentoCiclo( anoMesAnterior );
		
		String msnFechamento = "FECHAMENTO_AUTOMATICO_OK";
		// Gera as informaçoes do ciclo de pagamento para as conveniadas
		List<CicloPagamentoVenda> listaCicloPagamentoVenda = new ArrayList<CicloPagamentoVenda>();
		// Volta o limite de creditos dos funcionarios.
		List<RestabelecerLimitCreditoDTO> listaRestabelecerLimitCredito = new ArrayList<RestabelecerLimitCreditoDTO>();
		// Gera as informaçoes do ciclo de controle de recebimento das Encitidades.
		List<ContasReceber> listaCicloReceberVenda = new ArrayList<ContasReceber>();
		try {
			listaCicloPagamentoVenda      = this.fechamentoConveniado( msnFechamento, anoMesAnterior ); 
			listaRestabelecerLimitCredito = this.restabelecerLimiteCreditoFuncionarios( msnFechamento, anoMesAnterior );
	        listaCicloReceberVenda        = this.fechamentoEntidade( msnFechamento, anoMesAnterior, execManual );
			
		}catch (Exception e) {
			 msnFechamento = e.getMessage();
			 // Realiza Rollback em caso de erro em qualquer uma das etapas.
	   		 if( listaCicloPagamentoVenda.size()      > 0 ) cicloPagamentoVendaService.deletarListaCiclos(listaCicloPagamentoVenda);
	   		 if( listaRestabelecerLimitCredito.size() > 0 ) restabelecerLimiteCreditoFuncionariosRollback( listaRestabelecerLimitCredito );
    		 if( listaCicloReceberVenda.size()        > 0 ) contasReceberService.deletarListaCiclos(listaCicloReceberVenda);
		}
 		return msnFechamento;		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public void validaFechamentoCiclo( String anoMesAnterior ) {
		// String anoMesAnterior = FuncoesUteis.getPreviousMonthFormatted();
		if( cicloPagamentoVendaService.existCicloFechamentoPagamento(anoMesAnterior) ) cicloPagamentoVendaService.updateCancelamentoStatusCicloPagamentoVenda( anoMesAnterior );
		if( contasReceberService.existCicloFechamentoRecebimento(anoMesAnterior)     ) contasReceberService.updateCancelamentoStatusCicloRecebimentoVenda    ( anoMesAnterior );
		vendaRepository.updateStatusVendaReprocessamentoFechamento( anoMesAnterior );
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	private List<RestabelecerLimitCreditoDTO> restabelecerLimiteCreditoFuncionarios( String msn, String anoMesAnterior ) {
		
		// String anoMesAnterior = FuncoesUteis.getPreviousMonthFormatted();
		List<RestabelecerLimitCreditoDTO> listaRestabelecerLimitCredito = limitecreditoService.listaRestabelecerLimiteCredito(anoMesAnterior);
		
		for(RestabelecerLimitCreditoDTO lrlc : listaRestabelecerLimitCredito) {
			limitecreditoService.updateRestabelecerLimiteCredito( lrlc.getIdFuncionario(), lrlc.getValorRestituir());
		}
		// Atualizar o status do limite de credito da tabela vendas para o status de aplicado e restabelecido.
		vendaRepository.updateStatusLimiteRestabelecido( anoMesAnterior );
		
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
	private List<CicloPagamentoVenda> fechamentoConveniado( String msn, String anoMesAnterior ) {
		
		msn = null;
		List<CicloPagamentoVenda> listaCicloPagamentoVenda = new ArrayList<CicloPagamentoVenda>();
		try {
			// String anoMesAnterior = FuncoesUteis.getPreviousMonthFormatted();
			List<DadosFechamentoPagamentoCicloDTO> listaVendasFechamento = vendaRepository.listaFechamentoVendaPorMesAutomatica( anoMesAnterior ) ;		 

			for( DadosFechamentoPagamentoCicloDTO lv: listaVendasFechamento ) {
				CicloPagamentoVenda cPgVenda = new CicloPagamentoVenda();
				int diasParaPagamento        = conveniadosRepository.qtyDiasPagamento( lv.getIdConveniados() );
				Date dtPagamento             = FuncoesUteis.somarDiasDataAtual( diasParaPagamento );
				Date dataAtual               = Calendar.getInstance().getTime();
				
				cPgVenda.setAnoMes                      ( anoMesAnterior                                                               );
				cPgVenda.setDescStatusPagamento         ( StatusCicloPgVenda.AGUARDANDO_UPLOAD_NF                                      );
				cPgVenda.setDtAlteracao                 ( dataAtual                                                                    );
				cPgVenda.setDtCriacao                   ( dataAtual                                                                    );
				cPgVenda.setDtPagamento                 ( dtPagamento                                                                  );
				cPgVenda.setValorCiclo                  ( lv.getSomatorioValorVenda()                                                  );
				cPgVenda.setValorCalcTaxaConveniadoCiclo( lv.getSomatorioVlrCalcTxConv()                                               );
				cPgVenda.setConveniados                 ( conveniadosRepository.findById(lv.getIdConveniados()).orElse(null)           );
				cPgVenda.setTaxaConveiniados            ( taxaConveiniadosRepository.findById(lv.getIdTaxaConveiniados()).orElse(null) );

				List<Venda> listaVenda = vendaRepository.listaVendaByIdConveniadosStatusAnoMes(anoMesAnterior, StatusVendas.PAGAMENTO_APROVADO, lv.getIdConveniados() );
				List<FechamentoConvItensVendas> listaFciv =  new ArrayList<FechamentoConvItensVendas>();
				for(Venda venda : listaVenda) {
					FechamentoConvItensVendas fciv = new FechamentoConvItensVendas();
					fciv.setVenda              ( venda    );
					fciv.setCicloPagamentoVenda( cPgVenda );
					listaFciv.add(fciv);
				}			
				cPgVenda.setFechamentoConvItensVendas(listaFciv);
				
				listaCicloPagamentoVenda.add(cPgVenda);
			}
			
			// Grava na base as informaçoes de conta a Pagar.
			if(listaCicloPagamentoVenda.size() > 30000) listaCicloPagamentoVenda = cicloPagamentoVendaService.salvarListaGrande(listaCicloPagamentoVenda, msn);
			else  listaCicloPagamentoVenda = cicloPagamentoVendaService.salvarListaCiclos(listaCicloPagamentoVenda, msn );
			
			if( listaCicloPagamentoVenda != null ) {
			    // Atualiza Status referente ao fechamento dos pagamento (Fechamento) das empresas conveniadas.
			    vendaRepository.updateStatusVendaPgFechamentoAutomatico( anoMesAnterior );
			}else {
			    msn = "Error: erro na gerração do cilco de contadas a pagar!\n" + msn;
			    return null;
			}
			

		} catch (Exception e) {
			msn = e.getMessage();
			if( listaCicloPagamentoVenda.get(0).getIdCicloPagamentoVenda() != null )   cicloPagamentoVendaService.deletarListaCiclos(listaCicloPagamentoVenda);
		}
		
		return listaCicloPagamentoVenda;
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	private List<ContasReceber> fechamentoEntidade( String msn, String anoMesAnterior, Boolean execManual ){
		
		msn = null;
		Long idEntidade = 0L;
		// String anoMesAnterior = FuncoesUteis.getPreviousMonthFormatted();
		List<ContasReceber> listaContasReceberVenda =  new ArrayList<ContasReceber>();
		try {
			
			List<DadosFechamentoRecebimentoCicloDTO> listaFechamentoRecebimentoCiclo = vendaRepository.listaFechamentoRecebimentoPorMesAutomatica( anoMesAnterior ) ;		
			
			for( DadosFechamentoRecebimentoCicloDTO lrv: listaFechamentoRecebimentoCiclo ) {
				 
				 ContasReceber contasReceber = new ContasReceber();
				 int diasParaRecebimento      = entidadeRespository.qtyDiasRecebimento( lrv.getIdEntidade() );
				 Date dtPagamento             = FuncoesUteis.somarDiasDataAtual( diasParaRecebimento );
				 
				 contasReceber.setAnoMes(anoMesAnterior);
				 contasReceber.setDescStatusReceber(StatusReceber.AGUARDANDO_UPLOAD_NF);
				 contasReceber.setValorReceber(lrv.getSomatorioValorVenda());
				 contasReceber.setValorCalcTaxaEntidadeCiclo(lrv.getSomatorioVlrCalcTxEnt());
				 contasReceber.setDtPrevisaoRecebimento(dtPagamento);
				 
				 idEntidade = lrv.getIdEntidade();
				 contasReceber.getEntidade().setIdEntidade( lrv.getIdEntidade() );
				 contasReceber.getTaxaEntidade().setIdTaxaEntidade(lrv.getIdTaxaEntidade());

				 List<Venda> listaVenda = vendaRepository.listaVendaByIdEntidadeStatusAnoMes( anoMesAnterior, "PAGAMENTO_APROVADO", lrv.getIdEntidade() );
				 
				 List<FechamentoEntContasReceber> listaFecr =  new ArrayList<FechamentoEntContasReceber>();
				 
				 for( Venda venda :listaVenda ) {
					 FechamentoEntContasReceber fecr = new FechamentoEntContasReceber();
					 fecr.setContasReceber(contasReceber);
					 fecr.setVenda(venda);
					 listaFecr.add(fecr);
				 }
				 contasReceber.setFechamentoEntContasReceber(listaFecr);
				 
				 listaContasReceberVenda.add(contasReceber);
			}
			
			// Grava na base as informaçoes de conta a Receber.
			if(listaContasReceberVenda.size() > 30000) listaContasReceberVenda = contasReceberService.salvarListaGrande(listaContasReceberVenda, msn);
			else  listaContasReceberVenda = contasReceberService.salvarListaCiclos(listaContasReceberVenda, msn);
			
		    if( listaContasReceberVenda != null ) {
				// Atualiza Status referente ao fechamento dos recebimentod (Fechamento) das Entidades.
				vendaRepository.updateStatusVendaRecebFechamentoAutomatico( anoMesAnterior );
				if( execManual )vendaRepository.updateStatusVendasFechamentoManual( anoMesAnterior ); 
				else vendaRepository.updateStatusVendasFechamentoAutomatico( anoMesAnterior );
			    		     	
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
	    			    .addDetail("Periódo", anoMesAnterior);
			
		}
		
		return listaContasReceberVenda;
		
	}	
	
}

