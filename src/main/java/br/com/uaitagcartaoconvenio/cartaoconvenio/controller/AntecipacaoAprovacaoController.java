package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusAntecipacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Antecipacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContatoWorkflowDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.WorkflowInformativoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.AntecipacaoRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.security.AntecipacaoTokenService;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.AntecipacaoService;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.AuditoriaService;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.WorkflowService;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.EmailService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AntecipacaoAprovacaoController {

    @Autowired
    private AntecipacaoRepository antecipacaoRepository;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private AntecipacaoTokenService tokenService;
    
    @Autowired
    private AuditoriaService auditoriaService;
    
    @Autowired
	private WorkflowService workflowService;
    
    @Autowired
	private AntecipacaoService antecipacaoService;
    
	@Value("${api.security.token.secret}")
	private String SECRET_KEY;


    /******************************************************************/
    /*                                                                */
    /*                   Endpoint de Aprovação                        */
    /*                                                                */
    /******************************************************************/
    @PutMapping("/api/antecipacoes/{id}/aprovar")
    @CrossOrigin(origins = "*") // Permite chamadas do popup
    public ResponseEntity<?> aprovarAntecipacao(
            @PathVariable Long id,
            @RequestParam String token,
            @RequestParam(required = false) String motivo,
            HttpServletRequest request) {

        try {
        	
            Antecipacao antecipacao = antecipacaoRepository.findById(id).orElseThrow(() -> new ExceptionCustomizada("Antecipação não encontrada"));

            if (!tokenService.validarToken(antecipacao, token)) {
                auditoriaService.registrarAuditoria(
                    antecipacao,
                    antecipacao.getDescStatusAntecipacao(),
                    StatusAntecipacao.APROVADO,
                    "Tentativa de aprovação com token inválido: " + (motivo != null ? motivo : "Sem motivo"),
                    token,
                    request.getRemoteAddr(),
                    request.getHeader("User-Agent"),
                    obterUsuarioAtual()
                );
                
                throw new ExceptionCustomizada("Token inválido ou expirado");
            }

            StatusAntecipacao statusAnterior = antecipacao.getDescStatusAntecipacao();
            antecipacao.setDescStatusAntecipacao(StatusAntecipacao.APROVADO);
            
            if (motivo != null && !motivo.isEmpty()) {
                antecipacao.setObservacao(motivo);
            }
            
            Antecipacao antecipacaoAtualizada = antecipacaoRepository.save(antecipacao);
            
            // Registra auditoria da antecipação
            auditoriaService.registrarAuditoria(
                antecipacaoAtualizada,
                statusAnterior,
                antecipacaoAtualizada.getDescStatusAntecipacao(),
                "Aprovação realizada: " + (motivo != null ? motivo : "Sem motivo informado"),
                token,
                request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                obterUsuarioAtual()
            );
            
            // Realiza o fechamento para as vendas e ou Fechamento existente.
            antecipacaoService.processarAntecipacao( antecipacaoAtualizada );

            enviarEmailConfirmacao(antecipacaoAtualizada, "Aprovada", motivo);

            // Altere o retorno para JSON
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Antecipação aprovada com sucesso");
            response.put("status", "success");
            response.put("id", antecipacaoAtualizada.getIdAntecipacao().toString());
            
            return ResponseEntity.ok(response);
//            return ResponseEntity.ok("Antecipação aprovada com sucesso");

        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/
    private String obterUsuarioAtual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "Sistema";
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }
        
        return "Usuário Desconhecido";
    }


    /******************************************************************/
    /*                                                                */
    /*                   Endpoint de Reprovação                        */
    /*                                                                */
    /******************************************************************/
    @PutMapping("/api/antecipacoes/{id}/reprovar")
    @CrossOrigin(origins = "*") // Permite chamadas do popup
    public ResponseEntity<?> reprovarAntecipacao(
            @PathVariable Long id,
            @RequestParam String token,
            @RequestParam(required = false) String motivo,
            HttpServletRequest request) {

        try {
            Antecipacao antecipacao = antecipacaoRepository.findById(id)
                .orElseThrow(() -> new ExceptionCustomizada("Antecipação não encontrada"));

            if (!tokenService.validarToken(antecipacao, token)) {
                auditoriaService.registrarAuditoria(
                    antecipacao,
                    antecipacao.getDescStatusAntecipacao(),
                    StatusAntecipacao.REPROVADO,
                    "Tentativa de reprovação com token inválido: " + (motivo != null ? motivo : "Sem motivo"),
                    token,
                    request.getRemoteAddr(),
                    request.getHeader("User-Agent"),
                    obterUsuarioAtual()
                );
                throw new ExceptionCustomizada("Token inválido ou expirado");
            }

            StatusAntecipacao statusAnterior = antecipacao.getDescStatusAntecipacao();
            antecipacao.setDescStatusAntecipacao(StatusAntecipacao.REPROVADO);
            antecipacao.setObservacao(motivo != null ? motivo : "Reprovada sem motivo informado");
            Antecipacao antecipacaoAtualizada = antecipacaoRepository.save(antecipacao);

            auditoriaService.registrarAuditoria(
                antecipacaoAtualizada,
                statusAnterior,
                antecipacaoAtualizada.getDescStatusAntecipacao(),
                "Reprovação realizada: " + (motivo != null ? motivo : "Sem motivo informado"),
                token,
                request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                obterUsuarioAtual()
            );

            enviarEmailConfirmacao(antecipacaoAtualizada, "Reprovada", motivo);

            
            // Altere o retorno para JSON
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Antecipação reprovada com sucesso");
            response.put("status", "success");
            response.put("id", antecipacaoAtualizada.getIdAntecipacao().toString());
            
            return ResponseEntity.ok(response);

        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }
 
    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/
    @GetMapping(value = "/antecipacao/popup", produces = "text/html;charset=UTF-8")
    public ResponseEntity<String> popup() throws IOException {
        ClassPathResource htmlFile = new ClassPathResource("templates/antecipacao-popup.html");
        String htmlContent = new String(htmlFile.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("text/html; charset=UTF-8"))
                .body(htmlContent);
    }
    
    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/
    @GetMapping("/antecipacao/resultado-antecipacao")
    public String resultadoAntecipacao(
            @RequestParam boolean success,
            @RequestParam String action,
            @RequestParam String message,
            @RequestParam String id,
            @RequestParam(required = false) String motivo,
            Model model) {
        
        model.addAttribute("success", success);
        model.addAttribute("action", action);
        model.addAttribute("message", message);
        model.addAttribute("id", id);
        model.addAttribute("motivo", motivo);
        return "antecipacao/resultado-antecipacao"; // Isso apontará para templates/antecipacao/resultado-antecipacao.html
    }
    
    
    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/
    private void enviarEmailConfirmacao(Antecipacao antecipacao, String status, String... motivo) {
        try {
            List<String> emails = new ArrayList<>();
            WorkflowInformativoDTO wi = workflowService.buscarPorId(1L);
            
            for (ContatoWorkflowDTO ck : wi.getContatoWorkflow()) {
                emails.add(ck.getEmail());
            }
            
            // Se for reprovação, passa o motivo (se existir)
            if (status.equalsIgnoreCase("Reprovada") && motivo != null && motivo.length > 0) {
                emailService.enviarEmailConfirmacaoAntecipacao(
                    antecipacao, 
                    status, 
                    motivo[0], // Pega o primeiro motivo (se existir)
                    emails
                );
            } else {
                // Para aprovação ou sem motivo
                emailService.enviarEmailConfirmacaoAntecipacao(
                    antecipacao, 
                    status, 
                    null, // Sem motivo
                    emails
                );
            }
        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail de confirmação: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /******************************************************************/
    /*                                                                */
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