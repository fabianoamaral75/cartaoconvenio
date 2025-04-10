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
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.CartaoMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Cartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.CartaoDTO;
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
	public ResponseEntity<CartaoDTO> getCartaoByIdFuncionario( @PathVariable("idFuncionario") Long idFuncionario ) throws ExceptionCustomizada{

		Cartao cartao = cartaoService.getCartaoByIdFuncionario( idFuncionario );
		
		if(cartao == null) {
			throw new ExceptionCustomizada("Não existe Cartão para o ID do Funcionário: " + idFuncionario );
		}
		
		CartaoDTO dto = CartaoMapper.INSTANCE.toDto(cartao);
		return new ResponseEntity<CartaoDTO>(dto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getlistaCartaoByNomePessoa/{nomePessoa}")
	public ResponseEntity<List<CartaoDTO>> getlistaCartaoByNomePessoa( @PathVariable("nomePessoa") String nomePessoa) throws ExceptionCustomizada{

		List<Cartao> listaCartao = cartaoService.getlistaCartaoByNomePessoa( nomePessoa );
		
		if(listaCartao == null) {
			throw new ExceptionCustomizada("Não existe Cartão para o Funcionário: " + nomePessoa );
		}
		
		List<CartaoDTO> dto = CartaoMapper.INSTANCE.toListDto(listaCartao); 
		
		return new ResponseEntity<List<CartaoDTO>>(dto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getlistaCartaoByIdStatus/{status}")
	public ResponseEntity<List<CartaoDTO>> getlistaCartaoByIdStatus( @PathVariable("status") String status) throws ExceptionCustomizada{

		StatusCartao statusCicloPgVenda = StatusCartao.valueOf(status);
		
		List<Cartao> listaCartao = cartaoService.getlistaCartaoByIdStatus( statusCicloPgVenda );
		
		if(listaCartao == null) {
			throw new ExceptionCustomizada("Não existe Cartão para o Status: " + StatusCicloPgVenda.valueOf(status).getDescStatusReceber() );
		}
		
		List<CartaoDTO> dto = CartaoMapper.INSTANCE.toListDto(listaCartao); 
		
		return new ResponseEntity<List<CartaoDTO>>(dto, HttpStatus.OK);		
	}
	
}
