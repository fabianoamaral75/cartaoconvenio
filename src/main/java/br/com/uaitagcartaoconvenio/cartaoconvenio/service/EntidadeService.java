package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaCalcLimiteCredFuncionaro;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.EntidadeRespository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;

@Service
public class EntidadeService {

	
	@Autowired
	private EntidadeRespository entidadeRespository;
		
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public Entidade salvarEntidadeService( Entidade entidade ) throws ExceptionCustomizada {
		
				
		if (entidade.getIdEntidade() == null && entidadeRespository.findByCnpj(entidade.getCnpj()) != null) {
			throw new ExceptionCustomizada("Já existe o CNPJ cadastrado: " + entidade.getCnpj() );
		}
		
		String cnpj = FuncoesUteis.removerCaracteresNaoNumericos(entidade.getCnpj());
		entidade.setCnpj(cnpj);
		entidade.getTaxaEntidade().get(0).setEntidade(entidade);
		
		entidade.getTaxaEntidade().get(0).setStatusTaxaEntidade(StatusTaxaEntidade.ATUAL);
		
		entidade.getTaxaCalcLimiteCreditoFunc().get(0).setEntidade(entidade);
		
		entidade.getTaxaCalcLimiteCreditoFunc().get(0).setStatusTaxaCalcLimiteCredFuncionaro(StatusTaxaCalcLimiteCredFuncionaro.ATUAL);
		
		entidade.setListaFuncionario(null);
		
		entidade = entidadeRespository.saveAndFlush( entidade );
		
		return entidade;
		
	}
	
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

}
