package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.CicloPagamentoVendaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.CicloTaxaExtraMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloTaxaExtra;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.FechamentoConvItensVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.CicloPagamentoVendaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.CicloTaxaExtraDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContatoWorkflowDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.WorkflowInformativoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.CicloPagamentoVendaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.CicloTaxaExtraRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaExtraConveniadaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.BusinessException;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.EmailFechamentoException;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.EmailService;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CicloPagamentoVendaService {

	@Autowired
	private CicloPagamentoVendaRepository cicloPagamentoVendaRepository;
	
	@Autowired
	private CicloPagamentoVendaMapper cicloPagamentoVendaMapper;
	
	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private TaxaExtraConveniadaRepository taxaExtraConveniadaRepository;
	
	@Autowired
	private CicloTaxaExtraRepository cicloTaxaExtraRepository;
	
	@Autowired
	private CicloTaxaExtraMapper cicloTaxaExtraMapper;

	
	private static final Logger logger = LogManager.getLogger(ContasReceberService.class);
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<CicloPagamentoVenda> getCicloPagamentoVendaByAnoMes( String anoMes )  {
		
		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaRepository.listaCicloPagamentoVendaByAnoMes( anoMes );
		
		return listaCicloPagamentoVenda;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<CicloPagamentoVenda> getCicloPagamentoVendaByAnoMesStatus( String anoMes, StatusCicloPgVenda descStatusPagamento )  {
		
		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaRepository.listaCicloPagamentoVendaByAnoMesStatus( anoMes, descStatusPagamento );
		
		return listaCicloPagamentoVenda;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<CicloPagamentoVenda> getCicloPagamentoVendaByDtCriacao( String dtCriacaoIni, String dtCriacaoFim )  {
		
		String dtCriacaoIniFormat =  FuncoesUteis.validarEConverterData(dtCriacaoIni, "00:00:00");
		String dtCriacaoFimFormat =  FuncoesUteis.validarEConverterData(dtCriacaoFim, "23:59:59");
		
		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaRepository.listaCicloPagamentoVendaByDtCriacao( dtCriacaoIniFormat, dtCriacaoFimFormat );
		
		return listaCicloPagamentoVenda;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<CicloPagamentoVenda> getCicloPagamentoVendaByDescStatusPagamento( StatusCicloPgVenda descStatusPagamento )  {
		
		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaRepository.listaCicloPagamentoVendaByDescStatusPagamento( descStatusPagamento );
		
		return listaCicloPagamentoVenda;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public CicloPagamentoVenda getCicloPagamentoVendaByIdConveniados( Long idConveniados )  {
		
		CicloPagamentoVenda listaCicloPagamentoVenda = cicloPagamentoVendaRepository.listaCicloPagamentoVendaByIdConveniados( idConveniados );
		
		return listaCicloPagamentoVenda;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<CicloPagamentoVenda> getCicloPagamentoVendaByNomeConveniado( String nomeConveniado )  {
		
		List<CicloPagamentoVenda> listaCicloPagamentoVenda = cicloPagamentoVendaRepository.listaCicloPagamentoVendaByNomeConveniado( nomeConveniado );
		
		return listaCicloPagamentoVenda;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    public List<CicloPagamentoVendaDTO> processarUpload(MultipartFile[] files, Long idCicloPgVenda) {
        return Arrays.stream(files)
                .map(file -> processarArquivo(file, idCicloPgVenda))
                .collect(Collectors.toList());
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    private CicloPagamentoVendaDTO processarArquivo(MultipartFile file, Long idCicloPgVenda) {
        try {
            // Verificar se é PDF
            if (!"application/pdf".equals(file.getContentType())) {
                throw new RuntimeException("Apenas arquivos PDF são permitidos");
            }

            // Converter para Base64
            String base64 = Base64.getEncoder().encodeToString(file.getBytes());

            // Buscar as informações das contas a serem pagas
            CicloPagamentoVenda cicloPgVenda = cicloPagamentoVendaRepository.findByIdWithFechamentosPagamentos(idCicloPgVenda)
            	    .orElseThrow(() -> new RuntimeException("Ciclo de Pagamento não encontrado com ID: " + idCicloPgVenda));

            // Atualizar os campos do arquivo
            cicloPgVenda.setNomeArquivo   ( file.getOriginalFilename()       );
            cicloPgVenda.setConteudoBase64( base64                           );
            cicloPgVenda.setTamanhoBytes  ( file.getSize()                   );
            cicloPgVenda.setDataUpload    ( Calendar.getInstance().getTime() );

            // Salvar no banco
            CicloPagamentoVenda saved = cicloPagamentoVendaRepository.save(cicloPgVenda);
            
            // Atualiza Status para 'PAGAMENTO_APROVADO' ==> Aguardando a realização do pagament a Conveniada.
            cicloPagamentoVendaRepository.updateStatusCicloPagamentoAprovado( saved.getIdCicloPagamentoVenda() );
            
            // Após finalizar o processo de upload da NF pela Conveniada, envia email informativo a UaiTag.
            enviaEmailNF( saved );

            // Converter para DTO
            return cicloPagamentoVendaMapper.toDTO(saved);
            
        } catch (IOException e) {
            throw new RuntimeException("Falha ao processar arquivo: " + file.getOriginalFilename(), e);
        }
    }
    
    
    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
     private String enviaEmailNF( CicloPagamentoVenda cp ) {
         List<Map<String, String>> result = new ArrayList<>();
         
         result.add(createRowMapCR(cp));
         
         List<String> emails = new ArrayList<String>();
         // Pega a lista de e-mail das pessoas a serem informadas do recebimento.
         WorkflowInformativoDTO wi  = workflowService.buscarPorId( 1L ); // Esta usando a lista de fechamento de ciclo como teste. Criar uma para o informatifo de anexo de nota fiscal
         
         for(ContatoWorkflowDTO ck : wi.getContatoWorkflow()) {
             String email = ck.getNomeContato() + "<" + ck.getEmail() + ">";
             emails.add(email);
         }
         
         try {
         	 emailService.enviarEmailAnexoNF(	"Conveniado", emails, cp.getAnoMes(), cp.getConveniados().getPessoa().getNomePessoa(), result);
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
             // throw new BusinessException("Não foi possível enviar o e-mail de fechamento", e);
             throw new BusinessException( "Não foi possível enviar o e-mail de fechamento Pagamento: ",e.getMessage());
         
         }
         return "ENVIO_EMAIL_OK";
     }

 	/******************************************************************/
 	/*                                                                */
 	/*       Cria um Map representando uma linha da tabela            */
 	/*                                                                */
 	/******************************************************************/	
    private Map<String, String> createRowMapCR( CicloPagamentoVenda obj) {
         Map<String, String> row = new HashMap<>();
         
         // Formatação de valores monetários
         @SuppressWarnings("deprecation")
 		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
         
         // Popula o Map com os valores formatados
         row.put("empresa"  , obj.getConveniados().getPessoa().getNomePessoa() ); 
         row.put("periodo"  , obj.getAnoMes()                                  );
         row.put("valor"    , currencyFormat.format( obj.getValorCiclo()     ) );
         row.put("nomeArq"  , obj.getNomeArquivo()                             );
         row.put("dtAnexo"  , FuncoesUteis.dateToString( obj.getDataUpload() ) );
         
         return row;
     }
    
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/
    @Transactional
    public void registrarPagamento(Long idContasPagamento, String novaObservacao, String doc, String dtPagamento) {

        if( !cicloPagamentoVendaRepository.isStatusCicloFinalizarFechamentoOK(idContasPagamento) ) new RuntimeException("Ciclo não pode ser concluido. Favor verificar o Status/NF anexada... ID(" + idContasPagamento + ")");
      
         // Adiciona quebra de linha e data/hora antes da nova observação
        String observacaoFormatada = String.format("\n[%s] %s", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), novaObservacao);
       
        // Atualiza status para conta recebida.
        int updated = cicloPagamentoVendaRepository.atualizarPagamento(idContasPagamento, observacaoFormatada, StatusCicloPgVenda.PAGAMENTO, doc, FuncoesUteis.converterParaTimestamp(dtPagamento));
        
        if (updated == 0) {
            throw new EntityNotFoundException("Pagamento não encontrada com ID: " + idContasPagamento);
        }
        
        // Atualiza o status dos itens da vendas.
        atualizarVendasParaFechamentoConcluido( cicloPagamentoVendaRepository.listaCicloPagamentoVendaByIdCR( idContasPagamento ) );
        
        // Buscar as informações das contas a serem pagas
        CicloPagamentoVenda cicloPgVenda = cicloPagamentoVendaRepository.findByIdWithFechamentosPagamentos(idContasPagamento)
        	    .orElseThrow(() -> new RuntimeException("Ciclo de Pagamento não encontrado com ID: " + idContasPagamento));

        // Envia e-mail de aviso de conta recebida.
        enviaEmailPagamento( cicloPgVenda, doc, dtPagamento );

    }
    
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @Async
    private String enviaEmailPagamento( CicloPagamentoVenda cp, String doc, String dtPagamento ) {
    	
    	 List<Map<String, String>> result = new ArrayList<>();
    	 result.add(createRowMapRecebPG(cp, doc, dtPagamento));
    	 
 		 List<String> emails = new ArrayList<String>();

 		 // Pega a lista de e-mail das pessoas a serem informadas do recebimento.
 		 WorkflowInformativoDTO wi  = workflowService.buscarPorId( 1L ); // Esta usando a lista de fechamento de ciclo como teste. Criar uma para o informatifo de pagamento
		
 		 for(ContatoWorkflowDTO ck : wi.getContatoWorkflow()) {
 			String email = ck.getNomeContato() + "<" + ck.getEmail() + ">";
 			emails.add(email);
 		 }
    	 
 		try {
    	 emailService.enviarEmailConfPagamento(	emails, cp.getAnoMes(), cp.getConveniados().getPessoa().getNomePessoa(), result);
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
	/*       Cria um Map representando uma linha da tabela            */
	/*                                                                */
	/******************************************************************/	
   private Map<String, String> createRowMapRecebPG( CicloPagamentoVenda obj, String doc, String dtPagamento) {
       Map<String, String> row = new HashMap<>();
       
       // Formatação de valores monetários
       @SuppressWarnings("deprecation")
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
       
       // Popula o Map com os valores formatados
       row.put("empresa"  , obj.getConveniados().getPessoa().getNomePessoa() ); 
       row.put("periodo"  , obj.getAnoMes()                                  );
       row.put("valor"    , currencyFormat.format( obj.getValorCiclo()   )   );
       row.put("docBanco" , doc                                              );
       row.put("dtPG"     , dtPagamento                                      );
       
       return row;
   }


	/******************************************************************/
	/* Atualiza as vendas para o status com fechamento concluido,     */
	/* e indica que foi concluido com o recebimento dos valores       */
	/*                                                                */
	/******************************************************************/	
    public int atualizarVendasParaFechamentoConcluido(List<FechamentoConvItensVendas> lfciv ) {
    	List<Long> idsVendas =  new ArrayList<Long>();
    	
    	for( FechamentoConvItensVendas fciv : lfciv ) {
    		Long id = fciv.getVenda().getIdVenda();
    		idsVendas.add(id);
    	}
    	
    	return vendaService.atualizarVendasFechamentoConcluidoPG( idsVendas );
        
    }

    
	// @Transactional
	public List<CicloPagamentoVenda> salvarListaGrande(List<CicloPagamentoVenda> listaCiclos, String msn) {
    	try {
			List<CicloPagamentoVenda> listaCicloPagamentoVendaPrincipal = new ArrayList<CicloPagamentoVenda>();
		    int batchSize = 50;
		    for (int i = 0; i < listaCiclos.size(); i += batchSize) {
		        List<CicloPagamentoVenda> batch = listaCiclos.subList(i, Math.min(i + batchSize, listaCiclos.size()));
		        batch = cicloPagamentoVendaRepository.saveAll(batch);
		        listaCicloPagamentoVendaPrincipal.addAll(batch);
		        cicloPagamentoVendaRepository.flush(); // Libera a memória
		    }
		    
		    return listaCicloPagamentoVendaPrincipal;
	     }catch (Exception e) {
	    	 msn = e.getMessage();
			System.err.println(e.getMessage());
	    	 throw new BusinessException(
	    			    "Não foi possível processar o Fechamento do Ciclo para as Entidades!",
	    			    "Falha ao gerar Fechamento do Ciclo a Receber!")
	    			    .addDetail("Periódo", listaCiclos.get(0).getAnoMes())
	    			    .addDetail("Data Pagamento", listaCiclos.get(0).getDtAlteracao());

		 }
	}

 //   @Transactional
    public List<CicloPagamentoVenda> salvarListaCiclos(List<CicloPagamentoVenda> listaCiclos, String msn) {
        // Atualiza timestamps antes de salvar
        listaCiclos.forEach(ciclo -> {
            ciclo.setDtCriacao(Calendar.getInstance().getTime());
            ciclo.setDtAlteracao(Calendar.getInstance().getTime());
        });
        
    	try {
	        
           return cicloPagamentoVendaRepository.saveAll(listaCiclos);
	     }catch (Exception e) {
	    	 System.err.println(e.getMessage());
//	    	 throw new BusinessException("Já existe um ciclo com este status. Detalhes: " + e.getMessage());
	    	 throw new BusinessException(
	    			    "Não foi possível processar o Fechamento do Ciclo para as Conveniadas!",
	    			    "Falha ao gerar Fechamento do Ciclo a Pagar!")
	    			    .addDetail("Periódo", listaCiclos.get(0).getAnoMes())
	    			    .addDetail("Data Pagamento", listaCiclos.get(0).getDtPagamento());
		 }
    }
    
    // Método para deletar uma lista de ciclos
    // @Transactional
    public void deletarListaCiclos(List<CicloPagamentoVenda> listaCiclos) {
        cicloPagamentoVendaRepository.deleteAll(listaCiclos);
    }
    
    // Método alternativo para deletar por IDs
    // @Transactional
    public void deletarListaCiclosPagamentoPorIds(List<Long> ids) {
        cicloPagamentoVendaRepository.deleteAllByPagamentoIdIn(ids);
    }
    
    // Método para deletar em lotes (melhor performance para listas grandes)
    // @Transactional
    public void deletarListaCiclosPagamentoEmLote(List<Long> ids) {
        int batchSize = 50;
        for (int i = 0; i < ids.size(); i += batchSize) {
            List<Long> batchIds = ids.subList(i, Math.min(i + batchSize, ids.size()));
            cicloPagamentoVendaRepository.deleteAllByPagamentoIdIn(batchIds);
            cicloPagamentoVendaRepository.flush();
        }
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public Boolean existCicloFechamentoPagamento( String anoMes )  {
		return cicloPagamentoVendaRepository.isExistCicloPagamentoVenda( anoMes ) > 0 ? true : false;		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public int updateCancelamentoStatusCicloPagamentoVenda( String anoMes )  {
		return cicloPagamentoVendaRepository.updateStatusCicloPagamentoVenda( anoMes );		
	}

	/******************************************************************/
	/*                                                                */
	/*            Exemplo de Uso no Service                           */
	/*                                                                */
	/******************************************************************/	
	public CicloTaxaExtraDTO associarTaxaExtraACiclo(Long cicloId, Long taxaId, BigDecimal valorTaxaExtra) {
	    CicloPagamentoVenda ciclo = cicloPagamentoVendaRepository.findById(cicloId)
	            .orElseThrow(() -> new EntityNotFoundException("Ciclo não encontrado"));
	    TaxaExtraConveniada taxa = taxaExtraConveniadaRepository.findById(taxaId)
	            .orElseThrow(() -> new EntityNotFoundException("Taxa extra não encontrada"));
	    
	    CicloTaxaExtra cicloTaxaExtra = CicloTaxaExtra.builder()
	            .cicloPagamentoVenda(ciclo)
	            .taxaExtraConveniada(taxa)
	            .valorTaxaExtra(valorTaxaExtra)
	            .build();
	    
	    cicloTaxaExtra = cicloTaxaExtraRepository.save(cicloTaxaExtra);
//	    return CicloTaxaExtraMapper.INSTANCE.toDTO(cicloTaxaExtra);
	    return cicloTaxaExtraMapper.toDTO(cicloTaxaExtra); 
	}
}
