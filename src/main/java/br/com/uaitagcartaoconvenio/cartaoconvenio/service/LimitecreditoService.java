package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RestabelecerLimitCreditoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.LimitecreditoRepository;

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
    
	public List<RestabelecerLimitCreditoDTO> listaRestabelecerLimiteCredito( String anoMes ) {
		
		return limitecreditoRepository.listaRestabelecerLimiteCredito( anoMes );		

	}
	
//	
    public int updateRollbackRestabelecerLimiteCredito(Long idFuncionario, BigDecimal valor) {
         
        return limitecreditoRepository.updateRollbackRestabelecerLimiteCredito(idFuncionario, valor);
        
    }

    
}
