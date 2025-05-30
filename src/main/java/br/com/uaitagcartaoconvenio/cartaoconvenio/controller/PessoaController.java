package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

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
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.PessoaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Pessoa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PessoaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.PessoaService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PessoaController {
	
	@Autowired
	private PessoaService pessoaService;

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getPessoaFisicaByCpf/{cpf}")
	public ResponseEntity<?> getPessoaFisicaByCpf( @PathVariable("cpf") String cpf, HttpServletRequest request ) throws ExceptionCustomizada{
		try {
			List<Pessoa> listPessoa = pessoaService.getPessoaFisicaByCpf(cpf);
			
			if(listPessoa == null) {
				throw new ExceptionCustomizada("Não existe a Pessoa com este CPJ: " + cpf );
			}
			
			List<PessoaDTO> dto = PessoaMapper.INSTANCE.toListDto(listPessoa); 
			
			return new ResponseEntity<List<PessoaDTO>>(dto, HttpStatus.OK);
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
	@GetMapping(value = "/getPessoaFisicaByNome/{nomePessoa}")
	public ResponseEntity<?> getPessoaFisicaByNome( @PathVariable("nomePessoa") String nomePessoa, HttpServletRequest request ) throws ExceptionCustomizada{
		try {
			List<Pessoa> listPessoa = pessoaService.getPessoaFisicaByNome(nomePessoa);
			
			if(listPessoa == null) {
				throw new ExceptionCustomizada("Não existe a Pessoa com este nome: " + nomePessoa );
			}
			
			List<PessoaDTO> dto = PessoaMapper.INSTANCE.toListDto(listPessoa); 
			
			return new ResponseEntity<List<PessoaDTO>>(dto, HttpStatus.OK);		
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
	@GetMapping(value = "/getPessoaJuridicaByCnpj/{cnpj}")
	public ResponseEntity<?> getPessoaJuridicaByCnpj( @PathVariable("cnpj") String cnpj, HttpServletRequest request ) throws ExceptionCustomizada{
		try {
			List<Pessoa> listPessoa = pessoaService.getPessoaJuridicaByCnpj(cnpj);
			
			if(listPessoa == null) {
				throw new ExceptionCustomizada("Não existe a Pessoa com este CNPJ: " + cnpj );
			}
			
			List<PessoaDTO> dto = PessoaMapper.INSTANCE.toListDto(listPessoa); 
			
			return new ResponseEntity<List<PessoaDTO>>(dto, HttpStatus.OK);	
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
