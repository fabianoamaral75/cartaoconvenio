package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusAntecipacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.AntecipacaoMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Antecipacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.AntecipacaoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.DadosCalculoAntcipacaoCicloDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.DadosCalculoAntcipacaoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.EmailAprovacaoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.AntecipacaoService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/antecipacoes")
public class AntecipacaoController {

    @Autowired
    private AntecipacaoService antecipacaoService;

    @Autowired
    private AntecipacaoMapper antecipacaoMapper;

    /******************************************************************/
    /*                                                                */
    /*                   Endpoints Básicos de Consulta                */
    /*                                                                */
    /******************************************************************/

    @ResponseBody
    @GetMapping("/{idAntecipacao}")
    public ResponseEntity<?> getAntecipacaoById(@PathVariable("idAntecipacao") Long id, HttpServletRequest request) {
        try {
            Antecipacao antecipacao = antecipacaoService.getAntecipacaoById(id);
            
            if(antecipacao == null) {
                throw new ExceptionCustomizada("Não existe Antecipação com o ID: " + id);
            }
            
            AntecipacaoDTO dto = antecipacaoMapper.toDTO(antecipacao);
            return new ResponseEntity<AntecipacaoDTO>(dto, HttpStatus.OK);
        
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getAntecipacoesByStatus(@PathVariable("status") String status, HttpServletRequest request) {
        try {
            StatusAntecipacao statusAntecipacao = StatusAntecipacao.valueOf(status);
            List<AntecipacaoDTO> dtos = antecipacaoService.getAntecipacoesByStatus(statusAntecipacao);
            
            if(dtos == null || dtos.isEmpty()) {
                throw new ExceptionCustomizada("Não existem Antecipações com o status: " + status);
            }
            
            return new ResponseEntity<List<AntecipacaoDTO>>(dtos, HttpStatus.OK);
        
        } catch (IllegalArgumentException e) {
            return handleException(new ExceptionCustomizada("Status inválido: " + status), request);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @GetMapping("/conveniada/{idConveniados}")
    public ResponseEntity<?> listarPorConveniada(@PathVariable Long idConveniados, HttpServletRequest request) {
        try {
            List<AntecipacaoDTO> dtos = antecipacaoService.listarPorConveniada(idConveniados);
            return new ResponseEntity<List<AntecipacaoDTO>>(dtos, HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @GetMapping("/conveniada/{idConveniados}/status/{status}")
    public ResponseEntity<?> listarPorConveniadaEStatus(
            @PathVariable Long idConveniados,
            @PathVariable String status,
            HttpServletRequest request) {
        try {
            StatusAntecipacao statusAntecipacao = StatusAntecipacao.valueOf(status);
            List<AntecipacaoDTO> dtos = antecipacaoService.listarPorConveniadaEStatus(idConveniados, statusAntecipacao);
            
            if(dtos == null || dtos.isEmpty()) {
                throw new ExceptionCustomizada("Não existem Antecipações com o status: " + status);
            }
            
            return new ResponseEntity<List<AntecipacaoDTO>>(dtos, HttpStatus.OK);
        
        } catch (IllegalArgumentException e) {
            return handleException(new ExceptionCustomizada("Status inválido: " + status), request);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    /******************************************************************/
    /*                                                                */
    /*                   Endpoints de Criação                         */
    /*                                                                */
    /******************************************************************/

    @ResponseBody
    @PostMapping("/vendas-mes-corrente")
//    public ResponseEntity<?> criarPreAntecipacaoVendasMesCorrente( @RequestParam Long idConveniados, @RequestBody List<Long> idsVendas, @RequestParam String loginUser, HttpServletRequest request ) {
    public ResponseEntity<?> criarPreAntecipacaoVendasMesCorrente( @RequestBody DadosCalculoAntcipacaoDTO dto, HttpServletRequest request ) {
        try {
//            AntecipacaoDTO dto = antecipacaoService.criarPreAntecipacaoVendasMesCorrente(idConveniados, idsVendas, loginUser);
            AntecipacaoDTO dtoRetorno = antecipacaoService.criarPreAntecipacaoVendasMesCorrente( dto );
            return new ResponseEntity<AntecipacaoDTO>(dtoRetorno, HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @PostMapping("/fechamento-existente")
    public ResponseEntity<?> criarPreAntecipacaoFechamentoExistente( @RequestBody DadosCalculoAntcipacaoCicloDTO dto, HttpServletRequest request) {
        try {
            AntecipacaoDTO dtoRetorno = antecipacaoService.criarPreAntecipacaoFechamentoExistente( dto );
            return new ResponseEntity<AntecipacaoDTO>(dtoRetorno, HttpStatus.OK);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    /******************************************************************/
    /*                                                                */
    /*                   Endpoints de Atualização                     */
    /*                                                                */
    /******************************************************************/

    @ResponseBody
    @PutMapping("/{idAntecipacao}/status/{novoStatus}")
    public ResponseEntity<?> atualizarStatusAntecipacao(
            @PathVariable Long idAntecipacao,
            @PathVariable String novoStatus,
            HttpServletRequest request) {
        try {
            StatusAntecipacao status = StatusAntecipacao.valueOf(novoStatus);
            Antecipacao antecipacaoAtualizada = antecipacaoService.atualizarStatusAntecipacao(idAntecipacao, status);
            
            AntecipacaoDTO dto = antecipacaoMapper.toDTO(antecipacaoAtualizada);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return handleException(new ExceptionCustomizada("Status inválido: " + novoStatus), request);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }
 
    @ResponseBody
    @PutMapping("/{idAntecipacao}/registrar-comprovante")
    public ResponseEntity<?> registrarComprovante(
            @PathVariable Long idAntecipacao,
            @RequestParam MultipartFile arquivoComprovante,
            HttpServletRequest request) {
        try {
            AntecipacaoDTO dto = antecipacaoService.registrarComprovante(idAntecipacao, arquivoComprovante);
            return ResponseEntity.ok(dto);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @PutMapping("/{idAntecipacao}/cancelar")
    public ResponseEntity<?> cancelarAntecipacao(
            @PathVariable Long idAntecipacao,
            @RequestParam String motivo,
            HttpServletRequest request) {
        try {
            antecipacaoService.cancelarAntecipacao(idAntecipacao, motivo);
            return ResponseEntity.ok().build();
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    /******************************************************************/
    /*                                                                */
    /*                   Endpoints de Email                           */
    /*                                                                */
    /******************************************************************/

    @ResponseBody
    @Transactional
    @PostMapping("/enviar-email-aprovacao")
    public ResponseEntity<?> enviarEmailAprovacao( @RequestBody EmailAprovacaoDTO dto, HttpServletRequest request) {
        try {
            // Verifica se a lista de destinatários está vazia
            if(dto.getDestinatariosCopia() != null && dto.getDestinatariosCopia().isEmpty()) {
                throw new ExceptionCustomizada("A lista de destinatários não pode estar vazia");
            }

            if(dto.getDestinatarioPrincipal() != null) {
                antecipacaoService.enviarEmailAprovacao( dto );
            }
            
            return ResponseEntity.ok().body("E-mail de aprovação enviado com sucesso");
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }
    /******************************************************************/
    /*                                                                */
    /*                   Métodos Auxiliares                           */
    /*                                                                */
    /******************************************************************/

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