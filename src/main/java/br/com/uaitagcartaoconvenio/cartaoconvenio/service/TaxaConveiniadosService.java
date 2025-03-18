package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveiniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaConveiniadosRepository;

@Service
public class TaxaConveiniadosService {

	
	@Autowired
	private TaxaConveiniadosRepository taxaConveiniadosRepository;
	
	public TaxaConveiniados salvarTaxaConveiniados(TaxaConveiniados txConveiniados) {
		
		taxaConveiniadosRepository.saveAndFlush(txConveiniados);

		return txConveiniados;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public TaxaConveiniados getTaxaConveiniadosByIdConveniados( Long idConveniados )  {
		
		TaxaConveiniados taxaConveiniados = taxaConveiniadosRepository.taxaConveiniadosByIdConveniados( idConveniados );
		
		return taxaConveiniados;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<TaxaConveiniados> getListaTaxaConveiniadosByStatusTaxaConv( StatusTaxaConv descStatusTaxaCon )  {
		
		List<TaxaConveiniados> listaTaxaConveiniados = taxaConveiniadosRepository.listaTaxaConveiniadosByStatusTaxaConv( descStatusTaxaCon );
		
		return listaTaxaConveiniados;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public TaxaConveiniados getTaxaConveiniadosAtualByIdConveniados( Long idConveniados )  {
		
		TaxaConveiniados taxaConveiniados = taxaConveiniadosRepository.taxaConveiniadosAtualByIdConveniados( idConveniados );
		
		return taxaConveiniados;
		
	}

}
