package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Adiciona os parâmetros da URL ao modelo para tratamento de erros
        model.addAttribute("success", request.getParameter("success"));
        model.addAttribute("action", request.getParameter("action"));
        model.addAttribute("message", request.getParameter("message"));
        model.addAttribute("id", request.getParameter("id"));
        model.addAttribute("motivo", request.getParameter("motivo"));
        
        return "resultado-antecipacao"; // Usa o template resultado-antecipacao.html
    }

    @GetMapping("/antecipacao/resultado")
    public String resultadoAntecipacao(
        @RequestParam boolean success,
        @RequestParam String action,
        @RequestParam String message,
        @RequestParam(required = false) Long id,
        @RequestParam(required = false) String motivo,
        Model model) {
        
        model.addAttribute("success", success);
        model.addAttribute("action", action);
        model.addAttribute("message", message);
        model.addAttribute("id", id);
        model.addAttribute("motivo", motivo);
        
        return "resultado-antecipacao";
    }

    // Método para compatibilidade com a interface ErrorController
    public String getErrorPath() {
        return "/error";
    }
}