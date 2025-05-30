package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ItemTaxaExtraEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaExtraEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TaxaExtraEntidadeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaxaExtraEntidadeController {

    private final TaxaExtraEntidadeService service;

    @PostMapping(value = "/salvarTaxaExtraEntidade")
    public ResponseEntity<?> criar(@RequestBody TaxaExtraEntidadeDTO dto, HttpServletRequest request ) throws ExceptionCustomizada, IOException{
    	try {
    		
    		TaxaExtraEntidadeDTO dtoRetorno = service.criarTaxaExtra(dto);
    		
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
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping("/adicionarContaReceber/{taxaExtraId}")
    public ResponseEntity<ItemTaxaExtraEntidadeDTO> adicionarContaReceber( @PathVariable Long taxaExtraId, @RequestBody ItemTaxaExtraEntidadeDTO itemDto) {
        service.adicionarContaReceber(taxaExtraId, itemDto);
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping("/listarContasReceberPorTaxa/{taxaExtraId}")
    public ResponseEntity<List<ItemTaxaExtraEntidadeDTO>> listarContasReceberPorTaxa( @PathVariable Long taxaExtraId) {
        return ResponseEntity.ok(service.listarContasReceberPorTaxa(taxaExtraId));
    }
}