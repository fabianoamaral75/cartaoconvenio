package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoConveniado;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Pessoa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ConveniadosRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;
import jakarta.transaction.Transactional;

@Service
public class ConveniadosService {
	
	@Autowired
	private ConveniadosRepository conveniadosRepository;
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private TaxaConveniadosService taxaConveniadosService;
	
	private static final Logger logger = LogManager.getLogger(ConveniadosService.class);
	
	@Transactional
	public Conveniados salvarConveniadosService(Conveniados conveniados) throws ExceptionCustomizada {
	    // Validação do objeto principal
	    if (conveniados == null) {
	        throw new IllegalArgumentException("Conveniado não pode ser nulo");
	    }

	    // Validações básicas
	    if (conveniados.getNicho() == null) {
	        throw new IllegalArgumentException("Nicho é obrigatório");
	    }
	    
	    if (conveniados.getRamoAtividade() == null) {
	        throw new IllegalArgumentException("Ramo de atividade é obrigatório");
	    }
	    
	    // Configura relacionamentos básicos
	    conveniados.getNicho().setConveniados(conveniados);
	    conveniados.getRamoAtividade().setConveniados(conveniados);
	    
	    // Configura taxa conveniados
	    if (conveniados.getTaxaConveniados() == null) {
	        conveniados.setTaxaConveniados(new ArrayList<>());
	    }
	    
	    if (conveniados.getTaxaConveniados().isEmpty()) {
	        TaxaConveniados taxa = new TaxaConveniados();
	        taxa.setDescStatusTaxaCon(StatusTaxaConv.ATUAL);
	        taxa.setConveniados(conveniados);
	        conveniados.getTaxaConveniados().add(taxa);
	    } else {
	        conveniados.getTaxaConveniados().get(0).setDescStatusTaxaCon(StatusTaxaConv.ATUAL);
	        conveniados.getTaxaConveniados().get(0).setConveniados(conveniados);
	    }
	    
	    // Configura status
	    conveniados.setDescStatusConveniada(StatusConveniada.AGUARDANDO_CONFIRMACAO);
	    
	    // Tratamento de contratos
	    if (conveniados.getContratoConveniado() == null) {
	        conveniados.setContratoConveniado(new ArrayList<>());
	    }
	    
	    if (conveniados.getContratoConveniado().isEmpty()) {
	        ContratoConveniado contratoPadrao = new ContratoConveniado();
	        contratoPadrao.setConveniados(conveniados);
	        contratoPadrao.setObservacao("Contrato inicial criado automaticamente");
	        
	        VigenciaContratoConveniada vigenciaPadrao = new VigenciaContratoConveniada();
	        vigenciaPadrao.setDataInicio(LocalDate.now());
	        vigenciaPadrao.setDataFinal(LocalDate.now().plusYears(1));
	        vigenciaPadrao.setObservacao("Vigência inicial criada automaticamente");
	        vigenciaPadrao.setContratoConveniado(contratoPadrao);
	        
	        contratoPadrao.setVigencias(new ArrayList<>());
	        contratoPadrao.getVigencias().add(vigenciaPadrao);
	        conveniados.getContratoConveniado().add(contratoPadrao);
	    } else {
	        ContratoConveniado contrato = conveniados.getContratoConveniado().get(0);
	        contrato.setConveniados(conveniados);
	        
	        if (contrato.getVigencias() == null) {
	            contrato.setVigencias(new ArrayList<>());
	        }
	        
	        if (contrato.getVigencias().isEmpty()) {
	            VigenciaContratoConveniada vigenciaPadrao = new VigenciaContratoConveniada();
	            vigenciaPadrao.setDataInicio(LocalDate.now());
	            vigenciaPadrao.setDataFinal(LocalDate.now().plusYears(1));
	            vigenciaPadrao.setObservacao("Vigência inicial criada automaticamente");
	            vigenciaPadrao.setContratoConveniado(contrato);
	            contrato.getVigencias().add(vigenciaPadrao);
	        } else {
	            for (VigenciaContratoConveniada vigencia : contrato.getVigencias()) {
	                if (vigencia.getDataInicio() == null) {
	                    vigencia.setDataInicio(LocalDate.now());
	                }
	                if (vigencia.getDataFinal() == null) {
	                    vigencia.setDataFinal(LocalDate.now().plusYears(1));
	                }
	                
	                // Corrigido o problema do NullPointerException
	                vigencia.setContratoConveniado(contrato);
	                
	                // Garante que a lista de vigencias no contrato está correta
	                if (vigencia.getContratoConveniado() != null && 
	                    vigencia.getContratoConveniado().getVigencias() != null) {
	                    vigencia.getContratoConveniado().getVigencias().forEach(v -> v.setContratoConveniado(contrato));
	                }
	            }
	        }
	    }
	    
	    // Tratamento de TAXAS EXTRAS
	    if (conveniados.getTaxaExtraConveniada() != null && !conveniados.getTaxaExtraConveniada().isEmpty()) {
	        for (TaxaExtraConveniada taxa : conveniados.getTaxaExtraConveniada()) {
	            // Validação do período de cobrança
	            if (taxa.getPeriodoCobrancaTaxa() == null) {
	                throw new ExceptionCustomizada("Período de cobrança é obrigatório para taxas extras");
	            }
	            
	            // Validações do período
	            LocalDate dataInicio = taxa.getPeriodoCobrancaTaxa().getDataInicio();
	            LocalDate dataFim = taxa.getPeriodoCobrancaTaxa().getDataFim();
	            
	            if (dataInicio == null || dataFim == null) {
	                throw new ExceptionCustomizada("Datas de início e fim são obrigatórias para o período de cobrança");
	            }
	            
	            if (dataInicio.isAfter(dataFim)) {
	                throw new ExceptionCustomizada("Data de início não pode ser posterior à data de fim");
	            }
	            
	            taxa.getPeriodoCobrancaTaxa().setTaxaExtraConveniada(taxa);
	            taxa.setConveniados(conveniados);
	        }
	    }
	    
	    try {
	        return conveniadosRepository.saveAndFlush(conveniados);
	    } catch (Exception e) {
	        logger.error("Erro ao salvar conveniado", e);
	        throw new ExceptionCustomizada("Erro ao salvar conveniado: " + e.getMessage());
	    }
	}	
	
	
    /* ******************************************************************************************************************************** */
	/*                                                                                                                                  */
	/*                                                                                                                                  */
    /* ******************************************************************************************************************************** */
	public Pessoa salvarConveniadosService( Pessoa pessoa ) throws ExceptionCustomizada {
		
		
		pessoa.getConveniados().getNicho().setConveniados(pessoa.getConveniados());
		pessoa.getConveniados().getRamoAtividade().setConveniados(pessoa.getConveniados());
		pessoa.getConveniados().setCicloPagamentoVenda(null);
		
		if( pessoa.getConveniados().getIdConveniados() != null ) {
			TaxaConveniados taxaConveniados = conveniadosRepository.findTxConvByIdconv(pessoa.getConveniados().getIdConveniados());
			if( taxaConveniados != null ) conveniadosRepository.updateStatusTaxaConveniados(taxaConveniados.getIdTaxaConveniados());
			
			Conveniados con = conveniadosRepository.findUserByIdconv( pessoa.getConveniados().getIdConveniados() );
			
			pessoa.setUsuario( con.getPessoa().getUsuario() );
		}
		
		TaxaConveniados taxaConveniados = new TaxaConveniados();
		
		taxaConveniados.setDescStatusTaxaCon(StatusTaxaConv.ATUAL);
		taxaConveniados.setTaxa(pessoa.getConveniados().getTaxaConveniados().get(0).getTaxa());
		taxaConveniados.setConveniados(pessoa.getConveniados());
		
		taxaConveniados = taxaConveniadosService.salvarTaxaConveniados(taxaConveniados);
		pessoa.getConveniados().getTaxaConveniados().add(taxaConveniados);

//		pessoa.getConveniados().getTaxaConveniados().get(0).setDescStatusTaxaCon(StatusTaxaConv.ATUAL);
//		pessoa.getConveniados().getTaxaConveniados().get(0).setConveniados(pessoa.getConveniados());
		pessoa.getConveniados().setDescStatusConveniada(StatusConveniada.AGUARDANDO_CONFIRMACAO);
		pessoa.getConveniados().setPessoa(pessoa.getConveniados().getPessoa());
		
		pessoa.setPessoaFisica(null);
		pessoa.setFuncionario(null);
		pessoa.getConveniados().setIsTaxasFaixaVendas(true);
				
		pessoa = pessoaService.savarPassoa(pessoa);
		
		return pessoa;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public Conveniados getConveniadosByCnpj( String cnpj )  {
		
		String resultCnpj = FuncoesUteis.removerCaracteresNaoNumericos( cnpj );
		
		Conveniados listaConveniados = conveniadosRepository.conveniadosByCnpj( resultCnpj );
		
		return listaConveniados;		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Conveniados> getConveniadosByNome( String nome )  {
		
		List<Conveniados> listaConveniados = conveniadosRepository.listaConveniadosByNome( nome );
		
		return listaConveniados;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Conveniados> getConveniadosByCidade( String cidade )  {
		
		List<Conveniados> listaConveniados = conveniadosRepository.listaConveniadosByCidade( cidade );
		
		return listaConveniados;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public Conveniados getConveniadoId( Long id )  {				
		Conveniados conveniados = conveniadosRepository.findById(id).orElse(null);
		return conveniados;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public String getNomeConveniada( Long id )  {				
		return conveniadosRepository.getNomeConveniada(id);
		
	}

    // Método 1: Atualização direta via query em lote
    public int atualizarAnoMesRecebimentoPosFechamentoEmLote(List<Long> ids, String mesRecebimento) {
        return conveniadosRepository.updateMesRecebimentoPosFechamentoEmLote(ids, mesRecebimento);
    }

}
