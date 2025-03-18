package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaEntidadeRepository;

@Service
public class TaxaEntidadeService {

	@Autowired
	private TaxaEntidadeRepository taxaEntidadeRepository;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<TaxaEntidade> getListaTaxaEntidadeByStatusTaxaEntidade( StatusTaxaEntidade statusTaxaEntidade )  {
		
		List<TaxaEntidade> listaTaxaEntidade = taxaEntidadeRepository.listaTaxaEntidadeByStatusTaxaEntidade( statusTaxaEntidade );
		
		return listaTaxaEntidade;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<TaxaEntidade> getTaxaEntidadeByIdEntidade( Long idEntidade )  {
		
		List<TaxaEntidade> listaTaxaEntidade = taxaEntidadeRepository.taxaEntidadeByIdEntidade( idEntidade );
		
		return listaTaxaEntidade;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public TaxaEntidade getTaxaEntidadeAtualByIdEntidade( Long idEntidade )  {
		
		TaxaEntidade taxaEntidade = taxaEntidadeRepository.taxaEntidadeAtualByIdEntidade( idEntidade );
		
		return taxaEntidade;
		
	}

}
