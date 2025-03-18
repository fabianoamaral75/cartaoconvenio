package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.util.List;

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
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Cartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.CartaoService;

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
	public ResponseEntity<Cartao> getCartaoByIdFuncionario( @PathVariable("idFuncionario") Long idFuncionario ) throws ExceptionCustomizada{

		Cartao Cartao = cartaoService.getCartaoByIdFuncionario( idFuncionario );
		
		if(Cartao == null) {
			throw new ExceptionCustomizada("Não existe Cartão para o ID do Funcionário: " + idFuncionario );
		}
		return new ResponseEntity<Cartao>(Cartao, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getlistaCartaoByNomePessoa/{nomePessoa}")
	public ResponseEntity<List<Cartao>> getlistaCartaoByNomePessoa( @PathVariable("nomePessoa") String nomePessoa) throws ExceptionCustomizada{

		List<Cartao> listaCartao = cartaoService.getlistaCartaoByNomePessoa( nomePessoa );
		
		if(listaCartao == null) {
			throw new ExceptionCustomizada("Não existe Cartão para o Funcionário: " + nomePessoa );
		}
		return new ResponseEntity<List<Cartao>>(listaCartao, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getlistaCartaoByIdStatus/{status}")
	public ResponseEntity<List<Cartao>> getlistaCartaoByIdStatus( @PathVariable("status") String status) throws ExceptionCustomizada{

		StatusCartao statusCicloPgVenda = StatusCartao.valueOf(status);
		
		List<Cartao> listaCartao = cartaoService.getlistaCartaoByIdStatus( statusCicloPgVenda );
		
		if(listaCartao == null) {
			throw new ExceptionCustomizada("Não existe Cartão para o Status: " + StatusCicloPgVenda.valueOf(status).getDescStatusReceber() );
		}
		return new ResponseEntity<List<Cartao>>(listaCartao, HttpStatus.OK);		
	}
	
}
