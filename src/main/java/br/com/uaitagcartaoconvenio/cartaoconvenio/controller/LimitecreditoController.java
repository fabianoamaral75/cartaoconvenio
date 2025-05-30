package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.LimitecreditoService;
import jakarta.servlet.http.HttpServletRequest;

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
	public ResponseEntity<?> upLimitCredValorUtilizado(@PathVariable("idVenda")             Long       idVenda       , 
			                                                @PathVariable("valorUtilizado") BigDecimal valorUtilizado,
			                                                HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			// BigDecimal saldo = new BigDecimal(inputSaldo); 
			if(idVenda == null) {
				throw new ExceptionCustomizada("Favor informar o ID da venda:" + idVenda );
			}
			
			limitecreditoService.updateLCredValorUtilizado(idVenda, valorUtilizado);
			
		    return new ResponseEntity<String>("sucesso", HttpStatus.OK);
		    
	    } catch (ExceptionCustomizada ex) {
	    	
	    	long timestamp = System.currentTimeMillis();

	    	// Criar formato desejado
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    	sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Fuso horário opcional

	    	// Converter
	    	String dataFormatada = sdf.format(new Date(timestamp));
	    	
	        ErrorResponse error = new ErrorResponse(
	            HttpStatus.BAD_REQUEST.value(),
	            ex.getMessage(),
	            request.getRequestURI(),
	            dataFormatada
	        );
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	    }
	}

}
