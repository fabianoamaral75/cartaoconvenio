package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxasExtraordinariasDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TaxasExtraordinariasService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class TaxasExtraordinariasController {

    private final TaxasExtraordinariasService taxasExtraordinariasService;

    public TaxasExtraordinariasController(TaxasExtraordinariasService taxasExtraordinariasService) {
        this.taxasExtraordinariasService = taxasExtraordinariasService;
    }

    @ResponseBody
    @GetMapping(value = "/taxas-extraordinarias/getAllTaxasExtraordinarias")
    public ResponseEntity<?> getAllTaxasExtraordinarias(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        try {
            Page<TaxasExtraordinariasDTO> result = taxasExtraordinariasService.findAllPaginado(page, size);
            return ResponseEntity.ok(result);
            
        } catch (Exception ex) {
            return handleException(new ExceptionCustomizada(ex.getMessage()), request);
        }
    }

    @ResponseBody
    @GetMapping(value = "/taxas-extraordinarias/getTaxaExtraordinariaById/{idTaxa}")
    public ResponseEntity<?> getTaxaExtraordinariaById(
            @PathVariable("idTaxa") Long idTaxa,
            HttpServletRequest request) {
        
        try {
            TaxasExtraordinariasDTO dto = taxasExtraordinariasService.findById(idTaxa);
            return ResponseEntity.ok(dto);
            
        } catch (Exception ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @GetMapping(value = "/taxas-extraordinarias/status/{status}/getTaxasByStatus")
    public ResponseEntity<?> getTaxasByStatus(
            @PathVariable("status") String status,
            HttpServletRequest request) {
        
        try {
            List<TaxasExtraordinariasDTO> result = taxasExtraordinariasService.findByStatus(status);
            return ResponseEntity.ok(result);
            
        } catch (Exception ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @PostMapping(value = "/taxas-extraordinarias/criar-taxas-extraordinarias")
    public ResponseEntity<?> createTaxaExtraordinaria(
            @RequestBody TaxasExtraordinariasDTO dto,
            HttpServletRequest request) {
        
        try {
            TaxasExtraordinariasDTO createdDto = taxasExtraordinariasService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
            
        } catch (Exception ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @PutMapping(value = "/taxas-extraordinarias/{idTaxa}/updateTaxaExtraordinaria")
    public ResponseEntity<?> updateTaxaExtraordinaria(
            @PathVariable("idTaxa") Long idTaxa,
            @RequestBody TaxasExtraordinariasDTO dto,
            HttpServletRequest request) {
        
        try {
            TaxasExtraordinariasDTO updatedDto = taxasExtraordinariasService.update(idTaxa, dto);
            return ResponseEntity.ok(updatedDto);
            
        } catch (Exception ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/taxas-extraordinarias/{idTaxa}/deleteTaxaExtraordinaria")
    public ResponseEntity<?> deleteTaxaExtraordinaria(
            @PathVariable("idTaxa") Long idTaxa,
            HttpServletRequest request) {
        
        try {
            taxasExtraordinariasService.delete(idTaxa);
            return ResponseEntity.noContent().build();
            
        } catch (Exception ex) {
            return handleException(ex, request);
        }
    }

    private ResponseEntity<?> handleException(Exception ex, HttpServletRequest request) {
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        String dataFormatada = sdf.format(new Date(timestamp));
        
        if (ex instanceof ExceptionCustomizada) {
            ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI(),
                dataFormatada
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ex.getMessage(),
            request.getRequestURI(),
            dataFormatada
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    
    
}