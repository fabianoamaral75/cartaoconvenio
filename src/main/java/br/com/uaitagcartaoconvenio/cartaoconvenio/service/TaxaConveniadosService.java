package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaConveniadosRepository;

@Service
public class TaxaConveniadosService {

	
	@Autowired
	private TaxaConveniadosRepository taxaConveniadosRepository;
	
	public TaxaConveniados salvarTaxaConveniados(TaxaConveniados txConveiniados) {
		
		taxaConveniadosRepository.saveAndFlush(txConveiniados);

		return txConveiniados;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public TaxaConveniados getTaxaConveniadosByIdConveniados( Long idConveniados )  {
		
		TaxaConveniados taxaConveniados = taxaConveniadosRepository.taxaConveniadosByIdConveniados( idConveniados );
		
		return taxaConveniados;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<TaxaConveniados> getListaTaxaConveniadosByStatusTaxaConv( StatusTaxaConv descStatusTaxaCon )  {
		
		List<TaxaConveniados> listaTaxaConveniados = taxaConveniadosRepository.listaTaxaConveniadosByStatusTaxaConv( descStatusTaxaCon );
		
		return listaTaxaConveniados;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public TaxaConveniados getTaxaConveniadosAtualByIdConveniados( Long idConveniados )  {
		
		TaxaConveniados taxaConveniados = taxaConveniadosRepository.taxaConveniadosAtualByIdConveniados( idConveniados );
		
		return taxaConveniados;
		
	}

}
