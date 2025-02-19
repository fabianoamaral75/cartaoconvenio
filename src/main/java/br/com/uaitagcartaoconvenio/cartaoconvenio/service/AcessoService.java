package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Acesso;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.AcessoRepository;

@Service
public class AcessoService {

	@Autowired
	private AcessoRepository acessoRepository;
	
	
	public Acesso saveAcesso( Acesso acesso ) {
		return acessoRepository.save(acesso);
	}
	
	
	public List<Acesso> getListaAcesso(  ) {
		return acessoRepository.buscarListaAcesso();
	}
	
}
