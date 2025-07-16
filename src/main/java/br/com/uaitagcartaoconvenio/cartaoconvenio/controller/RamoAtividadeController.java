package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.RamoAtividadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.RamoAtividade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RamoAtividadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.RamoAtividadeRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.RamoAtividadeService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class RamoAtividadeController {

	@Autowired
	private RamoAtividadeRepository ramoAtividadeRepository;
	
    @Autowired
    private RamoAtividadeService ramoAtividadeService;
	
	@ResponseBody
	@PostMapping(value = "/salvaRamoAtividade")
	public ResponseEntity<?> salvarRamoAtividade( @RequestBody RamoAtividade ramoAtividade, HttpServletRequest request  ) throws ExceptionCustomizada, UnsupportedEncodingException{
		try {
			
			if( ramoAtividade == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar o Ramo de Atividade para as empresas conveniadas. Valores vazios!");
			
			
			ramoAtividade = ramoAtividadeRepository.saveAndFlush(ramoAtividade);
			
			RamoAtividadeDTO dto = RamoAtividadeMapper.INSTANCE.toDto(ramoAtividade);
			
			return new ResponseEntity<RamoAtividadeDTO>(dto, HttpStatus.OK);
			
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
	@GetMapping(value = "/getAllRamoAtividade")
	public ResponseEntity<?> getAllRamoAtividade( HttpServletRequest request ) throws ExceptionCustomizada, UnsupportedEncodingException{

		try {
			List<RamoAtividade> listRamoAtividade = ramoAtividadeRepository.findAllRamoAtividade();
			
			if(listRamoAtividade == null) {
				throw new ExceptionCustomizada("Não existe Ramo de Atividade casdastrada!" );
			}
			
			List<RamoAtividadeDTO> dto = RamoAtividadeMapper.INSTANCE.toListDto(listRamoAtividade); 
			
			return new ResponseEntity<List<RamoAtividadeDTO>>(dto, HttpStatus.OK);
			
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
    @PutMapping(value = "/updateRamoAtividade/{id}")
    public ResponseEntity<?> updateRamoAtividade(
            @PathVariable Long id,
            @RequestBody RamoAtividade ramoAtividadeDetails,
            HttpServletRequest request) throws UnsupportedEncodingException {
        
        try {
            if (ramoAtividadeDetails == null) {
                throw new ExceptionCustomizada("Dados do Ramo de Atividade não podem ser nulos");
            }
            
            RamoAtividadeDTO updatedDto = ramoAtividadeService.updateRamoAtividade(id, ramoAtividadeDetails);
            return new ResponseEntity<RamoAtividadeDTO>(updatedDto, HttpStatus.OK);
            
        } catch (ExceptionCustomizada ex) {
            long timestamp = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
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
