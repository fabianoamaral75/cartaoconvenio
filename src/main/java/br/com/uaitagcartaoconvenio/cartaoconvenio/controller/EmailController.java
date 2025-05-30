package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.EmailService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/enviar-email")
    public ResponseEntity<?> enviarEmail(HttpServletRequest request ) throws ExceptionCustomizada, IOException{
        try {
            String status = emailService.enviarEmailSimples( new String[]{"fabiano.bolacha@gmail.com", "fabianoamaral.ti@gmail.com"},
												             "Teste envio de e-mail!",
												             "Teste para o envio de e-mail pelo Java!"
												            );
			if(!status.equals("OK")) {
				throw new ExceptionCustomizada("Não foi possivel o envio do e-mail!");
			}

            
            // return "E-mail enviado com sucesso!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail enviado com sucesso!");
        } catch (ExceptionCustomizada ex) {
	    	
	    	long timestamp = System.currentTimeMillis();

	    	// Criar formato desejado
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    	sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Fuso horário opcional

	    	// Converter
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
}

