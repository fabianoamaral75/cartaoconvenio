package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxasFaixaVendasMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxasFaixaVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxasFaixaVendasDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TaxasFaixaVendasService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequiredArgsConstructor
public class TaxasFaixaVendasController {

    private final TaxasFaixaVendasService service;
    private final TaxasFaixaVendasMapper mapper;

    @GetMapping(value = "/getTaxasFaixaVendasById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, HttpServletRequest request) {
        try {
            TaxasFaixaVendas entity = service.findById(id);
            TaxasFaixaVendasDTO dto = mapper.toDto(entity);
            return ResponseEntity.ok(dto);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @GetMapping(value = "/getTaxasFaixaVendasAll")
    public ResponseEntity<List<TaxasFaixaVendasDTO>> getAll() {
        List<TaxasFaixaVendas> entities = service.findAll();
        List<TaxasFaixaVendasDTO> dtos = mapper.toDtoList(entities);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/getTaxasFaixaVendasByDescricao/{descricao}")
    public ResponseEntity<?> getByDescricao(@PathVariable String descricao, HttpServletRequest request) {
        try {
            List<TaxasFaixaVendas> entities = service.findByDescricao(descricao);
            List<TaxasFaixaVendasDTO> dtos = mapper.toDtoList(entities);
            return ResponseEntity.ok(dtos);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @GetMapping(value = "/getTaxasFaixaVendasByStatus/{status}")
    public ResponseEntity<?> getByStatus(@PathVariable String status, HttpServletRequest request) {
        try {
            List<TaxasFaixaVendas> entities = service.findByStatus(status);
            List<TaxasFaixaVendasDTO> dtos = mapper.toDtoList(entities);
            return ResponseEntity.ok(dtos);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @PostMapping(value = "/salvarTaxasFaixaVendas")
    public ResponseEntity<TaxasFaixaVendasDTO> create(@RequestBody TaxasFaixaVendasDTO dto) {
        TaxasFaixaVendas entity = mapper.toEntity(dto);
        entity = service.save(entity);
        TaxasFaixaVendasDTO savedDto = mapper.toDto(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
    }

    @PutMapping("/updateTaxasFaixaVendasBy/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TaxasFaixaVendasDTO dto, HttpServletRequest request) {
        try {
            if (!service.findById(id).getId().equals(dto.getId())) {
                throw new ExceptionCustomizada("ID do recurso não corresponde ao ID do corpo da requisição");
            }
            TaxasFaixaVendas entity = mapper.toEntity(dto);
            entity = service.save(entity);
            TaxasFaixaVendasDTO updatedDto = mapper.toDto(entity);
            return ResponseEntity.ok(updatedDto);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @DeleteMapping("/deleteTaxasFaixaVendasBy/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        try {
            service.delete(id);
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