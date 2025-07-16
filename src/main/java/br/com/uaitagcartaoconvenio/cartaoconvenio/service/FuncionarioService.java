package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusFuncionario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTipoFuncionario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Funcionario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	public List<Funcionario> getAllFuncionario( )  {
		
		List<Funcionario> listaAllFuncionarios = funcionarioRepository.listaTodasFuncionario();
		
		return listaAllFuncionarios;		
	}
	
	public List<Funcionario> findFuncionarioNome( String nomeFuncionario )  {
		
		List<Funcionario> listaAllFuncionarios = funcionarioRepository.findFuncionarioNome( nomeFuncionario );
		
		return listaAllFuncionarios;
		
	}	

	public Funcionario getFuncionarioId( Long id )  {
				
		Funcionario funcionario = funcionarioRepository.findByIdFuncionario( id );
		
		return funcionario;
		
	}
	
	// Adicione estes métodos ao FuncionarioService existente

	public Funcionario updateFuncionario(Funcionario funcionario) throws ExceptionCustomizada {
	    if (funcionario.getIdFuncionario() == null) {
	        throw new ExceptionCustomizada("ID do funcionário é obrigatório para atualização");
	    }
	    
	    Funcionario funcionarioExistente = funcionarioRepository.findById(funcionario.getIdFuncionario())
	        .orElseThrow(() -> new ExceptionCustomizada("Funcionário não encontrado com ID: " + funcionario.getIdFuncionario()));
	    
	    // Atualiza apenas os campos permitidos
	    // (Aqui você pode adicionar lógica para atualizar outros campos conforme necessário)
	    
	    return funcionarioRepository.save(funcionarioExistente);
	}

	public void updateStatusFuncionario(Long id, String statusStr) throws ExceptionCustomizada {
	    try {
	        StatusFuncionario status = StatusFuncionario.valueOf(statusStr.toUpperCase());
	        int updated = funcionarioRepository.updateStatusFuncionario(id, status);
	        if (updated == 0) {
	            throw new ExceptionCustomizada("Nenhum funcionário encontrado com ID: " + id);
	        }
	    } catch (IllegalArgumentException e) {
	        throw new ExceptionCustomizada("Status inválido: " + statusStr + 
	            ". Valores permitidos: " + Arrays.toString(StatusFuncionario.values()));
	    }
	}

	public void updateTipoFuncionario(Long id, String tipoStr) throws ExceptionCustomizada {
	    try {
	        StatusTipoFuncionario tipo = StatusTipoFuncionario.valueOf(tipoStr.toUpperCase());
	        int updated = funcionarioRepository.updateTipoFuncionario(id, tipo);
	        if (updated == 0) {
	            throw new ExceptionCustomizada("Nenhum funcionário encontrado com ID: " + id);
	        }
	    } catch (IllegalArgumentException e) {
	        throw new ExceptionCustomizada("Tipo de funcionário inválido: " + tipoStr + 
	            ". Valores permitidos: " + Arrays.toString(StatusTipoFuncionario.values()));
	    }
	}	
}
