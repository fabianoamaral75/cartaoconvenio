// EmprestimoController.java
package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Emprestimo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PrestacaoEmprestimo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.EmprestimoRequestDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.EmprestimoResponseDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PagamentoPrestacaoRequestDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PrestacaoResponseDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.EmprestimoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * Controller responsável por gerenciar as operações relacionadas a empréstimos
 * Inclui solicitação, aprovação, reprovação e consulta de empréstimos e prestações
 */
@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    /******************************************************************/
    /*                                                                */
    /*                   Endpoints de Criação                         */
    /*                                                                */
    /******************************************************************/

    /**
     * Solicita um novo empréstimo
     * @param request DTO com os dados da solicitação
     * @param requestHttp Objeto HttpServletRequest para tratamento de erros
     * @return ResponseEntity com o empréstimo criado ou erro
     */
    @PostMapping(value = "/solicitarEmprestimo")
    public ResponseEntity<?> solicitarEmprestimo(@Valid @RequestBody EmprestimoRequestDTO request, HttpServletRequest requestHttp) {
        try {
        	
            Emprestimo emprestimo = emprestimoService.solicitarEmprestimo(request);
            
            return new ResponseEntity<EmprestimoResponseDTO>(new EmprestimoResponseDTO(emprestimo), HttpStatus.OK);
            
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, requestHttp);
        }
    }

    /******************************************************************/
    /*                                                                */
    /*                   Endpoints de Atualização                     */
    /*                                                                */
    /******************************************************************/

    /**
     * Aprova um empréstimo solicitado
     * @param id ID do empréstimo a ser aprovado
     * @param requestHttp Objeto HttpServletRequest para tratamento de erros
     * @return ResponseEntity com o empréstimo aprovado ou erro
     */
    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<?> aprovarEmprestimo(@PathVariable Long id, HttpServletRequest requestHttp) {
        try {
            Emprestimo emprestimo = emprestimoService.aprovarEmprestimo(id);
            return new ResponseEntity<EmprestimoResponseDTO>(new EmprestimoResponseDTO(emprestimo), HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, requestHttp);
        }
    }

    /**
     * Reprova um empréstimo solicitado
     * @param id ID do empréstimo a ser reprovado
     * @param motivo Motivo da reprovação
     * @param requestHttp Objeto HttpServletRequest para tratamento de erros
     * @return ResponseEntity com o empréstimo reprovado ou erro
     */
    @PatchMapping("/{id}/reprovar")
    public ResponseEntity<?> reprovarEmprestimo(@PathVariable Long id, @RequestParam String motivo, HttpServletRequest requestHttp) {
        try {
            Emprestimo emprestimo = emprestimoService.reprovarEmprestimo(id, motivo);
            return new ResponseEntity<EmprestimoResponseDTO>(new EmprestimoResponseDTO(emprestimo), HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, requestHttp);
        }
    }

    /**
     * Registra o pagamento de uma prestação
     * @param id ID da prestação a ser paga
     * @param request DTO com dados do pagamento
     * @param requestHttp Objeto HttpServletRequest para tratamento de erros
     * @return ResponseEntity com a prestação paga ou erro
     */
    @PatchMapping("/prestacoes/{id}/pagar")
    public ResponseEntity<?> pagarPrestacao(@PathVariable Long id, @Valid @RequestBody PagamentoPrestacaoRequestDTO request, HttpServletRequest requestHttp) {
        try {
            PrestacaoEmprestimo prestacao = emprestimoService.pagarPrestacao(id, request);
            return new ResponseEntity<PrestacaoResponseDTO>(new PrestacaoResponseDTO(prestacao), HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, requestHttp);
        }
    }

    /******************************************************************/
    /*                                                                */
    /*                   Endpoints de Consulta                        */
    /*                                                                */
    /******************************************************************/

    /**
     * Lista todos os empréstimos de um funcionário
     * @param idFuncionario ID do funcionário
     * @param requestHttp Objeto HttpServletRequest para tratamento de erros
     * @return ResponseEntity com a lista de empréstimos ou erro
     */
    @GetMapping("/funcionario/{idFuncionario}")
    public ResponseEntity<?> listarEmprestimosPorFuncionario(@PathVariable Long idFuncionario, HttpServletRequest requestHttp) {
        try {
            List<Emprestimo> emprestimos = emprestimoService.listarEmprestimosPorFuncionario(idFuncionario);
            
            if (emprestimos == null || emprestimos.isEmpty()) {
                throw new ExceptionCustomizada("Nenhum empréstimo encontrado para o funcionário com ID: " + idFuncionario);
            }
            
            List<EmprestimoResponseDTO> response = emprestimos.stream()
                    .map(EmprestimoResponseDTO::new)
                    .collect(Collectors.toList());
            return new ResponseEntity<List<EmprestimoResponseDTO>>(response, HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, requestHttp);
        }
    }

    /**
     * Busca um empréstimo específico com suas prestações
     * @param id ID do empréstimo
     * @param requestHttp Objeto HttpServletRequest para tratamento de erros
     * @return ResponseEntity com o empréstimo e suas prestações ou erro
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEmprestimo(@PathVariable Long id, HttpServletRequest requestHttp) {
        try {
            return emprestimoService.buscarEmprestimoComPrestacoes(id)
                    .map(emprestimo -> {
                        EmprestimoResponseDTO response = new EmprestimoResponseDTO(emprestimo);
                        if (emprestimo.getPrestacoes() != null) {
                            List<PrestacaoResponseDTO> prestacoes = emprestimo.getPrestacoes().stream()
                                    .map(PrestacaoResponseDTO::new)
                                    .collect(Collectors.toList());
                            response.setPrestacoes(prestacoes);
                        }
                        return new ResponseEntity<EmprestimoResponseDTO>(response, HttpStatus.OK);
                    })
                    .orElseThrow(() -> new ExceptionCustomizada("Empréstimo não encontrado com ID: " + id));
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, requestHttp);
        }
    }

    /**
     * Lista todas as prestações de um empréstimo
     * @param id ID do empréstimo
     * @param requestHttp Objeto HttpServletRequest para tratamento de erros
     * @return ResponseEntity com a lista de prestações ou erro
     */
    @GetMapping("/{id}/prestacoes")
    public ResponseEntity<?> listarPrestacoes(@PathVariable Long id, HttpServletRequest requestHttp) {
        try {
            List<PrestacaoEmprestimo> prestacoes = emprestimoService.listarPrestacoesPorEmprestimo(id);
            
            if (prestacoes == null || prestacoes.isEmpty()) {
                throw new ExceptionCustomizada("Nenhuma prestação encontrada para o empréstimo com ID: " + id);
            }
            
            List<PrestacaoResponseDTO> response = prestacoes.stream()
                    .map(PrestacaoResponseDTO::new)
                    .collect(Collectors.toList());
            return new ResponseEntity<List<PrestacaoResponseDTO>>(response, HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, requestHttp);
        }
    }

    /**
     * Lista todas as prestações vencidas
     * @param requestHttp Objeto HttpServletRequest para tratamento de erros
     * @return ResponseEntity com a lista de prestações vencidas ou erro
     */
    @GetMapping("/prestacoes/vencidas")
    public ResponseEntity<?> listarPrestacoesVencidas(HttpServletRequest requestHttp) {
        try {
            List<PrestacaoEmprestimo> prestacoes = emprestimoService.listarPrestacoesVencidas();
            
            if (prestacoes == null || prestacoes.isEmpty()) {
                throw new ExceptionCustomizada("Nenhuma prestação vencida encontrada");
            }
            
            List<PrestacaoResponseDTO> response = prestacoes.stream()
                    .map(PrestacaoResponseDTO::new)
                    .collect(Collectors.toList());
            return new ResponseEntity<List<PrestacaoResponseDTO>>(response, HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, requestHttp);
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

