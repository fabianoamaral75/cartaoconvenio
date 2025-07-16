package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Pessoa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.PessoaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;
import jakarta.transaction.Transactional;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public Pessoa savarPassoa( Pessoa pessoa) {
		
		pessoaRepository.saveAndFlush(pessoa);
		return pessoa;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Pessoa> getPessoaFisicaByCpf( String cpf )  {
		
		String resultCpf = FuncoesUteis.removerCaracteresNaoNumericos( cpf );
		
		List<Pessoa> listaPessoaFisica = pessoaRepository.listaPessoaFisicaByCpf( resultCpf );
		
		return listaPessoaFisica;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Pessoa> getPessoaFisicaByNome( String nome )  {
		
		List<Pessoa> listaPessoaFisica = pessoaRepository.listaPessoaFisicaByNome( nome );
		
		return listaPessoaFisica;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<Pessoa> getPessoaJuridicaByCnpj( String cnpj )  {
		
		String resultCnpj = FuncoesUteis.removerCaracteresNaoNumericos( cnpj );
		
		List<Pessoa> listaPessoaFisica = pessoaRepository.listaPessoaJuridicaByCnpj( resultCnpj );
		
		return listaPessoaFisica;
		
	}

	
	
	@Transactional
	public Pessoa atualizarPessoaCompleta(Pessoa pessoaAtualizada) {
	    Pessoa pessoaExistente = pessoaRepository.findById(pessoaAtualizada.getIdPessoa())
	        .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));
	    
	    // Atualiza os dados básicos
	    pessoaExistente.setNomePessoa(pessoaAtualizada.getNomePessoa());
	    pessoaExistente.setLogradoro(pessoaAtualizada.getLogradoro());
	    pessoaExistente.setUf(pessoaAtualizada.getUf());
	    pessoaExistente.setCidade(pessoaAtualizada.getCidade());
	    pessoaExistente.setCep(pessoaAtualizada.getCep());
	    pessoaExistente.setNumero(pessoaAtualizada.getNumero());
	    pessoaExistente.setComplemento(pessoaAtualizada.getComplemento());
	    pessoaExistente.setBairro(pessoaAtualizada.getBairro());
	    pessoaExistente.setEmail(pessoaAtualizada.getEmail());
	    pessoaExistente.setTelefone(pessoaAtualizada.getTelefone());
	    
	    // Atualiza relacionamentos
	    pessoaExistente.setConveniados(pessoaAtualizada.getConveniados());
	    pessoaExistente.setUsuario(pessoaAtualizada.getUsuario());
	    
	    return pessoaRepository.save(pessoaExistente);
	}

	@Transactional
	public void atualizarDadosBasicos(Long idPessoa, String nome, String email, String telefone) {
	    if (nome == null || nome.trim().isEmpty()) {
	        throw new IllegalArgumentException("Nome da pessoa não pode ser vazio");
	    }
	    pessoaRepository.atualizarDadosBasicosPessoa(idPessoa, nome, email, telefone);
	}

	@Transactional
	public void atualizarEndereco(Long idPessoa, String logradouro, String uf, String cidade, 
	                             String cep, String numero, String complemento, String bairro) {
	    pessoaRepository.atualizarEnderecoPessoa(idPessoa, logradouro, uf, cidade, cep, numero, complemento, bairro);
	}

	@Transactional
	public void atualizarConveniado(Long idPessoa, Long idConveniados) {
	    pessoaRepository.atualizarConveniadoPessoa(idPessoa, idConveniados);
	}
}
