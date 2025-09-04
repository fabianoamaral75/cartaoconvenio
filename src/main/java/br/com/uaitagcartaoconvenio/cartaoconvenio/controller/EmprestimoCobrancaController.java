// EmprestimoCobrancaController.java
package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.FechamentoEmprestimoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.EmprestimoFechamentoRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.EmprestimoFechamentoService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Controller responsável por gerenciar as operações de cobrança relacionadas a empréstimos
 * Inclui fechamento, restabelecimento de limite e consulta de prestações pendentes
 */
@RestController
@RequestMapping("/api/emprestimos/cobrancas")
public class EmprestimoCobrancaController {

    @Autowired
    private EmprestimoFechamentoService emprestimoFechamentoService;

    @Autowired
    private EmprestimoFechamentoRepository emprestimoFechamentoRepository;

    /******************************************************************/
    /*                                                                */
    /*                   Endpoints de Processamento                   */
    /*                                                                */
    /******************************************************************/

    /**
     * Processa o fechamento de empréstimos para um determinado mês/ano
     * @param anoMes Ano e mês no formato YYYY-MM
     * @param request Objeto HttpServletRequest para tratamento de erros
     * @return ResponseEntity com a lista de contas a receber processadas ou erro
     */
    @PostMapping("/fechamento/{anoMes}")
    public ResponseEntity<?> processarFechamentoEmprestimos(@PathVariable String anoMes, HttpServletRequest request) {
        try {
            List<ContasReceber> contasReceber = emprestimoFechamentoService.processarFechamentoEmprestimos(anoMes);
            
            if (contasReceber == null || contasReceber.isEmpty()) {
                throw new ExceptionCustomizada("Nenhuma conta a receber processada para o período: " + anoMes);
            }
            
            return new ResponseEntity<List<ContasReceber>>(contasReceber, HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    /**
     * Restabelece os limites de crédito com base nos pagamentos de empréstimos de um determinado mês/ano
     * @param anoMes Ano e mês no formato YYYY-MM
     * @param request Objeto HttpServletRequest para tratamento de erros
     * @return ResponseEntity com a lista de resultados do restabelecimento ou erro
     */
/*    
    @PostMapping("/restabelecer-limite/{anoMes}")
    public ResponseEntity<?> restabelecerLimites(@PathVariable String anoMes, HttpServletRequest request) {
        try {
            List<RestabelecerLimitCreditoEmprestimoDTO> resultados = 
                emprestimoFechamentoService.processarRestabelecimentoLimite(anoMes);
            
            if (resultados == null || resultados.isEmpty()) {
                throw new ExceptionCustomizada("Nenhum limite restabelecido para o período: " + anoMes);
            }
            
            return new ResponseEntity<List<RestabelecerLimitCreditoEmprestimoDTO>>(resultados, HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }
*/
    /******************************************************************/
    /*                                                                */
    /*                   Endpoints de Consulta                        */
    /*                                                                */
    /******************************************************************/

    /**
     * Lista as prestações pendentes para fechamento em um determinado mês/ano
     * @param anoMes Ano e mês no formato YYYY-MM
     * @param request Objeto HttpServletRequest para tratamento de erros
     * @return ResponseEntity com a lista de prestações pendentes ou erro
     */
    @GetMapping("/pendentes/{anoMes}")
    public ResponseEntity<?> listarPrestacoesPendentes(@PathVariable String anoMes, HttpServletRequest request) {
        try {
            List<FechamentoEmprestimoDTO> prestacoes = 
                emprestimoFechamentoRepository.findPrestacoesVencidasParaFechamento(anoMes);
            
            if (prestacoes == null || prestacoes.isEmpty()) {
                throw new ExceptionCustomizada("Nenhuma prestação pendente encontrada para o período: " + anoMes);
            }
            
            return new ResponseEntity<List<FechamentoEmprestimoDTO>>(prestacoes, HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    /******************************************************************/
    /*                                                                */
    /*                   Métodos Auxiliares                           */
    /*                                                                */
    /******************************************************************/

    /**
     * Trata exceções e retorna uma resposta padronizada de erro
     * @param ex Exceção customizada
     * @param request Objeto HttpServletRequest
     * @return ResponseEntity com detalhes do erro
     */
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

