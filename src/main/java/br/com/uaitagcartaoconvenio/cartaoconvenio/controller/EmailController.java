package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.util.EmailService;

@RestController
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/enviar-email")
    public String enviarEmail() {
        try {
            emailService.enviarEmailSimples(
                new String[]{"fabiano.bolacha@gmail.com", "fabianoamaral.ti@gmail.com"},
                "Teste envio de e-mail!",
                "Teste para o envio de e-mail pelo Java!"
            );
            return "E-mail enviado com sucesso!";
        } catch (Exception e) {
            return "Erro ao enviar e-mail: " + e.getMessage();
        }
    }
}

