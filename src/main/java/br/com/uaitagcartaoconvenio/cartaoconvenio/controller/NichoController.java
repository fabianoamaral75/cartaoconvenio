package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.UnsupportedEncodingException;
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

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.NichoMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Nicho;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.NichoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.NichoRepository;

@Controller
public class NichoController {

	@Autowired
	private NichoRepository nichoRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvarNicho")
	public ResponseEntity<NichoDTO> salvarNicho( @RequestBody Nicho nicho ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( nicho == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar o Nicho para as empresas conveniadas. Valores vazios!");

		nicho = nichoRepository.saveAndFlush(nicho);
		
		NichoDTO dto = NichoMapper.INSTANCE.toDto(nicho); 
		
		return new ResponseEntity<NichoDTO>(dto, HttpStatus.OK);		
	}

	
	@ResponseBody
	@GetMapping(value = "/getAllNicho")
	public ResponseEntity<List<NichoDTO>> getAllNicho(  ) throws ExceptionCustomizada{

		List<Nicho> listNicho = nichoRepository.getAllNicho();
		
		if(listNicho == null) {
			throw new ExceptionCustomizada("Não existe Nicho cadastradas!" );
		}
		
		List<NichoDTO> dto = NichoMapper.INSTANCE.toListDto(listNicho);  
		
		return new ResponseEntity<List<NichoDTO>>(dto, HttpStatus.OK);		
	}

	@ResponseBody
	@GetMapping(value = "/findNichoByNome/{nomeNicho}")
	public ResponseEntity<List<NichoDTO>> findNichoByNome( @PathVariable("nomeNicho") String nomeNicho ) throws ExceptionCustomizada{

		List<Nicho> listNicho = nichoRepository.findNichoNome(nomeNicho);
		
		if(listNicho == null) {
			throw new ExceptionCustomizada("Não existe Nicho cadastradas!" );
		}
		
		List<NichoDTO> dto = NichoMapper.INSTANCE.toListDto(listNicho);  
		
		return new ResponseEntity<List<NichoDTO>>(dto, HttpStatus.OK);		
	}

	@ResponseBody
	@GetMapping(value = "/findNichoNome/{id}")
	public ResponseEntity<NichoDTO> findNichoById( @PathVariable("id") Long id ) throws ExceptionCustomizada{

		Nicho nicho = nichoRepository.findNichoById(id);
		
		if(nicho == null) throw new ExceptionCustomizada("Não existe Nicho cadastradas!" );
		
		NichoDTO dto = NichoMapper.INSTANCE.toDto(nicho);
		
		return new ResponseEntity<NichoDTO>(dto, HttpStatus.OK);		
	}

}
