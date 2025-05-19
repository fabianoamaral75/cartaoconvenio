package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TipoPeriodoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TipoPeriodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TipoPeriodoController {

    private final TipoPeriodoService tipoPeriodoService;

    @GetMapping(value = "/getAllTipoPeriodo")
    public ResponseEntity<List<TipoPeriodoDTO>> findAll() {
        List<TipoPeriodoDTO> tiposPeriodo = tipoPeriodoService.findAll();
        return ResponseEntity.ok(tiposPeriodo);
    }

    @GetMapping(value = "/getTipoPeriodoById/{id}")
    public ResponseEntity<TipoPeriodoDTO> findById(@PathVariable Long id) {
        TipoPeriodoDTO tipoPeriodo = tipoPeriodoService.findById(id);
        return ResponseEntity.ok(tipoPeriodo);
    }

    @PostMapping(value = "/salvarTipoPeriodo")
    public ResponseEntity<TipoPeriodoDTO> create(@Valid @RequestBody TipoPeriodoDTO tipoPeriodoDTO) {
        TipoPeriodoDTO createdTipoPeriodo = tipoPeriodoService.create(tipoPeriodoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTipoPeriodo);
    }

    @PutMapping("/updateTipoPeriodoById/{id}")
    public ResponseEntity<TipoPeriodoDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TipoPeriodoDTO tipoPeriodoDTO) {
        TipoPeriodoDTO updatedTipoPeriodo = tipoPeriodoService.update(id, tipoPeriodoDTO);
        return ResponseEntity.ok(updatedTipoPeriodo);
    }

    @DeleteMapping("/deleteTipoPeriodoById/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tipoPeriodoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
