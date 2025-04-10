package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.RamoAtividadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.RamoAtividade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RamoAtividadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.RamoAtividadeRepository;

@Controller
public class RamoAtividadeController {

	@Autowired
	private RamoAtividadeRepository ramoAtividadeRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvaRamoAtividade")
	public ResponseEntity<RamoAtividadeDTO> salvarRamoAtividade( @RequestBody RamoAtividade ramoAtividade ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( ramoAtividade == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar o Ramo de Atividade para as empresas conveniadas. Valores vazios!");
		
		
		ramoAtividade = ramoAtividadeRepository.saveAndFlush(ramoAtividade);
		
		RamoAtividadeDTO dto = RamoAtividadeMapper.INSTANCE.toDto(ramoAtividade);
		
		return new ResponseEntity<RamoAtividadeDTO>(dto, HttpStatus.OK);		
	}

	@ResponseBody
	@GetMapping(value = "/getAllRamoAtividade")
	public ResponseEntity<List<RamoAtividadeDTO>> getAllRamoAtividade(  ) throws ExceptionCustomizada, UnsupportedEncodingException{

		
		List<RamoAtividade> listRamoAtividade = ramoAtividadeRepository.findAllRamoAtividade();
		
		List<RamoAtividadeDTO> dto = RamoAtividadeMapper.INSTANCE.toListDto(listRamoAtividade); 
		
		return new ResponseEntity<List<RamoAtividadeDTO>>(dto, HttpStatus.OK);		
	}

}
