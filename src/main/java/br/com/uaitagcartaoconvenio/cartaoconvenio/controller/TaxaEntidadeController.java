package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaEntidadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TaxaEntidadeService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class TaxaEntidadeController {

	@Autowired
	private TaxaEntidadeService taxaEntidadeService;

	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaTaxaEntidadeByStatusTaxaEntidade/{status}")
	public ResponseEntity<?> getListaTaxaEntidadeByStatusTaxaEntidade( @PathVariable("status") String status, HttpServletRequest request) throws ExceptionCustomizada{

		try {
			
			StatusTaxaEntidade statusTaxaEntidade = StatusTaxaEntidade.valueOf(status);
			
			List<TaxaEntidade> listTaxaEntidade = taxaEntidadeService.getListaTaxaEntidadeByStatusTaxaEntidade( statusTaxaEntidade );
			
			if(listTaxaEntidade == null) {
				throw new ExceptionCustomizada("Não existe Taxa para a Entidade para o Status: " + StatusTaxaEntidade.valueOf(status).getDescStatusTaxaEntidade() );
			}
			
			List<TaxaEntidadeDTO> dto = TaxaEntidadeMapper.INSTANCE.toListDto(listTaxaEntidade); 
			
			return new ResponseEntity<List<TaxaEntidadeDTO>>(dto, HttpStatus.OK);
		
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
	@GetMapping(value = "/getTaxaEntidadeByIdEntidade/{idEntidade}")
	public ResponseEntity<?> getTaxaEntidadeByIdEntidade( @PathVariable("idEntidade") Long idEntidade, HttpServletRequest request ) throws ExceptionCustomizada{

		try {
			
			List<TaxaEntidade> listTaxaEntidade = taxaEntidadeService.getTaxaEntidadeByIdEntidade( idEntidade );
			
			if(listTaxaEntidade == null) {
				throw new ExceptionCustomizada("Não existe Taxa para a Entidade com o ID dasta Entidade: " + idEntidade );
			}
			
			List<TaxaEntidadeDTO> dto = TaxaEntidadeMapper.INSTANCE.toListDto(listTaxaEntidade); 
			
			return new ResponseEntity<List<TaxaEntidadeDTO>>( dto, HttpStatus.OK);
		
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

}
