package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.VigenciaContratoConveniadaRepository;

@Service
public class VigenciaContratoConveniadaService {
	
	@Autowired
	private VigenciaContratoConveniadaRepository vigenciaContratoConveniadaRepository;
	
	
	public VigenciaContratoConveniada save( VigenciaContratoConveniada savedEntity ) {
		savedEntity = vigenciaContratoConveniadaRepository.save(savedEntity);
		return savedEntity;
	}

	
	public VigenciaContratoConveniada findById( Long id ) {
				
		return vigenciaContratoConveniadaRepository.findById(id).orElse(null);

	}

}
