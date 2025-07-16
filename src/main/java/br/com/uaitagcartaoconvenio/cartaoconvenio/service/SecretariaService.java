package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;
import java.util.Optional;

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
	
	public List<Secretaria> getSecretariaByIdEntidade( Long id ) {
		
		List<Secretaria> listaSecretaria = secretariaRepository.listaSecretariaByIdEntidade(id);
		
		return listaSecretaria;
		
	}
	
	public List<Secretaria> getSecretariaByIdFuncionario( Long id ) {
		
		List<Secretaria> listaSecretaria = secretariaRepository.listaSecretariaByIdFuncionario(id);
		
		return listaSecretaria;
		
	}
	
	public Optional<Secretaria> getByIdSecretaria( Long id ) {
		
		Optional<Secretaria> secretaria = secretariaRepository.findByIdSecretaria( id );
		
		return secretaria;
		
	}
	
	public List<Secretaria> getAllSecretaria( ) {
		
		List<Secretaria> listaSecretaria = secretariaRepository.listaAllSecretaria();
		
		return listaSecretaria;
		
	}

	public Secretaria atualizarSecretaria(Long id, Secretaria secretariaAtualizada) throws ExceptionCustomizada {
	    Optional<Secretaria> secretariaExistente = secretariaRepository.findByIdSecretaria(id);

	    if (!secretariaExistente.isPresent()) {
	        throw new ExceptionCustomizada("Secretaria não encontrada com o ID: " + id);
	    }

	    Secretaria secretaria = secretariaExistente.get();

	    // Atualiza todos os campos (exceto ID e campos imutáveis como dtCriacao)
	    secretaria.setNomeSecretaria(secretariaAtualizada.getNomeSecretaria());
	    secretaria.setLogradoro(secretariaAtualizada.getLogradoro());
	    secretaria.setUf(secretariaAtualizada.getUf());
	    secretaria.setCidade(secretariaAtualizada.getCidade());
	    secretaria.setCep(secretariaAtualizada.getCep());
	    secretaria.setNumero(secretariaAtualizada.getNumero());
	    secretaria.setComplemento(secretariaAtualizada.getComplemento());
	    secretaria.setBairro(secretariaAtualizada.getBairro());
	    secretaria.setEntidade(secretariaAtualizada.getEntidade());

	    // O JPA atualiza automaticamente dtAlteracao via @PreUpdate
	    return secretariaRepository.save(secretaria);
	}
}
