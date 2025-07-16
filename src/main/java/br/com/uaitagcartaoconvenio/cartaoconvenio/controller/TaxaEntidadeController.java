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
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaEntidadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaUpdateDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TaxaEntidadeService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class TaxaEntidadeController {

	@Autowired
	private TaxaEntidadeService taxaEntidadeService;

    @Autowired
    private TaxaEntidadeMapper taxaEntidadeMapper;
    
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

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @ResponseBody
    @GetMapping("/findByEntidadeIdAndStatus/{idEntidade}/status/{status}")
    public ResponseEntity<?> findByEntidadeIdAndStatus(
            @PathVariable Long idEntidade,
            @PathVariable String status,
            HttpServletRequest request) {
        try {
            StatusTaxaEntidade statusTaxa = StatusTaxaEntidade.valueOf(status);
            List<TaxaEntidade> taxas = taxaEntidadeService.findByEntidadeIdAndStatus(idEntidade, statusTaxa);
            
            List<TaxaEntidadeDTO> dtos = taxaEntidadeMapper.toListDto(taxas);
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
    @PutMapping("/atualizarTaxaEntidade/{idEntidade}")
    public ResponseEntity<?> atualizarTaxa(
            @PathVariable Long idEntidade,
            @RequestBody TaxaUpdateDTO taxaUpdateDTO,
            HttpServletRequest request) {
        try {
            if (taxaUpdateDTO.getTaxa() == null || taxaUpdateDTO.getTaxa().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ExceptionCustomizada("Valor da taxa deve ser maior que zero");
            }

            TaxaEntidade taxaAtualizada = taxaEntidadeService.atualizarTaxa(
                idEntidade, 
                taxaUpdateDTO.getTaxa()
            );

            TaxaEntidadeDTO dto = taxaEntidadeMapper.toDto(taxaAtualizada);
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
    @PutMapping("/atualizarStatusTaxaEntidade/{idEntidade}/{novoStatus}")
    public ResponseEntity<?> atualizarStatusTaxa(
            @PathVariable Long idEntidade,
            @PathVariable String novoStatus,
            HttpServletRequest request) {
        try {
            StatusTaxaEntidade status = StatusTaxaEntidade.valueOf(novoStatus);
            TaxaEntidade taxaAtualizada = taxaEntidadeService.atualizarStatusTaxa(idEntidade, status);
            
            TaxaEntidadeDTO dto = taxaEntidadeMapper.toDto(taxaAtualizada);
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
