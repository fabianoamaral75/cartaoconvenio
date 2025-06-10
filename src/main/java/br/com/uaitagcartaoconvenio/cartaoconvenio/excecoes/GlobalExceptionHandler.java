package br.com.uaitagcartaoconvenio.cartaoconvenio.excecoes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private String formatTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        return sdf.format(new Date());
    }
    
    @ExceptionHandler(ExceptionCustomizada.class)
    public ResponseEntity<ErrorResponse> handleExceptionCustomizada(
            ExceptionCustomizada ex, HttpServletRequest request) {
        
    	String errorMessage = ExceptionMessageExtractor.extractDetailedErrorMessage(ex);
    	
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            errorMessage,
            // ex.getMessage(),
            request.getRequestURI(),
            formatTimestamp()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<ErrorResponse> handleMismatchedInputException(
            MismatchedInputException ex, HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Tipo de dado inválido para o campo: " + ex.getPath().get(0).getFieldName(),
            request.getRequestURI(),
            formatTimestamp()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .findFirst()
            .orElse("Erro de validação");
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            errorMessage,
            request.getRequestURI(),
            formatTimestamp()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {
        
    	String errorMessage = ExceptionMessageExtractor.extractDetailedErrorMessage(ex);
    	
        ErrorResponse error = new ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            "Acesso negado: " + errorMessage, //ex.getMessage(),
            request.getRequestURI(),
            formatTimestamp()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
    	String errorMessage = ExceptionMessageExtractor.extractDetailedErrorMessage(ex);
    	
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro interno: " + errorMessage ,//ex.getMessage(),
            request.getRequestURI(),
            formatTimestamp()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    

    
    ///////////////////

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, 
            HttpServletRequest request) {
    	
    	String errorMessage = ExceptionMessageExtractor.extractDetailedErrorMessage(ex);
    	
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value()
            , "Unsupported Media Type",
           // ex.getMessage(),
            errorMessage,
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    
    @ExceptionHandler(InvalidDefinitionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDefinitionException(
            InvalidDefinitionException ex, HttpServletRequest request) {
        // deveria entrar aqui
        String errorMessage = ExceptionMessageExtractor.extractDetailedErrorMessage(ex);
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            errorMessage,
            request.getRequestURI(),
            formatTimestamp()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        String errorMessage = ExceptionMessageExtractor.extractDetailedErrorMessage(ex);
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            errorMessage,
            request.getRequestURI(),
            formatTimestamp()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    
    
}