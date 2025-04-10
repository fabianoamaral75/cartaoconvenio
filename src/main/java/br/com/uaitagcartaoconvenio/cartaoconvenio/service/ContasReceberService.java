package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ContasReceberRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.BusinessException;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;

@Service
public class ContasReceberService {

	@Autowired
	private ContasReceberRepository contasReceberRepository;

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

	
}
