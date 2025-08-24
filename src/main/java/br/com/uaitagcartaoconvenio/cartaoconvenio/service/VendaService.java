package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusRestabeleceLimiteCredito;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaPg;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaReceb;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Cartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.LimiteCredito;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniadaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Venda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ValidaVendaCataoPassaword;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.VendaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;

@Service
public class VendaService {

	@Autowired
	private VendaRepository vendaRepository;
	
	@Autowired
	private TaxaConveniadosService taxaConveniadosService;
	
	@Autowired
	private CartaoService cartaoService;
	
	@Autowired
	private TaxaConveniadaEntidadeService txConveniadaEntidadeService;
	
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public Venda salvarVendaService( Venda venda ) throws ExceptionCustomizada {

		if ( venda.getConveniados().getIdConveniados() == null )  throw new ExceptionCustomizada( "Não informado uma Conveniados!" );

		if ( venda.getItensVenda().isEmpty() || venda.getItensVenda().size() == 0 ) throw new ExceptionCustomizada( "Favor informar os Item(ns) da Venda!" );

		venda.setDescStatusVendaPg    ( StatusVendaPg.AGUARDANDO_PAGAMENTO             );
		venda.setDescStatusVendaReceb ( StatusVendaReceb.AGURARDANDO_RECEBIMENTO       );
		venda.setDescStatusVendas     ( StatusVendas.AGUARDANDO_PAGAMENTO              );
		venda.setDescRestLimiteCredito( StatusRestabeleceLimiteCredito.VENDA_REALIZADA );
		
		for(int ca = 0; ca < venda.getItensVenda().size(); ca++)
			venda.getItensVenda().get(ca).setVenda(venda);
		
		/* Este valor será calculano quando a venda for evefivada pelo usuário, para validar se exite uma taxa diferenciada para a entidade do usuario/conveniado.
		// Seta a Taxa Atual da Conveiniada
		venda.setTaxaConveniados( taxaConveniadosService.getTaxaConveniadosAtualByIdConveniados(venda.getConveniados().getIdConveniados()) );		
		Double valorCalcTaxaConvCalculado = FuncoesUteis.truncar( ( venda.getTaxaConveniados().getTaxa().doubleValue() / 100) * venda.getValorVenda().doubleValue() );		
		venda.setValorCalcTaxaConveniado(new BigDecimal(  valorCalcTaxaConvCalculado ));
		*/
		venda.setAnoMes(FuncoesUteis.getDataAtualFormatoYYYMM());

		venda.setCartao                 ( null               );
		venda.setTaxaEntidade           ( null               );
		venda.setValorCalcTaxaEntidade  ( new BigDecimal( 0) );
		venda.setTaxaConveniados        ( null               );
		venda.setValorCalcTaxaConveniado( new BigDecimal( 0 ));
		
		venda = vendaRepository.saveAndFlush( venda );
		
		return venda;
		
	}
	
