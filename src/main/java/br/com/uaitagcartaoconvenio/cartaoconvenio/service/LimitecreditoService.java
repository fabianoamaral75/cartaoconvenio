package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	
}
