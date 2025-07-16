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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaConveniadosMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaConveniadosDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaUpdateDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TaxaConveniadosService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class TaxaConveniadosController {

	@Autowired
	private TaxaConveniadosService taxaConveniadosService;
	
    @Autowired
    private TaxaConveniadosMapper taxaConveniadosMapper;
	
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

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @ResponseBody
    @GetMapping("/findByConveniadosIdAndStatus/{idConveniados}/status/{status}")
    public ResponseEntity<?> findByConveniadosIdAndStatus(
            @PathVariable Long idConveniados,
            @PathVariable String status,
            HttpServletRequest request) {
        try {
            StatusTaxaConv statusTaxa = StatusTaxaConv.valueOf(status);
            List<TaxaConveniados> taxas = taxaConveniadosService.findByConveniadosIdAndStatus(idConveniados, statusTaxa);
            
            List<TaxaConveniadosDTO> dtos = taxaConveniadosMapper.toListDto(taxas);
            return ResponseEntity.ok(dtos);
        } catch (IllegalArgumentException e) {
            return handleException(new ExceptionCustomizada("Status inválido: " + status), request);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @ResponseBody
    @PutMapping("/atualizarTaxaConveniados/{idConveniados}")
    public ResponseEntity<?> atualizarTaxa(
            @PathVariable Long idConveniados,
            @RequestBody TaxaUpdateDTO taxaUpdateDTO,
            HttpServletRequest request) {
        try {
            if (taxaUpdateDTO.getTaxa() == null || taxaUpdateDTO.getTaxa().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ExceptionCustomizada("Valor da taxa deve ser maior que zero");
            }

            TaxaConveniados taxaAtualizada = taxaConveniadosService.atualizarTaxa(
                idConveniados, 
                taxaUpdateDTO.getTaxa()
            );

            TaxaConveniadosDTO dto = taxaConveniadosMapper.toDto(taxaAtualizada);
            return ResponseEntity.ok(dto);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @ResponseBody
    @PutMapping("/atualizarStatusTaxaConveniados/{idConveniados}/{novoStatus}")
    public ResponseEntity<?> atualizarStatusTaxa(
            @PathVariable Long idConveniados,
            @PathVariable String novoStatus,
            HttpServletRequest request) {
        try {
            StatusTaxaConv status = StatusTaxaConv.valueOf(novoStatus);
            TaxaConveniados taxaAtualizada = taxaConveniadosService.atualizarStatusTaxa(idConveniados, status);
            
            TaxaConveniadosDTO dto = taxaConveniadosMapper.toDto(taxaAtualizada);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return handleException(new ExceptionCustomizada("Status inválido: " + novoStatus), request);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    private ResponseEntity<ErrorResponse> handleException(ExceptionCustomizada ex, HttpServletRequest request) {
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
