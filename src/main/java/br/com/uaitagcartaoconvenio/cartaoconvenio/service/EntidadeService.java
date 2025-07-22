package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusEmtidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaCalcLimiteCredFuncionaro;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ArqContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ServicoContrato;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.EntidadeRespository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;

@Service
public class EntidadeService {

	
	@Autowired
	private EntidadeRespository entidadeRespository;
		
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/**
	 * @throws IOException ****************************************************************/	
	public Entidade salvarEntidadeService( Entidade entidade ) throws ExceptionCustomizada, IOException {
		
		
		String cnpj = FuncoesUteis.removerCaracteresNaoNumericos(entidade.getCnpj());
		entidade.setCnpj(cnpj);
				
		if (entidade.getIdEntidade() == null && entidadeRespository.findByCnpj(entidade.getCnpj()) != null) {
			throw new ExceptionCustomizada("Já existe o CNPJ cadastrado: " + entidade.getCnpj() );
		}
		
		entidade.getTaxaEntidade().get(0).setEntidade(entidade);
		
		entidade.getTaxaEntidade().get(0).setStatusTaxaEntidade(StatusTaxaEntidade.ATUAL);
		
		entidade.getTaxaCalcLimiteCreditoFunc().get(0).setEntidade(entidade);
		
		entidade.getTaxaCalcLimiteCreditoFunc().get(0).setStatusTaxaCalcLimiteCredFuncionaro(StatusTaxaCalcLimiteCredFuncionaro.ATUAL);
		
		// Transforma o arquivo em base64.
		for( ContratoEntidade contEnt : entidade.getContratoEntidade() ) {
			for( ArqContratoEntidade arqEnt : contEnt.getArquivos() ) {
                /*
				File arquivo = new File(arqEnt.getCaminhoArquivo());
				
			    if (!arquivo.exists() || !arquivo.isFile()) {
			        throw new IOException("Arquivo inválido ou não encontrado");
			    }
			    
			    // Ler o arquivo manualmente
			    byte[] fileContent = new byte[(int) arquivo.length()];
			    try (FileInputStream fis = new FileInputStream(arquivo)) {
			        fis.read(fileContent);
			    }
			    String base64 = Base64.getEncoder().encodeToString(fileContent);
			    Integer tamanho = (int) arquivo.length();
			    arqEnt.setArqContrato(arquivo.getName());
			    arqEnt.setConteudoBase64(base64);
			    arqEnt.setTamanhoBytes(tamanho);
			    arqEnt.setContratoEntidade(contEnt);
			    */
			    arqEnt.setContratoEntidade(contEnt);
			}
			contEnt.setEntidade(entidade);
		}
		
		for( ContratoEntidade contEntidate : entidade.getContratoEntidade() ) {
			for( VigenciaContratoEntidade vigencia : contEntidate.getVigencias() ) {
				 vigencia.setContratoEntidade(contEntidate);
			}
			for( ServicoContrato servico : contEntidate.getServicos() ) {
				 servico.setContratoEntidade(contEntidate);
			}
		}
		
		entidade.setListaFuncionario(null);
		
		entidade = entidadeRespository.saveAndFlush( entidade );
		
		return entidade;
		
	}

