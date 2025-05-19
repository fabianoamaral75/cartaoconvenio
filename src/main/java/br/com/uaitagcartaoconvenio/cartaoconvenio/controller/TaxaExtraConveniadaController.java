package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaExtraConveniadaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaExtraConveniadaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TaxaExtraConveniadaService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaxaExtraConveniadaController {

    private final TaxaExtraConveniadaService service;
    private final TaxaExtraConveniadaMapper mapper;

    @PostMapping(value = "/salvarTaxaExtraConveniada")
    public ResponseEntity<TaxaExtraConveniadaDTO> create(
            @RequestBody TaxaExtraConveniadaDTO dto) {
        TaxaExtraConveniada entity = mapper.toEntity(dto);
        TaxaExtraConveniada savedEntity = service.save(entity);
        return ResponseEntity.ok(mapper.toDTO(savedEntity));
    }

    @GetMapping(value = "/getTaxaExtraConveniadaById/{id}")
    public ResponseEntity<TaxaExtraConveniadaDTO> getById(@PathVariable Long id) {

    	
    	TaxaExtraConveniada entity = service.findById(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toDTO(entity));   	
     }
}
