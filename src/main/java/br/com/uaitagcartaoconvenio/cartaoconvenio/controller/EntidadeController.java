package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusEmtidade;
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

	/********************************************************************/
	/*                                                                  */
	/*Endpoint 1: Buscar todas as entidades ordenadas por ID (limit 10) */
	/*                                                                  */
	/********************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getTodasEntidadesOrdenadasPorId")
	public ResponseEntity<?> buscarTodasEntidadesOrdenadasPorId( HttpServletRequest request ) throws ExceptionCustomizada{

		try {
			List<Entidade> listaEntidade = entidadeService.buscarTodasEntidadesOrdenadasPorId();
			
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
	
	
	/******************************************************************/
	/*                                                                */
	/*  Endpoint 2: Buscar entidades por parte do nome (limit 10)     */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getPorParteDoNome/{name}")
	public ResponseEntity<?> buscarPorParteDoNome( @PathVariable("name") String name, HttpServletRequest request ) throws ExceptionCustomizada{

		try {
			List<Entidade> listaEntidade = entidadeService.buscarEntidadesPorParteDoNome( name );
			
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
	
	
	
	/******************************************************************/
	/*                                                                */
	/*  Versões com paginação                                         */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getTodasEntidadesPaginadas/paginados")
	public ResponseEntity<?> listarTodasEntidadesPaginadas( 
			@RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho, 
            HttpServletRequest request ) throws ExceptionCustomizada{

		try {
			Page<Entidade> listaEntidade = entidadeService.buscarTodasEntidadesPaginadas(pagina, tamanho);
					
	        Page<EntidadeDTO> dtoPage = mapper.toDTOPage(listaEntidade);
	        
	        return new ResponseEntity<Page<EntidadeDTO>>(dtoPage, HttpStatus.OK);
			
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
	/*  Versões com paginação                                         */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getPorParteDoNomePaginadas/paginados/{name}")
	public ResponseEntity<?> buscarPorParteDoNomePaginadas(
			@PathVariable("name") String name,
			@RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho, 
            HttpServletRequest request ) throws ExceptionCustomizada{

		try {

			Page<Entidade> listaEntidade = entidadeService.buscarEntidadesPorParteDoNomePaginadas(name, pagina, tamanho);
	        Page<EntidadeDTO> dtoPage = mapper.toDTOPage(listaEntidade);
	        
	        return new ResponseEntity<Page<EntidadeDTO>>(dtoPage, HttpStatus.OK);
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
	@PutMapping(value = "/atualizarEntidade/{id}")
	public ResponseEntity<?> atualizarEntidade(
	    @PathVariable("id") Long id,
	    @RequestBody Entidade entidadeAtualizada,
	    HttpServletRequest request) throws ExceptionCustomizada, IOException {
	    
	    try {
	        if (entidadeAtualizada == null) {
	            throw new ExceptionCustomizada("ERRO ao tentar atualizar a Entidade. Valores vazios!");
	        }

	        // Garante que o ID do path é o mesmo do corpo
	        entidadeAtualizada.setIdEntidade(id);
	        
	        // Chama o serviço para atualizar
	        Entidade entidade = entidadeService.atualizarEntidadeCompleta(entidadeAtualizada);
	        
	        // Converte para DTO
	        EntidadeDTO dto = mapper.toDTO(entidade);
	        
	        return new ResponseEntity<EntidadeDTO>(dto, HttpStatus.OK);
	    } catch (ExceptionCustomizada ex) {
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
	        String dataFormatada = sdf.format(new Date());
	        
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
	@PatchMapping(value = "/atualizarStatusEntidade/{id}")
	public ResponseEntity<?> atualizarStatusEntidade(
	    @PathVariable("id") Long id,
	    @RequestBody Map<String, String> statusRequest,
	    HttpServletRequest request) {
	    
	    try {
	        String novoStatusStr = statusRequest.get("descStatusEmtidade");
	        if (novoStatusStr == null) {
	            throw new ExceptionCustomizada("O campo 'descStatusEmtidade' é obrigatório");
	        }

	        // Converte string para enum
	        StatusEmtidade novoStatus = StatusEmtidade.valueOf(novoStatusStr);
	        
	        // Atualiza apenas o status
	        Entidade entidade = entidadeService.atualizarStatusEntidade(id, novoStatus);
	        
	        // Converte para DTO
	        EntidadeDTO dto = mapper.toDTO(entidade);
	        
	        return new ResponseEntity<EntidadeDTO>(dto, HttpStatus.OK);
	    } catch (IllegalArgumentException ex) {
	        // Caso o status não seja válido
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
	        String dataFormatada = sdf.format(new Date());
	        
	        ErrorResponse error = new ErrorResponse(
	            HttpStatus.BAD_REQUEST.value(),
	            "Status inválido. Valores permitidos: " + Arrays.toString(StatusEmtidade.values()),
	            request.getRequestURI(),
	            dataFormatada
	        );
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	    } catch (ExceptionCustomizada ex) {
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
	        String dataFormatada = sdf.format(new Date());
	        
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
