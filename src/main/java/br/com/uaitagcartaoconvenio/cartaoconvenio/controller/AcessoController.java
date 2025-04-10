package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.AcessoMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Acesso;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.AcessoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.AcessoService;

@Controller
@RestController
public class AcessoController {
	
	@Autowired
	private AcessoService acessoService;
	
	@ResponseBody                         /* Poder dar um retorno da API      */
	@PostMapping(value = "/salvarAcesso") /*Mapeando a url para receber JSON*/
	public ResponseEntity<AcessoDTO> salvarAcesso( @RequestBody Acesso acesso) { /*Recebe o JSON e converte pra Objeto*/
		
		Acesso acessoSalvo = acessoService.saveAcesso(acesso);

		AcessoDTO dto = AcessoMapper.INSTANCE.toDto(acessoSalvo);
		
		return new ResponseEntity<AcessoDTO>(dto, HttpStatus.OK);
		
	}
	

}