	/*

           // Converter para Base64
            String base64 = Base64.getEncoder().encodeToString(file.getBytes());
	            
	            // Atualizar os campos do arquivo
            cicloPgVenda.setNomeArquivo   ( file.getOriginalFilename()       );
            cicloPgVenda.setConteudoBase64( base64                           );
            cicloPgVenda.setTamanhoBytes  ( file.getSize()                   );
            cicloPgVenda.setDataUpload    ( Calendar.getInstance().getTime() );
 
	 */
	
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Entidade> getAllEntidades( )  {
		
		List<Entidade> listaAllEntidades = entidadeRespository.listaTodasEntidade();
		
		return listaAllEntidades;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public Entidade getEntidadesCnpj( String cnpj )  {
		
		String resultCnpj = FuncoesUteis.removerCaracteresNaoNumericos( cnpj );
		
		Entidade listaAllEntidade = entidadeRespository.findByCnpj( resultCnpj );
		
		return listaAllEntidade;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Entidade> findEntidadeNome( String nomeEntidade )  {
		
		List<Entidade> listaEntidades = entidadeRespository.findEntidadeNome( nomeEntidade );
		
		return listaEntidades;
		
	}	

	/********************************************************************/
	/*                                                                  */
	/*  Método 1: Buscar todas as entidades ordenadas por ID (limit 10) */
	/*                                                                  */
	/********************************************************************/	
    public List<Entidade> buscarTodasEntidadesOrdenadasPorId() {
        return entidadeRespository.findTop10ByOrderByIdEntidade();
    }

	/******************************************************************/
	/*                                                                */
	/*  Método 2: Buscar entidades por parte do nome (limit 10)       */
	/*                                                                */
	/******************************************************************/	
    public List<Entidade> buscarEntidadesPorParteDoNome(String parteNome) {
        return entidadeRespository.findTop10ByNomeEntidadeContainingIgnoreCaseOrderByIdEntidade(parteNome);
    }

	/******************************************************************/
	/*                                                                */
	/*  Versões com paginação (mais flexíveis)                        */
	/*                                                                */
	/******************************************************************/	
    public Page<Entidade> buscarTodasEntidadesPaginadas(int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return entidadeRespository.findAllByOrderByIdEntidade(pageable);
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    public Page<Entidade> buscarEntidadesPorParteDoNomePaginadas(String parteNome, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return entidadeRespository.findByNomeEntidadeContainingIgnoreCaseOrderByIdEntidade(parteNome, pageable);
    }	
	
	
	
	
	
		
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public Entidade getEntidadesId( Long id )  {
				
		Entidade entidade = entidadeRespository.findByIdEntidade( id );
		
		return entidade;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Entidade> listaEntidadeByCidade( String cidade )  {
		
		List<Entidade> listaEntidades = entidadeRespository.listaEntidadeByCidade( cidade) ;
		
		return listaEntidades;
		
	}	

	public Entidade findByIdEntity( Long id )  {
		return entidadeRespository.findById(id).orElseThrow(() -> new RuntimeException("Entidade nã0 encontrado para o ID: " + id));
		
	}
	
    public void atualizarAnoMesRecebimentoPosFechamento(Long idEntidade, String mesRecebimento) {
    	entidadeRespository.updateMesRecebimentoPosFechamento(idEntidade, mesRecebimento);
    }
    
    // Método 1: Atualização direta via query em lote
    public int atualizarAnoMesRecebimentoPosFechamentoEmLote(List<Long> ids, String mesRecebimento) {
        return entidadeRespository.updateMesRecebimentoPosFechamentoEmLote(ids, mesRecebimento);
    }

    // Método 2: Atualização via entidade em lote (chama os callbacks @PreUpdate)
     public List<Entidade> atualizarAnoMesRecebimentoEmLote(List<Long> ids, String mesRecebimento) {
        List<Entidade> entidades = entidadeRespository.findAllById(ids);
        if (entidades.size() != ids.size()) {
            throw new RuntimeException("Alguns IDs não foram encontrados");
        }
        
        entidades.forEach(entidade -> 
            entidade.setAnoMesUltinoFechamento(mesRecebimento));
        
        return entidadeRespository.saveAll(entidades);
    }
     
     /******************************************************************/
     /*                                                                */
     /*               Métodos para Atualização                         */
     /*                                                                */
     /******************************************************************/

     /**
      * Atualiza uma entidade existente
      */
     public Entidade atualizarEntidadeCompleta(Entidade entidadeAtualizada) throws ExceptionCustomizada, IOException {
         // Validações básicas
         if (entidadeAtualizada == null) {
             throw new ExceptionCustomizada("Entidade não pode ser nula");
         }
         
         if (entidadeAtualizada.getIdEntidade() == null) {
             throw new ExceptionCustomizada("ID da entidade é obrigatório para atualização");
         }
         
         // Verifica se a entidade existe
         Entidade entidadeExistente = entidadeRespository.findById(entidadeAtualizada.getIdEntidade())
             .orElseThrow(() -> new ExceptionCustomizada("Entidade não encontrada com ID: " + entidadeAtualizada.getIdEntidade()));
         
         // Mantém alguns campos que não devem ser atualizados
         entidadeAtualizada.setDtCriacao(entidadeExistente.getDtCriacao());
         
         // Processa o CNPJ
         String cnpj = FuncoesUteis.removerCaracteresNaoNumericos(entidadeAtualizada.getCnpj());
         entidadeAtualizada.setCnpj(cnpj);
         
         // Valida CNPJ único (se foi alterado)
         if (!entidadeExistente.getCnpj().equals(cnpj) ){
             if (entidadeRespository.findByCnpj(cnpj) != null) {
                 throw new ExceptionCustomizada("Já existe uma entidade com o CNPJ: " + cnpj);
             }
         }
         
         // Atualiza relacionamentos
         atualizarRelacionamentos(entidadeAtualizada);
         
         // Salva a entidade atualizada
         return entidadeRespository.saveAndFlush(entidadeAtualizada);
     }

     /**
      * Atualiza apenas o status de uma entidade
      */
     public Entidade atualizarStatusEntidade(Long id, StatusEmtidade novoStatus) throws ExceptionCustomizada {
         // Busca a entidade
         Entidade entidade = entidadeRespository.findById(id)
             .orElseThrow(() -> new ExceptionCustomizada("Entidade não encontrada com ID: " + id));
         
         // Atualiza o status
         entidade.setDescStatusEmtidade(novoStatus);
         
         // Salva a entidade
         return entidadeRespository.saveAndFlush(entidade);
     }

     /**
      * Método auxiliar para atualizar relacionamentos
      */
     private void atualizarRelacionamentos(Entidade entidade) {
         // Taxa Entidade
         if (entidade.getTaxaEntidade() != null) {
             entidade.getTaxaEntidade().forEach(taxa -> {
                 taxa.setEntidade(entidade);
                 taxa.setStatusTaxaEntidade(StatusTaxaEntidade.ATUAL);
             });
         }
         
         // Taxa Cálculo Limite
         if (entidade.getTaxaCalcLimiteCreditoFunc() != null) {
             entidade.getTaxaCalcLimiteCreditoFunc().forEach(taxa -> {
                 taxa.setEntidade(entidade);
                 taxa.setStatusTaxaCalcLimiteCredFuncionaro(StatusTaxaCalcLimiteCredFuncionaro.ATUAL);
             });
         }
         
         // Contratos
         if (entidade.getContratoEntidade() != null) {
             for (ContratoEntidade contrato : entidade.getContratoEntidade()) {
                 contrato.setEntidade(entidade);
                 
                 // Arquivos
                 if (contrato.getArquivos() != null) {
                     for (ArqContratoEntidade arquivo : contrato.getArquivos()) {
                         arquivo.setContratoEntidade(contrato);
                     }
                 }
                 
                 // Vigencias
                 if (contrato.getVigencias() != null) {
                     for (VigenciaContratoEntidade vigencia : contrato.getVigencias()) {
                         vigencia.setContratoEntidade(contrato);
                     }
                 }
                 
                 // Serviços
                 if (contrato.getServicos() != null) {
                     for (ServicoContrato servico : contrato.getServicos()) {
                         servico.setContratoEntidade(contrato);
                     }
                 }
             }
         }
     }
}
