package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.service.LimitecreditoService;

@RestController
public class LimitecreditoController {

	@Autowired
	private LimitecreditoService limitecreditoService;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@PostMapping(value = "/upLimitCredValorUtilizado/{idVenda}/{valorUtilizado}")
	public ResponseEntity<String> upLimitCredValorUtilizado(@PathVariable("idVenda")        Long       idVenda, 
			                                                @PathVariable("valorUtilizado") BigDecimal valorUtilizado){

		// BigDecimal saldo = new BigDecimal(inputSaldo); 
		
		limitecreditoService.updateLCredValorUtilizado(idVenda, valorUtilizado);
		
	    return new ResponseEntity<String>("sucesso", HttpStatus.OK);
	}

}
