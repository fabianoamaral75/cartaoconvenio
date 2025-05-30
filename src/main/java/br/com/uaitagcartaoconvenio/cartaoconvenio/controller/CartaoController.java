package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.CartaoMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Cartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.CartaoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.CartaoService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CartaoController {
	
	@Autowired
	private CartaoService cartaoService;


	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getCartaoByIdFuncionario/{idFuncionario}")
	public ResponseEntity<?> getCartaoByIdFuncionario( @PathVariable("idFuncionario") Long idFuncionario , HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			Cartao cartao = cartaoService.getCartaoByIdFuncionario( idFuncionario );
			
			if(cartao == null) {
				throw new ExceptionCustomizada("Não existe Cartão para o ID do Funcionário: " + idFuncionario );
			}
			
			CartaoDTO dto = CartaoMapper.INSTANCE.toDto(cartao);
			return new ResponseEntity<CartaoDTO>(dto, HttpStatus.OK);	
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
	@GetMapping(value = "/getlistaCartaoByNomePessoa/{nomePessoa}")
	public ResponseEntity<?> getlistaCartaoByNomePessoa( @PathVariable("nomePessoa") String nomePessoa, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			List<Cartao> listaCartao = cartaoService.getlistaCartaoByNomePessoa( nomePessoa );
			
			if(listaCartao == null) {
				throw new ExceptionCustomizada("Não existe Cartão para o Funcionário: " + nomePessoa );
			}
			
			List<CartaoDTO> dto = CartaoMapper.INSTANCE.toListDto(listaCartao); 
			
			return new ResponseEntity<List<CartaoDTO>>(dto, HttpStatus.OK);
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
	@GetMapping(value = "/getlistaCartaoByIdStatus/{status}")
	public ResponseEntity<?> getlistaCartaoByIdStatus( @PathVariable("status") String status, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			StatusCartao statusCicloPgVenda = StatusCartao.valueOf(status);
			
			List<Cartao> listaCartao = cartaoService.getlistaCartaoByIdStatus( statusCicloPgVenda );
			
			if(listaCartao == null) {
				throw new ExceptionCustomizada("Não existe Cartão para o Status: " + StatusCicloPgVenda.valueOf(status).getDescStatusReceber() );
			}
			
			List<CartaoDTO> dto = CartaoMapper.INSTANCE.toListDto(listaCartao); 
			
			return new ResponseEntity<List<CartaoDTO>>(dto, HttpStatus.OK);	
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
