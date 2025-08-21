package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RelatorioFaturamentoRequestDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RelatorioFaturamentoResponseDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.RelatorioFaturamentoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller responsável por gerenciar as operações de relatórios de faturamento
 * Inclui endpoints para gerar, buscar e download de relatórios
 */
@RestController
@RequestMapping("/api/relatorios-faturamento") // Define o path base para todos os endpoints
@RequiredArgsConstructor // Gera construtor com dependências obrigatórias (Lombok)
public class RelatorioFaturamentoController {

    // Injeção de dependência do service
    private final RelatorioFaturamentoService relatorioFaturamentoService;

    /**
     * Endpoint para gerar um novo relatório de faturamento
     * @param requestDTO DTO com dados necessários para gerar o relatório
     * @param request Objeto HttpServletRequest para informações da requisição
     * @return ResponseEntity com o relatório gerado ou erro
     */
    @ResponseBody // Indica que o retorno deve ser serializado como corpo da resposta
    @PostMapping(value = "/gerar") // Mapeia POST para /api/relatorios-faturamento/gerar
    public ResponseEntity<?> gerarRelatorio(
            @Valid @RequestBody RelatorioFaturamentoRequestDTO requestDTO, // Valida e desserializa o JSON do body
            HttpServletRequest request) {
        
        try {
            // Chama o service para gerar o relatório
            RelatorioFaturamentoResponseDTO response = relatorioFaturamentoService.gerarRelatorioFaturamento(requestDTO);
            // Retorna resposta com status CREATED (201) e o relatório gerado
            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        } catch (ExceptionCustomizada ex) {
            // Em caso de erro, trata a exceção customizada
            return handleException(ex, request);
        }
    }

    /**
     * Endpoint para buscar um relatório existente pelo ID
     * @param idRelatorio ID do relatório a ser buscado
     * @param request Objeto HttpServletRequest para informações da requisição
     * @return ResponseEntity com os dados do relatório ou erro
     */
    @ResponseBody
    @GetMapping(value = "/{idRelatorio}") // Mapeia GET para /api/relatorios-faturamento/{id}
    public ResponseEntity<?> buscarRelatorio(
            @PathVariable Long idRelatorio, // Extrai o ID do path da URL
            HttpServletRequest request) {
        
        try {
            // Chama o service para buscar o relatório pelo ID
            RelatorioFaturamentoResponseDTO response = relatorioFaturamentoService.buscarRelatorioPorId(idRelatorio);
            // Retorna resposta com status OK (200) e os dados do relatório
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (ExceptionCustomizada ex) {
            // Em caso de erro, trata a exceção customizada
            return handleException(ex, request);
        }
    }

    /**
     * Endpoint para download do relatório em PDF
     * @param idRelatorio ID do relatório a ser baixado
     * @param request Objeto HttpServletRequest para informações da requisição
     * @return ResponseEntity com o arquivo PDF para download ou erro
     */
    @GetMapping(value = "/download/{idRelatorio}") // Mapeia GET para /api/relatorios-faturamento/download/{id}
    public ResponseEntity<?> downloadRelatorio(
            @PathVariable Long idRelatorio, // Extrai o ID do path da URL
            HttpServletRequest request) {
        
        try {
            // Obtém os bytes do PDF do service
            byte[] pdfBytes = relatorioFaturamentoService.downloadRelatorio(idRelatorio);
            
            // Cria recurso a partir dos bytes do PDF
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            
            // Configura headers para download do arquivo
            HttpHeaders headers = new HttpHeaders();
            // Força o download com nome específico
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_faturamento.pdf");
            // Define o tipo de conteúdo como PDF
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);
            
            // Retorna resposta com headers configurados e o arquivo
            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length) // Define o tamanho do conteúdo
                .body(resource); // Corpo da resposta com o arquivo
            
        } catch (ExceptionCustomizada ex) {
            // Em caso de erro, trata a exceção customizada
            return handleException(ex, request);
        }
    }

    /**
     * Método auxiliar para tratamento padronizado de exceções
     * @param ex Exceção customizada a ser tratada
     * @param request Objeto HttpServletRequest para informações da requisição
     * @return ResponseEntity com detalhes do erro
     */
    private ResponseEntity<ErrorResponse> handleException(ExceptionCustomizada ex, HttpServletRequest request) {
        // Obtém timestamp atual
        long timestamp = System.currentTimeMillis();

        // Formata data e hora no fuso horário de São Paulo
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        String dataFormatada = sdf.format(new Date(timestamp));
        
        // Cria objeto de resposta de erro
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(), // Código HTTP 400
            ex.getMessage(), // Mensagem da exceção
            request.getRequestURI(), // URI da requisição que causou o erro
            dataFormatada // Timestamp formatado
        );
        // Retorna resposta com status BAD_REQUEST e detalhes do erro
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}