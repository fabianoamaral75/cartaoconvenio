package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContratoEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.ContratoEntidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContratoEntidadeController {

    private final ContratoEntidadeService service;

	@ResponseBody                         /* Poder dar um retorno da API      */
	@PostMapping(value = "/salvarContratoEntidade") /* Mapeando a url para receber JSON */
    public ResponseEntity<ContratoEntidadeDTO> create(@Valid @RequestBody ContratoEntidadeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping("/getContratoEntidadeById/{id}")
    public ResponseEntity<ContratoEntidadeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/getAllContratoEntidade")
    public ResponseEntity<List<ContratoEntidadeDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/updateContratoEntidadeById/{id}")
    public ResponseEntity<ContratoEntidadeDTO> update(@PathVariable Long id, @Valid @RequestBody ContratoEntidadeDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/deleteContratoEntidadeById/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
