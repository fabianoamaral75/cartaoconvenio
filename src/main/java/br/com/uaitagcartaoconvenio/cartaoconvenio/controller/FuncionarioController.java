package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.FuncionarioMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Funcionario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.FuncionarioDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.FuncionarioService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class FuncionarioController {
	
	@Autowired
	private FuncionarioService funcionarioService;

	@ResponseBody
	@GetMapping(value = "/getAllFuncionarios")
	public ResponseEntity<?> getAllFuncionarios( HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			List<Funcionario> listaAllFuncionario = funcionarioService.getAllFuncionario();
			
			if(listaAllFuncionario == null) {
				throw new ExceptionCustomizada("Não existe Funcionário cadastradas!" );
			}
			
			List<FuncionarioDTO> dto = FuncionarioMapper.INSTANCE.toListDto(listaAllFuncionario); 
			
			return new ResponseEntity<List<FuncionarioDTO>>(dto, HttpStatus.OK);
			
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
	@GetMapping(value = "/findEntidadeByNome/{nomeFuncionario}")
	public ResponseEntity<?> findEntidadeByNome( @PathVariable("nomeFuncionario") String nomeFuncionario , HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			List<Funcionario> listaFuncionario = funcionarioService.findFuncionarioNome( nomeFuncionario );
			
			if(listaFuncionario == null) {
				throw new ExceptionCustomizada("Não existe Funcionário cadastradas com este nome: " + nomeFuncionario);
			}
			
			List<FuncionarioDTO> dto = FuncionarioMapper.INSTANCE.toListDto(listaFuncionario); 
			
			return new ResponseEntity<List<FuncionarioDTO>>(dto, HttpStatus.OK);	
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
	@GetMapping(value = "/getIdFuncionario/{id}")
	public ResponseEntity<?> getEntidadesIdEntidade( @PathVariable("id") Long id , HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			
			Funcionario funcionario = funcionarioService.getFuncionarioId(id);
			
			if(funcionario == null) {
				throw new ExceptionCustomizada("Não existe Funcionário relacionada ao CNPJ: " + id );
			}
			
			FuncionarioDTO dto = FuncionarioMapper.INSTANCE.toDto(funcionario);
			
			return new ResponseEntity<FuncionarioDTO>(dto, HttpStatus.OK);	
			
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
