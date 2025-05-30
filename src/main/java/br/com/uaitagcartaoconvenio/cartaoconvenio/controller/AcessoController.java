package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.AcessoMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Acesso;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.AcessoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.AcessoService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RestController
public class AcessoController {
	
	@Autowired
	private AcessoService acessoService;
	
	@ResponseBody                         /* Poder dar um retorno da API      */
	@PostMapping(value = "/salvarAcesso") /*Mapeando a url para receber JSON*/
	public ResponseEntity<?> salvarAcesso( @RequestBody Acesso acesso, HttpServletRequest request ) throws ExceptionCustomizada, IOException{ /*Recebe o JSON e converte pra Objeto*/
		try {
			
			if(acesso == null) {
				throw new ExceptionCustomizada("Não existe informação para em Acesso1" );
			}
		
			Acesso acessoSalvo = acessoService.saveAcesso(acesso);
	
			AcessoDTO dto = AcessoMapper.INSTANCE.toDto(acessoSalvo);
			
			return new ResponseEntity<AcessoDTO>(dto, HttpStatus.OK);
			
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
