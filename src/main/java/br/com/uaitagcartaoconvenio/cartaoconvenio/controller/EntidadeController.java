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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.EntidadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.EntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.EntidadeService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class EntidadeController {

	@Autowired
	private EntidadeService entidadeService;
	
	@Autowired
	private EntidadeMapper mapper;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/
	@ResponseBody
	@PostMapping(value = "/salvarEntidade")
	public ResponseEntity<?> salvarEntidade( @RequestBody Entidade entidade,HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			if( entidade == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Entidade. Valores vazios!");

			entidade = entidadeService.salvarEntidadeService(entidade);
			
		    // Adicione estas anotações para evitar referências circulares
			EntidadeDTO dto = mapper.toDTO(entidade);
			
			return new ResponseEntity<EntidadeDTO>(dto, HttpStatus.OK);		
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
	@GetMapping(value = "/getAllEntidades")
	public ResponseEntity<?> getAllEntidades(HttpServletRequest request) {
	    try {
	        List<Entidade> listaEntidade = entidadeService.getAllEntidades();
	        
	        if(listaEntidade == null || listaEntidade.isEmpty()) {
	            throw new ExceptionCustomizada("Não existem Entidades cadastradas!");
	        }
	        
	        List<EntidadeDTO> dto = mapper.toDTOList(listaEntidade);
	        return new ResponseEntity<>(dto, HttpStatus.OK);
	        
	    } catch (ExceptionCustomizada ex) {
	        // Formatação da data/hora
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
	        String dataFormatada = sdf.format(new Date());
	        
	        // Criação do objeto de erro
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
	@GetMapping(value = "/getEntidadesCNPJ/{cnpj}")
	public ResponseEntity<?> getEntidadesCNPJ( @PathVariable("cnpj") String cnpj, HttpServletRequest request ) throws ExceptionCustomizada{
		try {
			Entidade listaEntidade = entidadeService.getEntidadesCnpj(cnpj);
			
			if(listaEntidade == null) {
				throw new ExceptionCustomizada("Não existe Entidades relacionada ao CNPJ: " + cnpj );
			}
			
			EntidadeDTO dto = mapper.toDTO(listaEntidade); 
			
			return new ResponseEntity<EntidadeDTO>(dto, HttpStatus.OK);	
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
	@GetMapping(value = "/findEntidadeByNome/{nomeEntidade}")
	public ResponseEntity<?> findEntidadeByNome( @PathVariable("nomeEntidade") String nomeEntidade, HttpServletRequest request ) throws ExceptionCustomizada{
		try {
			List<Entidade> listaEntidade = entidadeService.findEntidadeNome( nomeEntidade );
			
			if(listaEntidade == null) {
				throw new ExceptionCustomizada("Não existe Entidades cadastradas com este nome: " + nomeEntidade);
			}
			
			List<EntidadeDTO> dto = mapper.toDTOList(listaEntidade);
			
			return new ResponseEntity<List<EntidadeDTO>>(dto, HttpStatus.OK);
	    } catch (ExceptionCustomizada ex) {
	        // Formatação da data/hora
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
	        String dataFormatada = sdf.format(new Date());
	        
	        // Criação do objeto de erro
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
	@GetMapping(value = "/getIdEntidade/{id}")
	public ResponseEntity<?> getEntidadesIdEntidade( @PathVariable("id") Long id, HttpServletRequest request ) throws ExceptionCustomizada{
		try {
			Entidade entidade = entidadeService.getEntidadesId(id);
			
			if(entidade == null) {
				throw new ExceptionCustomizada("Não existe Entidades relacionada ao CNPJ: " + id );
			}
			
			EntidadeDTO dto = mapper.toDTO(entidade);
			
			return new ResponseEntity<EntidadeDTO>(dto, HttpStatus.OK);	
	    } catch (ExceptionCustomizada ex) {
	        // Formatação da data/hora
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
	        String dataFormatada = sdf.format(new Date());
	        
	        // Criação do objeto de erro
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
	@GetMapping(value = "/getEntidadeByCidade/{cidade}")
	public ResponseEntity<?> getEntidadeByCnpj( @PathVariable("cidade") String cidade, HttpServletRequest request ) throws ExceptionCustomizada{

		try {
			List<Entidade> listaEntidade = entidadeService.listaEntidadeByCidade(cidade);
			
			if(listaEntidade == null) {
				throw new ExceptionCustomizada("Não existe Entidades relacionada ao Cidade: " + cidade );
			}
			
			List<EntidadeDTO> dto = mapper.toDTOList(listaEntidade); 
			
			return new ResponseEntity<List<EntidadeDTO>>(dto, HttpStatus.OK);
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
