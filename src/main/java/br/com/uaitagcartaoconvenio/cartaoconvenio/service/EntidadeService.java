package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaCalcLimiteCredFuncionaro;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.EntidadeRespository;

@Service
public class EntidadeService {

	
	@Autowired
	private EntidadeRespository entidadeRespository;
	
	
	public Entidade salvarEntidadeService( Entidade entidade ) throws ExceptionCustomizada {
		
				
		if (entidade.getIdEntidade() == null && entidadeRespository.findByCnpj(entidade.getCnpj()) != null) {
			throw new ExceptionCustomizada("Já existe o CNPJ cadastrado: " + entidade.getCnpj() );
		}
		
		entidade.getTaxaEntidade().get(0).setEntidade(entidade);
		
		entidade.getTaxaEntidade().get(0).setStatusTaxaEntidade(StatusTaxaEntidade.ATUAL);
		
		entidade.getTaxaCalcLimiteCreditoFunc().get(0).setEntidade(entidade);
		
		entidade.getTaxaCalcLimiteCreditoFunc().get(0).setStatusTaxaCalcLimiteCredFuncionaro(StatusTaxaCalcLimiteCredFuncionaro.ATUAL);
		
		entidade.setFuncionario(null);
		
		entidade = entidadeRespository.saveAndFlush( entidade );
		
		return entidade;
		
	}
}
