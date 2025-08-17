package br.com.uaitagcartaoconvenio.cartaoconvenio.excecoes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ControllerAdvice
public class AntecipacaoExceptionHandler {

    @ExceptionHandler(AntecipacaoException.class)
    public ResponseEntity<ErrorResponse> handleAntecipacaoException(AntecipacaoException ex) {
        ErrorResponse error = new ErrorResponse(
            "ANTECIPACAO_ERROR", 
            ex.getMessage(),
            System.currentTimeMillis());
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private String code;
        private String message;
        private long timestamp;
    }
}
