package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Produto;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ProdutoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.ProdutoService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RestController
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;
	
	
	@ResponseBody                         /* Poder dar um retorno da API      */
	@PostMapping(value = "/saveProduto") /*Mapeando a url para receber JSON*/
	public ResponseEntity<?> saveProduto( @RequestBody Produto produto, HttpServletRequest request) throws ExceptionCustomizada { /*Recebe o JSON e converte pra Objeto*/
		try {
			
			if(produto == null ) throw new ExceptionCustomizada("Favor informar o Produto!" );
			
			if( produto.getConveniados().getIdConveniados() == null || produto.getConveniados().getIdConveniados() == 0 ) throw new ExceptionCustomizada("Favor informar o Conveniado!" );
			
			ProdutoDTO produtoDTO = produtoService.saveProduto(produto);
			
			
			return new ResponseEntity<ProdutoDTO>(produtoDTO, HttpStatus.OK);
			
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
	@GetMapping(value = "/getlistaProdutoByNomeProduto/{nomeProduto}/{idConveniados}")
	public ResponseEntity<?> getlistaProdutoByNomeProduto( @PathVariable("nomeProduto"  ) String nomeProduto  ,
		    	                                                          @PathVariable("idConveniados") Long   idConveniados, 
		    	                                                          HttpServletRequest request) throws ExceptionCustomizada{
		try {
			
			List<ProdutoDTO> listaProduto = produtoService.getlistaProdutoByNomeProduto( nomeProduto, idConveniados );
			
			if(listaProduto == null) {
				throw new ExceptionCustomizada("Não existe Produto cadastrado com este nome: " + nomeProduto );
			}
			return new ResponseEntity<List<ProdutoDTO>>(listaProduto, HttpStatus.OK);
			
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
	@GetMapping(value = "/getlistaProdutoByIdConveniados/{idConveniados}")
	public ResponseEntity<?> getlistaProdutoByIdConveniados( @PathVariable("idConveniados") Long idConveniados, HttpServletRequest request ) throws ExceptionCustomizada{
		
		try {
			
			List<ProdutoDTO> listaProduto = produtoService.getlistaProdutoByIdConveniados( idConveniados );
			
			if(listaProduto == null) {
				throw new ExceptionCustomizada("Não existe Produto cadastrado para o Id da Conveniados: " + idConveniados );
			}
			return new ResponseEntity<List<ProdutoDTO>>(listaProduto, HttpStatus.OK);
			
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
	@PatchMapping(value = "/atualizarNomeProduto/{idProduto}/{idConveniado}")
	public ResponseEntity<?> atualizarNomeProduto(
	        @PathVariable("idProduto") Long idProduto,
	        @PathVariable("idConveniado") Long idConveniado,
	        @RequestBody String novoNome,
	        HttpServletRequest request) {
	    try {
	        ProdutoDTO produtoAtualizado = produtoService.atualizarNomeProduto(idProduto, novoNome, idConveniado);
	        return new ResponseEntity<>(produtoAtualizado, HttpStatus.OK);
	    } catch (Exception ex) {
	        return criarRespostaErro(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	    }
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@PatchMapping(value = "/atualizarValorProduto/{idProduto}/{idConveniado}")
	public ResponseEntity<?> atualizarValorProduto(
	        @PathVariable("idProduto") Long idProduto,
	        @PathVariable("idConveniado") Long idConveniado,
	        @RequestBody BigDecimal novoValor,
	        HttpServletRequest request) {
	    try {
	        ProdutoDTO produtoAtualizado = produtoService.atualizarValorProduto(idProduto, novoValor, idConveniado);
	        return new ResponseEntity<>(produtoAtualizado, HttpStatus.OK);
	    } catch (Exception ex) {
	        return criarRespostaErro(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	    }
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @PutMapping("/atualizarProduto/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        Produto produto = produtoService.atualizarProduto(id, produtoAtualizado);
        return ResponseEntity.ok(produto);
    }

	/*************************************************************************/
	/*                                                                       */
	/* Método auxiliar para tratamento de erros (já existente no controller) */
	/*                                                                       */
	/*************************************************************************/
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