	/***********************************************************************/
	/*                                                                     */
	/* Ajustes para alterar e validar em qual taxa o estabelecimento está. */
	/*                                                                     */
	/**
	 * @throws AccessDeniedException *********************************************************************/	
	public String validaVenda( ValidaVendaCataoPassaword validaVendaCataoPassaword )  {
	    String strRetorno = "Venda Realizada com sucesso!";	
	    
	 // 1. Primeiro busque o usuário/cartão apenas pelo número do cartão
	    LimiteCredito limiteCredito = vendaRepository.findByCartaoNumero(validaVendaCataoPassaword.getCartao());

	    if (limiteCredito != null) {
	        // 2. Obtenha o BCryptPasswordEncoder (pode ser injetado como bean também)
	        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	        
	        // 3. Compare a senha fornecida com o hash armazenado
	        if (!encoder.matches(validaVendaCataoPassaword.getPassword().trim(), 
	                            limiteCredito.getFuncionario().getPessoa().getUsuario().getSenha())) {
	        	// Senha inválida
	        	return "Senha incorreta";
	        } 
	    } else {
	    	return "Cartão não encontrado";
	    }
	    
	    
	    Venda venda = getVendaByIdVendas( validaVendaCataoPassaword.getIdVenda() );
//	    Venda venda = vendaRepository.findVendaByIdVenda( validaVendaCataoPassaword.getIdVenda() );
	    	 
	    if( venda == null ) return "Favor verificar, Venda não encontrada!";
	    	 
	    Double limiteFunc         = limiteCredito.getLimite().doubleValue();
	    Double valorUtilizadoFunc = limiteCredito.getValorUtilizado().doubleValue();
	 
	    Double valorVenda = venda.getValorVenda().doubleValue();
	    
	    if( (limiteFunc - valorUtilizadoFunc) < valorVenda) {	    	
	    	 vendaRepository.updateStatusVendas(venda.getIdVenda(), StatusVendas.PAGAMENTO_NAO_APROVADO.name());
	    	 return "Saldo insuficiente! ( Saldo: " + FuncoesUteis.formatarParaReal(limiteFunc - valorUtilizadoFunc) + " )";
	    }
		
	    Cartao cartao = cartaoService.getCartaoByNumeracao( validaVendaCataoPassaword.getCartao() );
	    venda.setCartao(cartao);
	    
	    Double valorUtilizadoAtualizada = valorUtilizadoFunc + valorVenda;

	    // Verifica se existe uma taxa especifica da Conveniada para uma Entidade;
	    Optional<TaxaConveniadaEntidade> taxaConveniadaEntidade = txConveniadaEntidadeService.getTxConvEntStatus(StatusTaxaConv.ATUAL, limiteCredito.getFuncionario().getEntidade().getIdEntidade(), venda.getConveniados().getIdConveniados());
	    
	    if( taxaConveniadaEntidade.isPresent() ) {
			venda.setIdTaxaConveniadosEntidate(taxaConveniadaEntidade.get().getId());		
			Double valorCalcTaxaConvCalculado = FuncoesUteis.truncar(( taxaConveniadaEntidade.get().getVlrTaxa().doubleValue() /100 ) * venda.getValorVenda().doubleValue());			
			venda.setValorCalcTaxaConveniado(new BigDecimal(  valorCalcTaxaConvCalculado ));
			venda.setTaxaConveniados(null);
		
		// Se não será utilizada a taxa Verifica se existe uma taxa especifica da Conveniada para a Entidade;	
	    }else { 
			venda.setTaxaConveniados( taxaConveniadosService.getTaxaConveniadosAtualByIdConveniados(venda.getConveniados().getIdConveniados()) );		
			Double valorCalcTaxaConvCalculado = FuncoesUteis.truncar( ( venda.getTaxaConveniados().getTaxa().doubleValue() / 100) * venda.getValorVenda().doubleValue() );		
			venda.setValorCalcTaxaConveniado(new BigDecimal(  valorCalcTaxaConvCalculado ));
	    }

	    venda.setDescStatusVendas     ( StatusVendas.PAGAMENTO_APROVADO                );
	    venda.setDescRestLimiteCredito( StatusRestabeleceLimiteCredito.VENDA_REALIZADA );
	    
	    // Calcuclo o valor da taxa referente a Entidade!
    	TaxaEntidade taxaEntidade   = vendaRepository.taxaEntidadeByNumeroCatao( validaVendaCataoPassaword.getCartao() );
    	Double valorCalcTaxaEntCalc = FuncoesUteis.truncar(( taxaEntidade.getTaxaEntidade().doubleValue() /100 ) * venda.getValorVenda().doubleValue() );
	    venda.setValorCalcTaxaEntidade(new BigDecimal(valorCalcTaxaEntCalc));
	    venda.setTaxaEntidade(taxaEntidade);
	    
	    try {
	    	vendaRepository.updateValorLimiteCredito(limiteCredito.getIdLimiteCredito(), new BigDecimal(valorUtilizadoAtualizada));
	    	venda = vendaRepository.saveAndFlush( venda );
	    }catch ( Exception e ) {
	    	vendaRepository.updateValorLimiteCredito(limiteCredito.getIdLimiteCredito(), new BigDecimal(valorUtilizadoFunc));
	    	return e.getMessage();
		}
	    
	    return strRetorno;

	}
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public Venda getVendaByIdVendas( Long idVenda )  {
		Venda Venda = vendaRepository.findVendaByIdVenda( idVenda );		
		return Venda;		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Venda> getListaVendaByAnoMes( String anoMes )  {		
		List<Venda> listaVenda = vendaRepository.listaVendaByAnoMes( anoMes );		
		return listaVenda;		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Venda> getListaVendaByDtVenda( String dtVendaIni, String dtVendaFim )  {
		
		String dtVendaIniFormat =  FuncoesUteis.validarEConverterData(dtVendaIni, "00:00:00");
		String dtVendaFimFormat =  FuncoesUteis.validarEConverterData(dtVendaFim, "23:59:59");
		
		List<Venda> listaCicloPagamentoVenda = vendaRepository.listaVendaByDtVenda( FuncoesUteis.converterParaTimestamp(dtVendaIniFormat), FuncoesUteis.converterParaTimestamp(dtVendaFimFormat) );
		
		return listaCicloPagamentoVenda;		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Venda> getListaVendaByLoginUser( String loginUser )  {		
		List<Venda> listaVenda = vendaRepository.listaVendaByLoginUser( loginUser );		
		return listaVenda;		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Venda> getListaVendaByDescStatusVendaReceb( StatusVendaReceb descStatusVendaReceb )  {		
		List<Venda> listaVenda = vendaRepository.listaVendaByDescStatusVendaReceb( descStatusVendaReceb );		
		return listaVenda;		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Venda> getListaVendaByDescStatusVendaPg( StatusVendaPg descStatusVendaPg )  {		
		List<Venda> listaVenda = vendaRepository.listaVendaByDescStatusVendaPg( descStatusVendaPg );		
		return listaVenda;		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Venda> getListaVendaByDescStatusVendas( StatusVendas descStatusVendas )  {		
		List<Venda> listaVenda = vendaRepository.listaVendaByDescStatusVendas( descStatusVendas );		
		return listaVenda;		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Venda> getListaVendaByIdConveniados( Long idConveniados )  {
		List<Venda> listaVenda = vendaRepository.listaVendaByIdConveniados( idConveniados );		
		return listaVenda;		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Venda> getListaVendaByIdConveniadosAnoMes( String anoMes, Long idConveniados )  {
		List<Venda> listaVenda = vendaRepository.listaVendaByIdConveniadosAnoMes( anoMes, idConveniados );		
		return listaVenda;		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Venda> getListaVendaByIdConveniadosDtVenda( String dtVendaIni, String dtVendaFim, Long idConveniados )  {
		String dtVendaIniFormat =  FuncoesUteis.validarEConverterData(dtVendaIni, "00:00:00");
		String dtVendaFimFormat =  FuncoesUteis.validarEConverterData(dtVendaFim, "23:59:59");

		List<Venda> listaVenda = vendaRepository.listaVendaByIdConveniadosDtVenda( FuncoesUteis.converterParaTimestamp(dtVendaIniFormat), FuncoesUteis.converterParaTimestamp(dtVendaFimFormat), idConveniados );		
		return listaVenda;		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Venda> getListaVendaByIdConveniadosDescStatusVendas( StatusVendas descStatusVendas, Long idConveniados )  {
		List<Venda> listaVenda = vendaRepository.listaVendaByIdConveniadosDescStatusVendas( descStatusVendas, idConveniados );		
		return listaVenda;		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Venda> getListaVendaByIdConveniadosDescStatusVendaPg( StatusVendaPg descStatusVendaPg, Long idConveniados )  {
		List<Venda> listaVenda = vendaRepository.listaVendaByIdConveniadosDescStatusVendaPg( descStatusVendaPg, idConveniados );		
		return listaVenda;		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Venda> getListaVendaByIdConveniadosDescStatusVendaReceb( StatusVendaReceb descStatusVendaReceb, Long idConveniados )  {
		List<Venda> listaVenda = vendaRepository.listaVendaByIdConveniadosDescStatusVendaReceb( descStatusVendaReceb, idConveniados );		
		return listaVenda;		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Venda> getListaVendaByNomeConveniado( String nomeConveniado )  {
		List<Venda> listaVenda = vendaRepository.listaVendaByNomeConveniado( nomeConveniado );		
		return listaVenda;		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Venda> getListaVendaByCartao( String numCartao )  {
		List<Venda> listaVenda = vendaRepository.listaVendaByCartao( numCartao );		
		return listaVenda;		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Venda> getListaVendaByCartaoDescStatusVendas( StatusVendas descStatusVendas, String numCatao )  {
		List<Venda> listaVenda = vendaRepository.listaVendaByCartaoDescStatusVendas( descStatusVendas, numCatao );		
		return listaVenda;		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Venda> getListaVendaSelecaoAntecipacao( Long idConveniados )  {
		List<Venda> listaVenda = vendaRepository.findVendasSelectAntecipacao( idConveniados );		
		return listaVenda;		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public void updateStatusVendas( Long idConveniados, StatusVendas descStatusVendas  ) {
		
		vendaRepository.updateStatusVendas( idConveniados, descStatusVendas.name() );		

	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public void updateStatusVendaPg( Long idConveniados, StatusVendaPg descStatusVendaPg  ) {
		
		vendaRepository.updateStatusVendaPg( idConveniados, descStatusVendaPg.name() );		

	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public void updateStatusVendaReceb( Long idConveniados, StatusVendaReceb descStatusVendaReceb ) {
		
		vendaRepository.updateStatusVendaReceb( idConveniados, descStatusVendaReceb.name() );		

	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public void updateStatusVendasFechamentoAutomatico( String anoMes ) {
		
		vendaRepository.updateStatusVendasFechamentoAutomatico( anoMes );		

	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    public int atualizarVendasFechamentoConcluidoRecebimento(List<Long> idsVendas) {
        // Divide em lotes para evitar muito consumo de memória
    	int totalUpdate = 0;
        int batchSize = 1000;
        for (int i = 0; i < idsVendas.size(); i += batchSize) {
            List<Long> batch = idsVendas.subList(i, Math.min(i + batchSize, idsVendas.size()));
            totalUpdate += vendaRepository.atualizarStatusVendaRecebEmMassa(batch, StatusVendaReceb.VENDA_RECEBIDA);
        }
        return totalUpdate;
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    public int atualizarVendasFechamentoConcluidoPG(List<Long> idsVendas) {
        // Divide em lotes para evitar muito consumo de memória
    	int totalUpdate = 0;
        int batchSize = 1000;
        for (int i = 0; i < idsVendas.size(); i += batchSize) {
            List<Long> batch = idsVendas.subList(i, Math.min(i + batchSize, idsVendas.size()));
            totalUpdate += vendaRepository.atualizarStatusVendaPgEmMassa(batch, StatusVendaPg.VENDA_PAGA);
        }
        return totalUpdate;
    }

}
