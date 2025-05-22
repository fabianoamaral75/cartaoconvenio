package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
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
	

}
