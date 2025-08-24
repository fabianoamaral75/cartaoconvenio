package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.AuditoriaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.AuditoriaAntecipacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.AuditoriaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.AuditoriaService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }


    @GetMapping("/exemplo/antecipacoes")
    public String exemploAntecipacoes() {
    	
    	System.out.println("Teste");
    	
      return "exemplos/antecipacoes"; // Thymeleaf -> templates/exemplos/antecipacoes.html
    }
    
    
    @ResponseBody
    @GetMapping(value ="/auditoria/getAuditoriaPaginada/{idAntecipacao}")
    public ResponseEntity<?> getAuditoriaPaginada(
            @PathVariable Long idAntecipacao,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        
        try {
            Page<AuditoriaDTO> result = auditoriaService.findByAntecipacaoIdPaginado(idAntecipacao, page, size);
            return ResponseEntity.ok(result);
            
        } catch (Exception ex) {
            return handleException(new ExceptionCustomizada(ex.getMessage()), request);
        }
    }
    
    
    @ResponseBody
    @GetMapping(value = "/auditoria/getAuditoriaPorAntecipacao/{idAntecipacao}")
    public ResponseEntity<?> getAuditoriaPorAntecipacao(
            @PathVariable("idAntecipacao") Long idAntecipacao,
            HttpServletRequest request) throws ExceptionCustomizada {

        try {
            List<AuditoriaAntecipacao> listAuditoria = auditoriaService.getAuditoriaPorAntecipacao(idAntecipacao);
            
            if(listAuditoria == null || listAuditoria.isEmpty()) {
                throw new ExceptionCustomizada("Não existem registros de auditoria para a Antecipação com ID: " + idAntecipacao);
            }
            
            List<AuditoriaDTO> dto = AuditoriaMapper.INSTANCE.toListDto(listAuditoria); 
            
            return new ResponseEntity<List<AuditoriaDTO>>(dto, HttpStatus.OK);
        
        } catch (Exception ex) {
            return handleException(ex, request);
        }
    }

    @GetMapping(value ="/auditoria/periodo")
    public ResponseEntity<?> getAuditoriaPorPeriodo(
            @RequestParam String dataInicio,
            @RequestParam String dataFim,
            HttpServletRequest request) {
        
        try {
            List<AuditoriaDTO> auditorias = auditoriaService.findByPeriodo(dataInicio, dataFim);
            return ResponseEntity.ok(auditorias);
            
        } catch (Exception ex) {
            return handleException(ex, request);
        }
    }

    private ResponseEntity<?> handleException(Exception ex, HttpServletRequest request) {
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