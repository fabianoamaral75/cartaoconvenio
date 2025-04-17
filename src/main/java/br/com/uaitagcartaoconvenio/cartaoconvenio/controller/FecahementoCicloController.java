package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.FecahementoCicloService;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;

@Controller
@RestController
public class FecahementoCicloController {

	@Autowired
	private FecahementoCicloService fecahementoCicloService;
	
	@ResponseBody                         
	@PostMapping(value = "/fechamentoCicloAutomatico") 
	public ResponseEntity<String> fechamentoCicloAutomatico( ) throws ExceptionCustomizada { 
		
		String anoMesAnterior = FuncoesUteis.getPreviousMonthFormatted();
		
		String retornoMensagem = fecahementoCicloService.fechamentoCiclo( anoMesAnterior, false);
		return new ResponseEntity<String>(retornoMensagem, HttpStatus.OK);
		
	}

	@ResponseBody                         
	@PostMapping(value = "/fechamentoCicloManual/{anoMes}") 
	public ResponseEntity<String> fechamentoCicloManual( @PathVariable("anoMes") String anoMes ) throws ExceptionCustomizada { 
		
		if( !FuncoesUteis.isValidYearMonth(anoMes) ) throw new ExceptionCustomizada("Favor informar um período valido: " + anoMes );
		
		String retornoMensagem = fecahementoCicloService.fechamentoCiclo( anoMes, true);
		return new ResponseEntity<String>(retornoMensagem, HttpStatus.OK);
		
	}
	
}
