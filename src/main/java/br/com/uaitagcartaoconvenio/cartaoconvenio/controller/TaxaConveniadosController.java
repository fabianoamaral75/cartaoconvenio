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
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaConveniadosMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaConveniadosDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TaxaConveniadosService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class TaxaConveniadosController {

	@Autowired
	private TaxaConveniadosService taxaConveniadosService;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaTaxaConveniadosByStatusTaxaConv/{status}")
	public ResponseEntity<?> getListaTaxaConveniadosByStatusTaxaConv( @PathVariable("status") String status, HttpServletRequest request) throws ExceptionCustomizada{
		try {
			
			StatusTaxaConv statusTaxaCon = StatusTaxaConv.valueOf(status);
			
			List<TaxaConveniados> listaTaxaConveniados = taxaConveniadosService.getListaTaxaConveniadosByStatusTaxaConv( statusTaxaCon );
			
			if(listaTaxaConveniados == null) {
				throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para o Status: " + StatusCicloPgVenda.valueOf(status).getDescStatusReceber() );
			}
			
			List<TaxaConveniadosDTO> dto = TaxaConveniadosMapper.INSTANCE.toListDto(listaTaxaConveniados); 
			
			return new ResponseEntity<List<TaxaConveniadosDTO>>(dto, HttpStatus.OK);
		
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
	@GetMapping(value = "/getListaTaxaConveniadosByStatusTaxaConv/{idConveniados}")
	public ResponseEntity<?> getListaTaxaConveniadosByStatusTaxaConv( @PathVariable("idConveniados") Long idConveniados, HttpServletRequest request ) throws ExceptionCustomizada{

		try {
			
			TaxaConveniados taxaConveniados = taxaConveniadosService.getTaxaConveniadosByIdConveniados( idConveniados );
			
			if(taxaConveniados == null) {
				throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para a ID da Conveniada: " + idConveniados );
			}
			
			TaxaConveniadosDTO dto = TaxaConveniadosMapper.INSTANCE.toDto(taxaConveniados);
			
			return new ResponseEntity<TaxaConveniadosDTO>(dto, HttpStatus.OK);
			
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
