package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaExtraEntidadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ItemTaxaExtraEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaExtraEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TaxaExtraEntidadeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaxaExtraEntidadeController {

//    private final TaxaExtraEntidadeService service;
    
    @Autowired
    private TaxaExtraEntidadeService taxaExtraEntidadeService;
    
    @Autowired
    private TaxaExtraEntidadeMapper taxaExtraEntidadeMapper;

    @PostMapping(value = "/salvarTaxaExtraEntidade")
    public ResponseEntity<?> criar(@RequestBody TaxaExtraEntidadeDTO dto, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	try {
    		
    		TaxaExtraEntidadeDTO dtoRetorno = taxaExtraEntidadeService.criarTaxaExtra(dto);
    		
			if(dtoRetorno == null) {
				throw new ExceptionCustomizada("Erro ao salvar Taxa Extra para Entidade!" );
			}
    		
    		return ResponseEntity.ok( dto );
    	   // return ResponseEntity.ok(service.criarTaxaExtra(dto));
    	
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

    @GetMapping("/getTaxaExtraEntidadeById/{id}")
    public ResponseEntity<TaxaExtraEntidadeDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(taxaExtraEntidadeService.buscarPorId(id));
    }

    @PostMapping("/adicionarContaReceber/{taxaExtraId}")
    public ResponseEntity<ItemTaxaExtraEntidadeDTO> adicionarContaReceber( @PathVariable Long taxaExtraId, @RequestBody ItemTaxaExtraEntidadeDTO itemDto) {
    	taxaExtraEntidadeService.adicionarContaReceber(taxaExtraId, itemDto);
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping("/listarContasReceberPorTaxa/{taxaExtraId}")
    public ResponseEntity<List<ItemTaxaExtraEntidadeDTO>> listarContasReceberPorTaxa( @PathVariable Long taxaExtraId) {
        return ResponseEntity.ok(taxaExtraEntidadeService.listarContasReceberPorTaxa(taxaExtraId));
    }
    
    
    /***************************************************************************************/
    
    
    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/    
    @ResponseBody
    @GetMapping(value = "/getTaxasEntidadeByEntidadeId/{idEntidade}")
    public ResponseEntity<?> getTaxasByEntidadeId(
            @PathVariable("idEntidade") Long idEntidade,
            HttpServletRequest request) {
        try {
            List<TaxaExtraEntidade> taxas = taxaExtraEntidadeService.findAllByEntidadeId(idEntidade);
            return new ResponseEntity<List<TaxaExtraEntidadeDTO>>( taxaExtraEntidadeMapper.toListDto(taxas), HttpStatus.OK );
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/    
    @ResponseBody
    @GetMapping(value = "/getTaxaEntidadeById/{idEntidade}/{idTaxa}")
    public ResponseEntity<?> getTaxaById(
            @PathVariable("idEntidade") Long idEntidade,
            @PathVariable("idTaxa") Long idTaxa,
            HttpServletRequest request) {
        try {
            TaxaExtraEntidade taxa = taxaExtraEntidadeService.findById(idEntidade, idTaxa);
            return new ResponseEntity<TaxaExtraEntidadeDTO>(
                taxaExtraEntidadeMapper.toDto(taxa), HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/    
    @ResponseBody
    @GetMapping(value = "/getTaxasEntidadeByStatus/{idEntidade}/{status}")
    public ResponseEntity<?> getTaxasByStatus(
            @PathVariable("idEntidade") Long idEntidade,
            @PathVariable("status") String status,
            HttpServletRequest request) {
        try {
            List<TaxaExtraEntidade> taxas = taxaExtraEntidadeService.findByEntidadeIdAndStatus(idEntidade, status);
            return new ResponseEntity<List<TaxaExtraEntidadeDTO>>(
                taxaExtraEntidadeMapper.toListDto(taxas), HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/    
    @ResponseBody
    @PostMapping(value = "/createTaxaEntidade/{idEntidade}")
    public ResponseEntity<?> createTaxa(
            @PathVariable("idEntidade") Long idEntidade,
            @RequestBody TaxaExtraEntidadeDTO dto,
            HttpServletRequest request) {
        try {
            TaxaExtraEntidade novaTaxa = taxaExtraEntidadeService.create(idEntidade, dto);
            return new ResponseEntity<TaxaExtraEntidadeDTO>(
                taxaExtraEntidadeMapper.toDto(novaTaxa), HttpStatus.CREATED);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/    
    @ResponseBody
    @PutMapping(value = "/updateTaxaEntidade/{idEntidade}/{idTaxa}")
    public ResponseEntity<?> updateTaxa(
            @PathVariable("idEntidade") Long idEntidade,
            @PathVariable("idTaxa") Long idTaxa,
            @RequestBody TaxaExtraEntidadeDTO dto,
            HttpServletRequest request) {
        try {
            TaxaExtraEntidade taxaAtualizada = taxaExtraEntidadeService.update(idEntidade, idTaxa, dto);
            return new ResponseEntity<TaxaExtraEntidadeDTO>(
                taxaExtraEntidadeMapper.toDto(taxaAtualizada), HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/    
    @ResponseBody
    @PatchMapping(value = "/updateStatusTaxaEntidade/{idEntidade}/{idTaxa}/{novoStatus}")
    public ResponseEntity<?> updateStatusTaxa(
            @PathVariable("idEntidade") Long idEntidade,
            @PathVariable("idTaxa") Long idTaxa,
            @PathVariable("novoStatus") String novoStatus,
            HttpServletRequest request) {
        try {
            TaxaExtraEntidade taxaAtualizada = taxaExtraEntidadeService.updateStatus(idEntidade, idTaxa, novoStatus);
            return new ResponseEntity<TaxaExtraEntidadeDTO>(
                taxaExtraEntidadeMapper.toDto(taxaAtualizada), HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/    
    @ResponseBody
    @DeleteMapping(value = "/deleteTaxaEntidade/{idEntidade}/{idTaxa}")
    public ResponseEntity<?> deleteTaxa(
            @PathVariable("idEntidade") Long idEntidade,
            @PathVariable("idTaxa") Long idTaxa,
            HttpServletRequest request) {
        try {
            taxaExtraEntidadeService.delete(idEntidade, idTaxa);
            return ResponseEntity.noContent().build();
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

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