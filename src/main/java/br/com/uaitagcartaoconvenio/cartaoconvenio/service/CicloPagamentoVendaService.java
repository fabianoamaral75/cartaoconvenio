package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.CicloPagamentoVendaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.CicloPagamentoVendaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.CicloPagamentoVendaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.BusinessException;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;

@Service
public class CicloPagamentoVendaService {

	@Autowired
	private CicloPagamentoVendaRepository cicloPagamentoVendaRepository;
	
	@Autowired
	private CicloPagamentoVendaMapper cicloPagamentoVendaMapper;
	
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

            // Buscar a entidade existente
            CicloPagamentoVenda cicloPgVenda = cicloPagamentoVendaRepository.findById(idCicloPgVenda)
                    .orElseThrow(() -> new RuntimeException("Ciclo de Pagamento não encontrado com ID: " + idCicloPgVenda));

            // Atualizar os campos do arquivo
            cicloPgVenda.setNomeArquivo(file.getOriginalFilename());
            cicloPgVenda.setConteudoBase64(base64);
            cicloPgVenda.setTamanhoBytes(file.getSize());
            cicloPgVenda.setDataUpload(Calendar.getInstance().getTime());

            // Salvar no banco
            CicloPagamentoVenda saved = cicloPagamentoVendaRepository.save(cicloPgVenda);

            // Converter para DTO
            return cicloPagamentoVendaMapper.toDTO(saved);
            
        } catch (IOException e) {
            throw new RuntimeException("Falha ao processar arquivo: " + file.getOriginalFilename(), e);
        }
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
   	
}
