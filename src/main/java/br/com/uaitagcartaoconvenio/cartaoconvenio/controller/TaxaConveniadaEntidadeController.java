package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaConveniadaEntidadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniadaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaConveniadaEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TaxaConveniadaEntidadeService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class TaxaConveniadaEntidadeController {

    @Autowired
    private TaxaConveniadaEntidadeService service;

    @ResponseBody
    @GetMapping(value = "/findAllTaxasConvEnti/{idEntidade}/{idConveniados}")
    public ResponseEntity<?> getAllByEntidadeAndConveniados(
            @PathVariable("idEntidade") Long idEntidade,
            @PathVariable("idConveniados") Long idConveniados,
            HttpServletRequest request) {
        
        try {
            List<TaxaConveniadaEntidade> taxas = service.findAllByEntidadeAndConveniados(idEntidade, idConveniados);
            List<TaxaConveniadaEntidadeDTO> dtos = TaxaConveniadaEntidadeMapper.INSTANCE.toListDto(taxas);
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @ResponseBody
    @GetMapping(value = "/findByIdTaxasConvEnti/{idTaxa}/{idEntidade}/{idConveniados}")
    public ResponseEntity<?> getByIdAndEntidadeAndConveniados(
            @PathVariable("idTaxa") Long idTaxa,
            @PathVariable("idEntidade") Long idEntidade,
            @PathVariable("idConveniados") Long idConveniados,
            HttpServletRequest request) {
        
        try {
            TaxaConveniadaEntidade taxa = service.findByIdAndEntidadeAndConveniados(idTaxa, idEntidade, idConveniados);
            TaxaConveniadaEntidadeDTO dto = TaxaConveniadaEntidadeMapper.INSTANCE.toDto(taxa);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleError(ex, request);
        }
    }

    @ResponseBody
    @PostMapping(value = "/saveTaxasConvEnti")
    public ResponseEntity<?> create(
            @RequestBody TaxaConveniadaEntidadeDTO dto,
            HttpServletRequest request) {
        
        try {
            TaxaConveniadaEntidade saved = service.save(dto);
            TaxaConveniadaEntidadeDTO savedDto = TaxaConveniadaEntidadeMapper.INSTANCE.toDto(saved);
            return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @ResponseBody
    @PutMapping(value = "/updateTaxasConvEnti/{idTaxa}/{idEntidade}/{idConveniados}")
    public ResponseEntity<?> update(
            @PathVariable("idTaxa") Long idTaxa,
            @PathVariable("idEntidade") Long idEntidade,
            @PathVariable("idConveniados") Long idConveniados,
            @RequestBody TaxaConveniadaEntidadeDTO dto,
            HttpServletRequest request) {
        
        try {
            TaxaConveniadaEntidade updated = service.update(idTaxa, dto, idEntidade, idConveniados);
            TaxaConveniadaEntidadeDTO updatedDto = TaxaConveniadaEntidadeMapper.INSTANCE.toDto(updated);
            return new ResponseEntity<>(updatedDto, HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleError(ex, request);
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/deleteTaxasConvEntis/{idTaxa}/{idEntidade}/{idConveniados}")
    public ResponseEntity<?> delete(
            @PathVariable("idTaxa") Long idTaxa,
            @PathVariable("idEntidade") Long idEntidade,
            @PathVariable("idConveniados") Long idConveniados,
            HttpServletRequest request) {
        
        try {
            service.delete(idTaxa, idEntidade, idConveniados);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ExceptionCustomizada ex) {
            return handleError(ex, request);
        }
    }

    private ResponseEntity<ErrorResponse> handleError(Exception ex, HttpServletRequest request) {
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        String dataFormatada = sdf.format(new Date(timestamp));

        String message = ex instanceof ExceptionCustomizada ? ex.getMessage() : "Ocorreu um erro interno";

        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            message,
            request.getRequestURI(),
            dataFormatada
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    public TaxaConveniadaEntidadeController(TaxaConveniadaEntidadeService service) {
        this.service = service;
    }

    @PatchMapping("/updateStatusTaxasConvEntis/{idTaxa}/{newStatus}")
    public ResponseEntity<String> updateStatus(
    		@PathVariable("idTaxa") Long idTaxa,
            @PathVariable("newStatus") StatusTaxaConv newStatus      // Novo status
    ) {
        try {
            service.updateStatus(newStatus, idTaxa);
            return ResponseEntity.ok("Status atualizado com sucesso para " + newStatus);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


