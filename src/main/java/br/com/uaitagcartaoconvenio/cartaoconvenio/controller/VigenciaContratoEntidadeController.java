package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusContrato;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.VigenciaContratoEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.VigenciaContratoEntidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Vigência Contrato Entidade", description = "API para gerenciamento de vigências de contrato de entidades")
public class VigenciaContratoEntidadeController {

    private final VigenciaContratoEntidadeService service;

    @PostMapping(value = "/createVigenciaContratoEntidade")
    @Operation(summary = "Criar uma nova vigência de contrato")
    public ResponseEntity<?> create(@RequestBody VigenciaContratoEntidadeDTO dto, HttpServletRequest request) {
        try {
            if (dto == null) {
                throw new ExceptionCustomizada("Não existe informação sobre a vigência para ser salva");
            }
            return ResponseEntity.ok(service.create(dto));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @GetMapping(value = "/findVigenciaContratoEntidadeById/{id}")
    @Operation(summary = "Buscar vigência por ID")
    public ResponseEntity<?> findById(@PathVariable Long id, HttpServletRequest request) {
        try {
            VigenciaContratoEntidadeDTO dto = service.findById(id);
            if (dto == null) {
                throw new ExceptionCustomizada("Não foi encontrada vigência para o ID: " + id);
            }
            return ResponseEntity.ok(dto);
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @GetMapping(value = "/findAllVigenciaContratoEntidade")
    @Operation(summary = "Listar todas as vigências")
    public ResponseEntity<?> findAll(HttpServletRequest request) {
        try {
            List<VigenciaContratoEntidadeDTO> dtos = service.findAll();
            return ResponseEntity.ok(dtos);
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @PutMapping(value = "/updateVigenciaContratoEntidade/{id}")
    @Operation(summary = "Atualizar vigência existente")
    public ResponseEntity<?> update(
            @PathVariable Long id, 
            @RequestBody VigenciaContratoEntidadeDTO dto,
            HttpServletRequest request) {
        try {
            if (dto == null) {
                throw new ExceptionCustomizada("Dados da vigência não podem ser nulos");
            }
            return ResponseEntity.ok(service.update(id, dto));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @DeleteMapping(value = "/deleteVigenciaContratoEntidade/{id}")
    @Operation(summary = "Excluir vigência")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @GetMapping(value = "/findVigenciaContratoEntidadeByEntidadeId/entidade/{idEntidade}")
    @Operation(summary = "Buscar vigências por ID da entidade")
    public ResponseEntity<?> findByEntidadeId(
            @PathVariable Long idEntidade,
            HttpServletRequest request) {
        try {
            List<VigenciaContratoEntidadeDTO> dtos = service.findByEntidadeId(idEntidade);
            if (dtos.isEmpty()) {
                throw new ExceptionCustomizada("Nenhuma vigência encontrada para a entidade com ID: " + idEntidade);
            }
            return ResponseEntity.ok(dtos);
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @GetMapping(value = "/findVigenciaContratoEntidadeByEntidadeIdAndStatus/entidade/{idEntidade}/status/{status}")
    @Operation(summary = "Buscar vigências por ID da entidade e status")
    public ResponseEntity<?> findByEntidadeIdAndStatus(
            @PathVariable Long idEntidade, 
            @PathVariable StatusContrato status,
            HttpServletRequest request) {
        try {
            List<VigenciaContratoEntidadeDTO> dtos = service.findByEntidadeIdAndStatus(idEntidade, status);
            if (dtos.isEmpty()) {
                throw new ExceptionCustomizada("Nenhuma vigência encontrada para a entidade com ID: " + idEntidade + " e status: " + status);
            }
            return ResponseEntity.ok(dtos);
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @GetMapping(value = "/findVigenciaContratoEntidadeVigenciaAtualByEntidadeId/entidade/{idEntidade}/vigenciaAtual")
    @Operation(summary = "Buscar vigência atual por ID da entidade")
    public ResponseEntity<?> findVigenciaAtualByEntidadeId(
            @PathVariable Long idEntidade,
            HttpServletRequest request) {
        try {
            VigenciaContratoEntidadeDTO dto = service.findVigenciaAtualByEntidadeId(idEntidade);
            if (dto == null) {
                throw new ExceptionCustomizada("Nenhuma vigência atual encontrada para a entidade com ID: " + idEntidade);
            }
            return ResponseEntity.ok(dto);
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @PostMapping(value = "/renovarVigenciaContratoEntidade/renovar/{idContratoEntidade}")
    @Operation(summary = "Renovar vigência de contrato")
    public ResponseEntity<?> renovarVigencia(
            @PathVariable Long idContratoEntidade,
            @RequestBody VigenciaContratoEntidadeDTO novaVigenciaDTO,
            @RequestHeader("X-Username") String username,
            HttpServletRequest request) {
        try {
            if (novaVigenciaDTO == null) {
                throw new ExceptionCustomizada("Dados da nova vigência não podem ser nulos");
            }
            return ResponseEntity.ok(service.renovarVigencia(idContratoEntidade, novaVigenciaDTO, username));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    @PatchMapping(value = "/updateStatusVigenciaContratoEntidade/{idVigencia}/status/{novoStatus}")
    @Operation(summary = "Atualizar status da vigência")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long idVigencia,
            @PathVariable StatusContrato novoStatus,
            @RequestHeader("X-Username") String username,
            HttpServletRequest request) {
        try {
            return ResponseEntity.ok(service.updateStatus(idVigencia, novoStatus, username));
        } catch (Exception ex) {
            return handleError(ex, request);
        }
    }

    private ResponseEntity<?> handleError(Exception ex, HttpServletRequest request) {
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        String dataFormatada = sdf.format(new Date(timestamp));
        
        String message = ex instanceof ExceptionCustomizada ? ex.getMessage() : "Ocorreu um erro ao processar a requisição";
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            message,
            request.getRequestURI(),
            dataFormatada
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}