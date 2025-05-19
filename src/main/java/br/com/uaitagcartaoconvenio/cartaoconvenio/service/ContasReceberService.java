package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.io.IOException;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.ItemTaxaExtraEntidadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.FechamentoEntContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContasReceberDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContatoWorkflowDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ItemTaxaExtraEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.WorkflowInformativoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ContasReceberRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ItemTaxaExtraEntidadeRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.BusinessException;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.EmailFechamentoException;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.EmailService;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;


@Service
public class ContasReceberService {

	@Autowired
	private ContasReceberRepository contasReceberRepository;
	
	@Autowired
	private ContasReceberMappingService mappingService; 

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private ItemTaxaExtraEntidadeRepository itemRepository;
	
	@Autowired
	private ItemTaxaExtraEntidadeMapper itemMapper;
	
	private static final Logger logger = LogManager.getLogger(ContasReceberService.class);


	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<ContasReceber> getContasReceberByAnoMes( String anoMes )  {
		
		List<ContasReceber> listaContasReceber = contasReceberRepository.listaContasReceberByAnoMes( anoMes );
		
		return listaContasReceber;		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<ContasReceber> getContasReceberByDtCriacao( String dtCriacaoIni, String dtCriacaoFim )  {
		
		String dtCriacaoIniFormat =  FuncoesUteis.validarEConverterData(dtCriacaoIni, "00:00:00");
		String dtCriacaoFimFormat =  FuncoesUteis.validarEConverterData(dtCriacaoFim, "23:59:59");
		
		List<ContasReceber> listaContasReceber = contasReceberRepository.listaContasReceberByDtCriacao( dtCriacaoIniFormat, dtCriacaoFimFormat );
		
		return listaContasReceber;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<ContasReceber> getContasReceberByDescStatusReceber( StatusReceber descStatusReceber )  {
		
		List<ContasReceber> listaContasReceber = contasReceberRepository.listaContasReceberByDescStatusReceber( descStatusReceber );
		
		return listaContasReceber;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<ContasReceber> getCicloPagamentoVendaByIdConveniados( Long idEntidade )  {
		
		List<ContasReceber> contasReceber = contasReceberRepository.listaContasReceberByIdEntidade( idEntidade );
		
		return contasReceber;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<ContasReceber> getContasReceberByNomeEntidade( String nomeEntidade )  {
		
		List<ContasReceber> listaContasReceber = contasReceberRepository.listaContasReceberByNomeEntidade( nomeEntidade );
		
		return listaContasReceber;
		
	}
	
	// @Transactional
	public List<ContasReceber> salvarListaGrande(List<ContasReceber> listaCiclos, String msn) {
	  try {	
			List<ContasReceber> listaCicloReceberVendaPrincipal = new ArrayList<ContasReceber>();
		    int batchSize = 50;
		    for (int i = 0; i < listaCiclos.size(); i += batchSize) {
		        List<ContasReceber> batch = listaCiclos.subList(i, Math.min(i + batchSize, listaCiclos.size()));
		        batch = contasReceberRepository.saveAll(batch);
		        listaCicloReceberVendaPrincipal.addAll(batch);
		        contasReceberRepository.flush(); // Libera a memória
		    }	    
		    return listaCicloReceberVendaPrincipal;
	  }catch (Exception e) {
	    	 msn = e.getMessage();
			 System.err.println(e.getMessage());
	    	 throw new BusinessException(
	    			    "Não foi possível processar o Fechamento do Ciclo para as Entidades!",
	    			    "Falha ao gerar Fechamento do Ciclo a Receber!")
	    			    .addDetail("Periódo", listaCiclos.get(0).getAnoMes())
	    			    .addDetail("Data Pagamento", listaCiclos.get(0).getDtPrevisaoRecebimento());
	  }
	}

    // @Transactional
    public List<ContasReceber> salvarListaCiclos(List<ContasReceber> listaCiclos, String msn) {
    
	     try {	
	        // Atualiza timestamps antes de salvar
	        listaCiclos.forEach(ciclo -> {
	            ciclo.setDtCriacao(Calendar.getInstance().getTime());
	            ciclo.setDtAlteracao(Calendar.getInstance().getTime());
	        });
	        return contasReceberRepository.saveAll(listaCiclos);
	     }catch (Exception e) {
	    	 msn = e.getMessage();
			System.err.println(e.getMessage());
	    	 throw new BusinessException(
	    			    "Não foi possível processar o Fechamento do Ciclo para as Entidades!",
	    			    "Falha ao gerar Fechamento do Ciclo a Receber!")
	    			    .addDetail("Periódo", listaCiclos.get(0).getAnoMes())
	    			    .addDetail("Data Pagamento", listaCiclos.get(0).getDtPrevisaoRecebimento());

		}
       
    }
    
    // Método para deletar uma lista de ciclos
    // @Transactional
    public void deletarListaCiclos(List<ContasReceber> listaCiclos) {
    	contasReceberRepository.deleteAll(listaCiclos);
    }

    // Método alternativo para deletar por IDs
    // @Transactional
    public void deletarListaCiclosPagamentoPorIds(List<Long> ids) {
    	contasReceberRepository.deleteAllRecebimentoByIdIn(ids);
    }
    
    // Método para deletar em lotes (melhor performance para listas grandes)
    // @Transactional
    public void deletarListaCiclosPagamentoEmLote(List<Long> ids) {
        int batchSize = 50;
        for (int i = 0; i < ids.size(); i += batchSize) {
            List<Long> batchIds = ids.subList(i, Math.min(i + batchSize, ids.size()));
            contasReceberRepository.deleteAllRecebimentoByIdIn(batchIds);
            contasReceberRepository.flush();
        }
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public Boolean existCicloFechamentoRecebimento( String anoMes )  {
		return contasReceberRepository.isExistCicloRecebimentoVenda( anoMes ) > 0 ? true : false;		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public int updateCancelamentoStatusCicloRecebimentoVenda( String anoMes )  {
		return contasReceberRepository.updateStatusCicloRecebimentoVenda( anoMes );		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    public List<ContasReceberDTO> processarUpload(MultipartFile[] files, Long idCicloPgVenda) {
        return Arrays.stream(files)
                .map(file -> processarArquivo(file, idCicloPgVenda))
                .collect(Collectors.toList());
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/
    @Transactional
    private ContasReceberDTO processarArquivo(MultipartFile file, Long idCicloReVenda) {
        try {
            // Verificar se é PDF
            if (!"application/pdf".equals(file.getContentType())) {
                throw new RuntimeException("Apenas arquivos PDF são permitidos");
            }

            // Converter para Base64
            String base64 = Base64.getEncoder().encodeToString(file.getBytes());

            // Buscar a entidade existente
//            ContasReceber contasReceber = contasReceberRepository.findById(idCicloReVenda)
//                    .orElseThrow(() -> new RuntimeException("Ciclo de Pagamento não encontrado com ID: " + idCicloReVenda));
            ContasReceber contasReceber = contasReceberRepository.findByIdWithFechamentos(idCicloReVenda)
            	    .orElseThrow(() -> new RuntimeException("Ciclo de Recebimento não encontrado com ID: " + idCicloReVenda));

            // Atualizar os campos do arquivo
            contasReceber.setNomeArquivo   ( file.getOriginalFilename()       );
            contasReceber.setConteudoBase64( base64                           );
            contasReceber.setTamanhoBytes  ( file.getSize()                   );
            contasReceber.setDataUpload    ( Calendar.getInstance().getTime() );

            // Salvar no banco
            ContasReceber saved = contasReceberRepository.save(contasReceber);
            // Forçar inicialização
            // Hibernate.initialize(saved.getFechamentoEntContasReceber());

            // Atualiza Status para 'RECEBIMENTO_APROVADO' ==> Aguardando o pagamento pela Entidade.
            contasReceberRepository.updateStatusCicloRecebimentoAprovado( saved.getIdContasReceber() );

            // Após finalizar o processo de upload, envia email para informar a UaiTag.
            enviaEmailNF( saved );
                        
            // Converter para DTO
            return mappingService.mapComplete(saved);
            
        } catch (IOException e) {
            throw new RuntimeException("Falha ao processar arquivo: " + file.getOriginalFilename(), e);
        }
    }
    
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    public void registrarRecebimento(Long idContasReceber, String novaObservacao, String doc, String dtPagamento) {
    	
    	if( !contasReceberRepository.isStatusCicloFinalizarFechamentoOK(idContasReceber) ) new RuntimeException("Ciclo não pode ser concluido. Favor verificar o Status/NF anexada... ID(" + idContasReceber + ")");
    	
        // Adiciona quebra de linha e data/hora antes da nova observação
        String observacaoFormatada = String.format("\n[%s] %s", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), novaObservacao);
       
        // Atualiza status para conta recebida.
        int updated = contasReceberRepository.atualizarRecebimento(idContasReceber, observacaoFormatada, StatusReceber.RECEBIDO, doc, FuncoesUteis.converterParaTimestamp(dtPagamento));
        
        if (updated == 0) {
            throw new EntityNotFoundException("Conta a Receber não encontrada com ID: " + idContasReceber);
        }
        
        // Atualiza o status dos itens da vendas.
        atualizarVendasParaFechamentoConcluido( contasReceberRepository.listaFechamentoEntContasReceberByIdCR( idContasReceber ) );
        
        // Busca a informação da conta a receber.
        ContasReceber contasReceber = contasReceberRepository.findByIdWithFechamentos(idContasReceber)
        	    .orElseThrow(() -> new RuntimeException("Ciclo de Pagamento não encontrado com ID: " + idContasReceber));

        // Envia e-mail de aviso de conta recebida.
        enviaEmailRecebimento( contasReceber, doc, dtPagamento );

    }

	/******************************************************************/
	/* Atualiza as vendas para o status com fechamento concluido,     */
	/* e indica que foi concluido com o recebimento dos valores       */
	/*                                                                */
	/******************************************************************/	
    public int atualizarVendasParaFechamentoConcluido(List<FechamentoEntContasReceber> lFEC ) {
    	List<Long> idsVendas =  new ArrayList<Long>();
    	
    	for( FechamentoEntContasReceber fec : lFEC ) {
    		Long id = fec.getVenda().getIdVenda();
    		idsVendas.add(id);
    	}
    	
    	return vendaService.atualizarVendasFechamentoConcluidoRecebimento( idsVendas );
        
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    private String enviaEmailNF( ContasReceber cr ) {
    	
    	 List<Map<String, String>> result = new ArrayList<>();
    	 result.add(createRowMapCR(cr));
    	 
 		 List<String> emails = new ArrayList<String>();
 		 // Pega a lista de e-mail das pessoas a serem informadas do recebimento.
 		 WorkflowInformativoDTO wi  = workflowService.buscarPorId( 1L ); // Esta usando a lista de fechamento de ciclo como teste. Criar uma para o informatifo de anexo de nota fiscal
		
 		 for(ContatoWorkflowDTO ck : wi.getContatoWorkflow()) {
 			String email = ck.getNomeContato() + "<" + ck.getEmail() + ">";
 			emails.add(email);
 		 }
    	 
 		try {
    	 emailService.enviarEmailAnexoNF(	"Entidade", emails, cr.getAnoMes(), cr.getEntidade().getNomeEntidade(), result);
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
	    	 throw new BusinessException( "Não foi possível enviar o e-mail de fechamento de Recebimento: ",e.getMessage());

		}		  	 
		return "ENVIO_EMAIL_OK";    	
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    private String enviaEmailRecebimento( ContasReceber cr, String doc, String dtPagamento ) {
    	
    	 List<Map<String, String>> result = new ArrayList<>();
    	 result.add(createRowMapRecebPG(cr, doc, dtPagamento));
    	 
 		 List<String> emails = new ArrayList<String>();

 		 // Pega a lista de e-mail das pessoas a serem informadas do recebimento.
 		 WorkflowInformativoDTO wi  = workflowService.buscarPorId( 1L ); // Esta usando a lista de fechamento de ciclo como teste. Criar uma para o informatifo de pagamento
		
 		 for(ContatoWorkflowDTO ck : wi.getContatoWorkflow()) {
 			String email = ck.getNomeContato() + "<" + ck.getEmail() + ">";
 			emails.add(email);
 		 }
    	 
 		try {
    	 emailService.enviarEmailConfRecebEnt(	emails, cr.getAnoMes(), cr.getEntidade().getNomeEntidade(), result);
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
   private Map<String, String> createRowMapCR( ContasReceber obj) {
        Map<String, String> row = new HashMap<>();
        
        // Formatação de valores monetários
        @SuppressWarnings("deprecation")
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        
        // Popula o Map com os valores formatados
        row.put("empresa"  , obj.getEntidade().getNomeEntidade()              ); 
        row.put("periodo"  , obj.getAnoMes()                                  );
        row.put("valor"    , currencyFormat.format( obj.getValorReceber()   ) );
        row.put("nomeArq"  , obj.getNomeArquivo()                             );
        row.put("dtAnexo"  , FuncoesUteis.dateToString( obj.getDataUpload() ) );
        
        return row;
    }

	/******************************************************************/
	/*                                                                */
	/*       Cria um Map representando uma linha da tabela            */
	/*                                                                */
	/******************************************************************/	
   private Map<String, String> createRowMapRecebPG( ContasReceber obj, String doc, String dtPagamento) {
       Map<String, String> row = new HashMap<>();
       
       // Formatação de valores monetários
       @SuppressWarnings("deprecation")
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
       
       // Popula o Map com os valores formatados
       row.put("empresa"  , obj.getEntidade().getNomeEntidade()              ); 
       row.put("periodo"  , obj.getAnoMes()                                  );
       row.put("valor"    , currencyFormat.format( obj.getValorReceber()   ) );
       row.put("docBanco" , doc                                              );
       row.put("dtPG"     , dtPagamento                                      );
       
       return row;
   }
   
   public List<ItemTaxaExtraEntidadeDTO> listarTaxasExtrasPorConta(Long contasReceberId) {
	    return itemMapper.toDTOList(itemRepository.findByContasReceberId(contasReceberId));
	}
}
