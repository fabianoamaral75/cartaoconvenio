package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusContrato;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import br.com.uaitagcartaoconvenio.cartaoconvenio.excecoes.BusinessException;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoConveniado;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Nicho;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Pessoa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.RamoAtividade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ConveniadosDTO;
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
	            vigenciaPadrao.setDescStatusContrato(StatusContrato.VIGENTE);
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

    
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   public Pessoa atualizarConveniadaCompleta(Pessoa pessoaAtualizada) throws ExceptionCustomizada {
        // Validações básicas
        if (pessoaAtualizada.getConveniados() == null) {
            throw new ExceptionCustomizada("Dados da conveniada não podem ser nulos");
        }
        
        // Busca a conveniada existente
        Conveniados conveniadaExistente = conveniadosRepository.findById(pessoaAtualizada.getConveniados().getIdConveniados())
                .orElseThrow(() -> new ExceptionCustomizada("Conveniada não encontrada"));
        
        // Atualiza os dados básicos
        conveniadaExistente.setSite(pessoaAtualizada.getConveniados().getSite());
        conveniadaExistente.setObs(pessoaAtualizada.getConveniados().getObs());
        conveniadaExistente.setDiaPagamento(pessoaAtualizada.getConveniados().getDiaPagamento());
        
        // Atualiza relacionamentos
        if (pessoaAtualizada.getConveniados().getNicho() != null) {
            conveniadaExistente.setNicho(pessoaAtualizada.getConveniados().getNicho());
        }
        
        if (pessoaAtualizada.getConveniados().getRamoAtividade() != null) {
            conveniadaExistente.setRamoAtividade(pessoaAtualizada.getConveniados().getRamoAtividade());
        }
        
        // Atualiza taxas
        if (pessoaAtualizada.getConveniados().getTaxaConveniados() != null && 
            !pessoaAtualizada.getConveniados().getTaxaConveniados().isEmpty()) {
            
            // Marca taxas antigas como desatualizadas
            conveniadaExistente.getTaxaConveniados().forEach(t -> t.setDescStatusTaxaCon(StatusTaxaConv.DESATUALIZADA));
            
            // Adiciona nova taxa
            TaxaConveniados novaTaxa = new TaxaConveniados();
            novaTaxa.setTaxa(pessoaAtualizada.getConveniados().getTaxaConveniados().get(0).getTaxa());
            novaTaxa.setDescStatusTaxaCon(StatusTaxaConv.ATUAL);
            novaTaxa.setConveniados(conveniadaExistente);
            conveniadaExistente.getTaxaConveniados().add(novaTaxa);
        }
        
        // Salva a pessoa (que cascateia para a conveniada)
        return pessoaService.savarPassoa(pessoaAtualizada);
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    public Conveniados atualizarStatusConveniada(Long id, StatusConveniada novoStatus) throws ExceptionCustomizada {
        Conveniados conveniada = conveniadosRepository.findById(id)
                .orElseThrow(() -> new ExceptionCustomizada("Conveniada não encontrada"));
        
        // Valida transições de status permitidas
        if (conveniada.getDescStatusConveniada() == StatusConveniada.DESATIVADA && 
            novoStatus != StatusConveniada.ATIVA) {
            throw new ExceptionCustomizada("Só é possível reativar uma conveniada desativada");
        }
        
        conveniada.setDescStatusConveniada(novoStatus);
        return conveniadosRepository.save(conveniada);
    }
    
    @Transactional
    public Conveniados atualizarConveniadaSimples(ConveniadosDTO conveniadosDTO) throws ExceptionCustomizada {
        // Validações básicas
        if (conveniadosDTO == null) {
            throw new ExceptionCustomizada("Dados da conveniada não podem ser nulos");
        }
        
        // Busca a conveniada existente
        Conveniados conveniadaExistente = conveniadosRepository.findById(conveniadosDTO.getIdConveniados())
                .orElseThrow(() -> new ExceptionCustomizada("Conveniada não encontrada"));
        
        // Atualiza os dados básicos
        conveniadaExistente.setSite(conveniadosDTO.getSite());
        conveniadaExistente.setObs(conveniadosDTO.getObs());
        conveniadaExistente.setDiaPagamento(conveniadosDTO.getDiaPagamento());
        
        // Atualiza relacionamentos
        if (conveniadosDTO.getNicho() != null) {
            Nicho nicho = new Nicho();
            nicho.setIdNicho(conveniadosDTO.getNicho().getIdNicho());
            conveniadaExistente.setNicho(nicho);
            nicho.setConveniados(conveniadaExistente);
        }
        
        if (conveniadosDTO.getRamoAtividade() != null) {
            RamoAtividade ramoAtividade = new RamoAtividade();
            ramoAtividade.setIdRamoAtividade(conveniadosDTO.getRamoAtividade().getIdRamoAtividade());
            conveniadaExistente.setRamoAtividade(ramoAtividade);
            ramoAtividade.setConveniados(conveniadaExistente);
        }
        
        // Atualiza taxas
        if (conveniadosDTO.getTaxaConveniados() != null && !conveniadosDTO.getTaxaConveniados().isEmpty()) {
            // Marca taxas antigas como desatualizadas
            conveniadaExistente.getTaxaConveniados().forEach(t -> t.setDescStatusTaxaCon(StatusTaxaConv.DESATUALIZADA));
            
            // Adiciona nova taxa
            TaxaConveniados novaTaxa = new TaxaConveniados();
            novaTaxa.setTaxa(conveniadosDTO.getTaxaConveniados().get(0).getTaxa());
            novaTaxa.setDescStatusTaxaCon(StatusTaxaConv.ATUAL);
            novaTaxa.setConveniados(conveniadaExistente);
            conveniadaExistente.getTaxaConveniados().add(novaTaxa);
        }
        
        try {
            return conveniadosRepository.save(conveniadaExistente);
        } catch (Exception e) {
            logger.error("Erro ao atualizar conveniada", e);
            throw new ExceptionCustomizada("Erro ao atualizar conveniada: " + e.getMessage());
        }
    }
    

    // Método 1: Buscar todos os conveniados ordenados por ID (limit 10)
    public List<Conveniados> buscarTodosConveniadosOrdenadosPorId() {
    	Pageable topTen = PageRequest.of(0, 10);
        return conveniadosRepository.findTop10ByOrderByIdConveniados(topTen);
    }

    // Método 2: Buscar conveniados por parte do nome da pessoa (limit 10)
    public List<Conveniados> buscarConveniadosPorParteDoNomePessoa(String parteNome) {
    	Pageable topTen = PageRequest.of(0, 10);
        return conveniadosRepository.findTop10ByPessoaNomePessoaContainingIgnoreCase(parteNome, topTen);
    }

    // Versões com paginação
    public Page<Conveniados> buscarTodosConveniadosPaginados(int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return conveniadosRepository.findAllByOrderByIdConveniados(pageable);
    }
 

    public Page<Conveniados> buscarConveniadosPorParteDoNomePessoaPaginados(String parteNome, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by("idConveniados"));
        return conveniadosRepository.findByPessoaNomePessoaContainingIgnoreCase(parteNome, pageable);
    }
 }
