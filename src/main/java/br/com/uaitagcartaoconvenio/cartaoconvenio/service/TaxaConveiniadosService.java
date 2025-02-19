package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
