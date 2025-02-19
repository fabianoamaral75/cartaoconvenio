package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Secretaria;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.SecretariaRepository;

@Service
public class SecretariaService {
	
	
	@Autowired
	SecretariaRepository secretariaRepository;
	
	public Secretaria salvarSecretariaService( Secretaria secretaria ) throws ExceptionCustomizada {
		
		secretaria.setFuncionario(null);
		
		secretaria = secretariaRepository.saveAndFlush( secretaria );
		
		return secretaria;
		
	}
	

}
