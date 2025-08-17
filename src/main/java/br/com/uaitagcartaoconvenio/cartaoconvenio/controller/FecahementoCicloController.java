package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.FechamentoCicloService;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RestController
public class FecahementoCicloController {

	@Autowired
	private FechamentoCicloService fechamentoCicloService;
	
	@ResponseBody                         
	@PostMapping(value = "/fechamentoCicloAutomatico") 
	public ResponseEntity<?> fechamentoCicloAutomatico( HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			String anoMesAnterior = FuncoesUteis.getPreviousMonthFormatted();
			
			String retornoMensagem = fechamentoCicloService.fechamentoCiclo( anoMesAnterior, false);
			
			if(!retornoMensagem.equals("FECHAMENTO_AUTOMATICO_OK")) {
				// throw new ExceptionCustomizada("Erro ao realizar o Fechamento Automatico!");
				throw new ExceptionCustomizada( retornoMensagem );
			}

			return new ResponseEntity<String>(retornoMensagem, HttpStatus.OK);
		
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

	@ResponseBody                         
	@PostMapping(value = "/fechamentoCicloManual/{anoMes}") 
	public ResponseEntity<?> fechamentoCicloManual( @PathVariable("anoMes") String anoMes , HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			if( !FuncoesUteis.isValidYearMonth(anoMes) ) throw new ExceptionCustomizada("Favor informar um período valido: " + anoMes );
			
			String retornoMensagem = fechamentoCicloService.fechamentoCiclo( anoMes, true);
			return new ResponseEntity<String>(retornoMensagem, HttpStatus.OK);
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
