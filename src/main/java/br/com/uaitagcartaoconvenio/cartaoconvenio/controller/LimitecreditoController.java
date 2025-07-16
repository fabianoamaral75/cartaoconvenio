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
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.persistence.EntityNotFoundException;
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

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@PostMapping(value = "/atualizarLimite/{idLimiteCredito}")
	public ResponseEntity<?> atualizarLimite(@PathVariable("idLimiteCredito") Long idLimiteCredito,
	                                        @RequestBody BigDecimal novoLimite,
	                                        HttpServletRequest request) {
	    try {
	        limitecreditoService.atualizarLimite(idLimiteCredito, novoLimite);
	        return new ResponseEntity<>("Limite atualizado com sucesso", HttpStatus.OK);
	    } catch (EntityNotFoundException ex) {
	        return criarRespostaErro(HttpStatus.NOT_FOUND, ex.getMessage(), request);
	    } catch (IllegalArgumentException ex) {
	        return criarRespostaErro(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	    } catch (Exception ex) {
	        return criarRespostaErro(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar limite", request);
	    }
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@PostMapping(value = "/atualizarValorUtilizado/{idLimiteCredito}")
	public ResponseEntity<?> atualizarValorUtilizado(@PathVariable("idLimiteCredito") Long idLimiteCredito,
	                                               @RequestBody BigDecimal novoValorUtilizado,
	                                               HttpServletRequest request) {
	    try {
	        limitecreditoService.atualizarValorUtilizado(idLimiteCredito, novoValorUtilizado);
	        return new ResponseEntity<>("Valor utilizado atualizado com sucesso", HttpStatus.OK);
	    } catch (EntityNotFoundException ex) {
	        return criarRespostaErro(HttpStatus.NOT_FOUND, ex.getMessage(), request);
	    } catch (IllegalArgumentException ex) {
	        return criarRespostaErro(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	    } catch (Exception ex) {
	        return criarRespostaErro(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar valor utilizado", request);
	    }
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	private ResponseEntity<?> criarRespostaErro(HttpStatus status, String mensagem, HttpServletRequest request) {
	    long timestamp = System.currentTimeMillis();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
	    String dataFormatada = sdf.format(new Date(timestamp));
	    
	    ErrorResponse error = new ErrorResponse(
	        status.value(),
	        mensagem,
	        request.getRequestURI(),
	        dataFormatada
	    );
	    return ResponseEntity.status(status).body(error);
	}	
	
}
