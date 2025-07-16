package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.ConveniadosMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.PessoaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Pessoa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ConveniadosDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PessoaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.ConveniadosService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ConveniadosController {

	@Autowired
	private ConveniadosService conveniadosService;
	
//	@Autowired
//	private PessoaService pessoaService;
	
	@ResponseBody
	@PostMapping(value = "/salvarConveniados")
	public ResponseEntity<?> salvarConveniados( @RequestBody Pessoa pessoa , HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			if( pessoa == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar a Conveniada. Valores vazios!");
	
			pessoa = conveniadosService.salvarConveniadosService(pessoa);
			
			PessoaDTO dto = PessoaMapper.INSTANCE.toDto( pessoa);
			
			return new ResponseEntity<PessoaDTO>(dto, HttpStatus.OK);
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
	@GetMapping(value = "/getConveniadosByCnpj/{cnpj}")
	public ResponseEntity<?> getConveniadosByCnpj( @PathVariable("cnpj") String cnpj , HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			
			Conveniados conveniada = conveniadosService.getConveniadosByCnpj(cnpj.trim());
			
			if(conveniada == null) {
				throw new ExceptionCustomizada("Não existe a Conveniada com este CNPJ: " + cnpj );
			}
			
			ConveniadosDTO dto = ConveniadosMapper.INSTANCE.toDto(conveniada); 
			
			return new ResponseEntity<ConveniadosDTO>(dto, HttpStatus.OK);	
			
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
	@GetMapping(value = "/getConveniadosByNome/{nome}")
	public ResponseEntity<?> getConveniadosByNome( @PathVariable("nome") String nome , HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			
			List<Conveniados> listaConveniada = conveniadosService.getConveniadosByNome(nome.trim());
			
			if(listaConveniada == null) {
				throw new ExceptionCustomizada("Não existe a Conveniada com este Nome: " + nome );
			}
			
			List<ConveniadosDTO> dto = ConveniadosMapper.INSTANCE.toListDto(listaConveniada);
			
			return new ResponseEntity<List<ConveniadosDTO>>(dto, HttpStatus.OK);
			
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
	@GetMapping(value = "/getConveniadosByCidade/{cidade}")
	public ResponseEntity<?> getConveniadosByCidade( @PathVariable("cidade") String cidade , HttpServletRequest request ) throws ExceptionCustomizada, IOException{
		try {
			
			List<Conveniados> listaConveniada = conveniadosService.getConveniadosByCidade(cidade.trim());
			
			if(listaConveniada == null) {
				throw new ExceptionCustomizada("Não existe Conveniada(os) para esta Cidade: " + cidade );
			}
			
			List<ConveniadosDTO> dto = ConveniadosMapper.INSTANCE.toListDto(listaConveniada);
			
			return new ResponseEntity<List<ConveniadosDTO>>(dto, HttpStatus.OK);
			
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
	/*            Atualização completa da conveniada                  */
	/*                                                                */
	/******************************************************************/	
    @PutMapping("/atualizarConveniada/{id}")
    public ResponseEntity<?> atualizarConveniadaCompleta(
    		@PathVariable("id") Long id,
            @RequestBody Pessoa pessoaAtualizada,
            HttpServletRequest request) {
        
        try {
            if (pessoaAtualizada == null || pessoaAtualizada.getConveniados() == null) {
                throw new ExceptionCustomizada("Dados da conveniada não podem ser nulos");
            }

            // Garante que o ID do path é o mesmo do corpo
            pessoaAtualizada.getConveniados().setIdConveniados(id);
            
            Pessoa pessoaAtualizadaSalva = conveniadosService.atualizarConveniadaCompleta(pessoaAtualizada);
            
            PessoaDTO dto = PessoaMapper.INSTANCE.toDto(pessoaAtualizadaSalva);
            
            return ResponseEntity.ok(dto);
            
        } catch (ExceptionCustomizada ex) {
            ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI(),
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

	/******************************************************************/
	/*                                                                */
	/*                  Atualização apenas do status                  */
	/*                                                                */
	/******************************************************************/	
    @PatchMapping("atualizarStatusConveniada/{id}")
    public ResponseEntity<?> atualizarStatusConveniada(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusRequest,
            HttpServletRequest request) {
        
        try {
            String novoStatusStr = statusRequest.get("descStatusConveniada");
            if (novoStatusStr == null) {
                throw new ExceptionCustomizada("O campo 'descStatusConveniada' é obrigatório");
            }

            // Valida e converte o status
            StatusConveniada novoStatus = StatusConveniada.valueOf(novoStatusStr);
            
            Conveniados conveniadaAtualizada = conveniadosService.atualizarStatusConveniada(id, novoStatus);
            
            ConveniadosDTO dto = ConveniadosMapper.INSTANCE.toDto(conveniadaAtualizada);
            
            return ResponseEntity.ok(dto);
            
        } catch (IllegalArgumentException ex) {
            ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Status inválido. Valores permitidos: " + Arrays.toString(StatusConveniada.values()),
                request.getRequestURI(),
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (ExceptionCustomizada ex) {
            ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI(),
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @PutMapping("/atualizarConveniadaSimples/{id}")
    public ResponseEntity<?> atualizarConveniadaSimples(
            @PathVariable("id") Long id,
            @RequestBody ConveniadosDTO conveniadosDTO,
            HttpServletRequest request) {
        
        try {
            if (conveniadosDTO == null) {
                throw new ExceptionCustomizada("Dados da conveniada não podem ser nulos");
            }

            // Garante que o ID do path é o mesmo do corpo
            conveniadosDTO.setIdConveniados(id);
            
            Conveniados conveniadosAtualizados = conveniadosService.atualizarConveniadaSimples(conveniadosDTO);
            
            ConveniadosDTO dto = ConveniadosMapper.INSTANCE.toDto(conveniadosAtualizados);
            
            return ResponseEntity.ok(dto);
            
        } catch (ExceptionCustomizada ex) {
            ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI(),
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
	
}
