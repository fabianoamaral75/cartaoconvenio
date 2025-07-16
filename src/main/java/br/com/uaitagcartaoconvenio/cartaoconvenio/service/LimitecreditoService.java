package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RestabelecerLimitCreditoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.LimitecreditoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class LimitecreditoService {

	@Autowired
	private LimitecreditoRepository limitecreditoRepository;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public void updateLCredValorUtilizado( Long idVenda, BigDecimal valorUtilizado ) {
		
		limitecreditoRepository.updateLimiteCreditoValorUtilizado( idVenda, valorUtilizado );		

	}
	
    public int updateRestabelecerLimiteCredito(Long idFuncionario, BigDecimal valor) {
        // Verificação adicional
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
        
        return limitecreditoRepository.updateRestabelecerLimiteCredito(idFuncionario, valor);
        
       // if (updated == 0) {
       //     throw new EntityNotFoundException("Limite de crédito não encontrado");
       // }
    }
/*    
	public List<RestabelecerLimitCreditoDTO> listaRestabelecerLimiteCredito( String anoMes ) {
		
		 return limitecreditoRepository.listaRestabelecerLimiteCredito( anoMes );		

	}
*/	

    public List<RestabelecerLimitCreditoDTO> listaRestabelecerLimiteCredito(String anoMes) {
        return limitecreditoRepository.listaRawRestabelecerLimiteCredito(anoMes).stream()
            .map(obj -> new RestabelecerLimitCreditoDTO(
                ((Number) obj[0]).longValue(),
                ((Number) obj[1]).longValue(),
                new BigDecimal(obj[2].toString())
            ))
            .collect(Collectors.toList());
    }

//	
    public int updateRollbackRestabelecerLimiteCredito(Long idFuncionario, BigDecimal valor) {
         
        return limitecreditoRepository.updateRollbackRestabelecerLimiteCredito(idFuncionario, valor);
        
    }

    
    public void atualizarLimite(Long idLimiteCredito, BigDecimal novoLimite) {
        if (novoLimite.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O limite não pode ser negativo");
        }
        
        int updated = limitecreditoRepository.updateLimite(idLimiteCredito, novoLimite);
        
        if (updated == 0) {
            throw new EntityNotFoundException("Limite de crédito não encontrado com ID: " + idLimiteCredito);
        }
    }

    public void atualizarValorUtilizado(Long idLimiteCredito, BigDecimal novoValorUtilizado) {
        if (novoValorUtilizado.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor utilizado não pode ser negativo");
        }
        
        int updated = limitecreditoRepository.updateValorUtilizado(idLimiteCredito, novoValorUtilizado);
        
        if (updated == 0) {
            throw new EntityNotFoundException("Limite de crédito não encontrado com ID: " + idLimiteCredito);
        }
    }   
    
}
