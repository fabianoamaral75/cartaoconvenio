package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ProdutoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.ProdutoService;

@Controller
@RestController
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;
	
	
	@ResponseBody                         /* Poder dar um retorno da API      */
	@PostMapping(value = "/saveProduto") /*Mapeando a url para receber JSON*/
	public ResponseEntity<ProdutoDTO> saveProduto( @RequestBody ProdutoDTO produto) throws ExceptionCustomizada { /*Recebe o JSON e converte pra Objeto*/
		
		if(produto == null ) throw new ExceptionCustomizada("Favor informar o Produto!" );
		
		if( produto.getIdConveniado()== null || produto.getIdConveniado() == 0 ) throw new ExceptionCustomizada("Favor informar o Conveniado!" );
		
		ProdutoDTO produtoDTO = produtoService.saveProduto(produto);
		
		
		return new ResponseEntity<ProdutoDTO>(produtoDTO, HttpStatus.OK);
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getlistaProdutoByNomeProduto/{nomeProduto}/{idConveniados}")
	public ResponseEntity<List<ProdutoDTO>> getlistaProdutoByNomeProduto( @PathVariable("nomeProduto"  ) String nomeProduto  ,
		    	                                                       @PathVariable("idConveniados") Long   idConveniados) throws ExceptionCustomizada{

		List<ProdutoDTO> listaProduto = produtoService.getlistaProdutoByNomeProduto( nomeProduto, idConveniados );
		
		if(listaProduto == null) {
			throw new ExceptionCustomizada("Não existe Produto cadastrado com este nome: " + nomeProduto );
		}
		return new ResponseEntity<List<ProdutoDTO>>(listaProduto, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getlistaProdutoByIdConveniados/{idConveniados}")
	public ResponseEntity<List<ProdutoDTO>> getlistaProdutoByIdConveniados( @PathVariable("idConveniados") Long idConveniados) throws ExceptionCustomizada{

		List<ProdutoDTO> listaProduto = produtoService.getlistaProdutoByIdConveniados( idConveniados );
		
		if(listaProduto == null) {
			throw new ExceptionCustomizada("Não existe Produto cadastrado para o Id da Conveniados: " + idConveniados );
		}
		return new ResponseEntity<List<ProdutoDTO>>(listaProduto, HttpStatus.OK);		
	}

}
