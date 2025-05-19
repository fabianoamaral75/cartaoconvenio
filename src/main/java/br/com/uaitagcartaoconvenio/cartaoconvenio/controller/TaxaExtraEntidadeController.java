package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.*;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaxaExtraEntidadeController {

    private final TaxaExtraEntidadeService service;

    @PostMapping(value = "/salvarTaxaExtraEntidade")
    public ResponseEntity<TaxaExtraEntidadeDTO> criar(@RequestBody TaxaExtraEntidadeDTO dto) {
        return ResponseEntity.ok(service.criarTaxaExtra(dto));
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