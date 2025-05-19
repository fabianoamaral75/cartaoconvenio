package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.VigenciaContratoConveniadaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.VigenciaContratoConveniadaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.VigenciaContratoConveniadaService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class VigenciaContratoConveniadaController {
    
    private final VigenciaContratoConveniadaService service;
    private final VigenciaContratoConveniadaMapper mapper;
    
    @PostMapping(value = "/salvarVigenciaContratoConveniada")
    public ResponseEntity<VigenciaContratoConveniadaDTO> create(
            @RequestBody VigenciaContratoConveniadaDTO dto) {
        VigenciaContratoConveniada entity = mapper.toEntity(dto);
        VigenciaContratoConveniada savedEntity = service.save(entity);
        return ResponseEntity.ok(mapper.toDTO(savedEntity));
    }
    
    @GetMapping(value = "/getVigenciaContratoConveniadaById/{id}")
    public ResponseEntity<VigenciaContratoConveniadaDTO> getById(@PathVariable Long id) {        
        
        VigenciaContratoConveniada entity = service.findById(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toDTO(entity));
        
    }
}
