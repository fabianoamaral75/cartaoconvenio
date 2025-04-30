package br.com.uaitagcartaoconvenio.cartaoconvenio.excecoes;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private record ErrorResponse(
        String timestamp,
        int status,
        String error,
        String message,
        String path
    ) {}

    // Tratamento para validação de campos
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {
        
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("timestamp", LocalDateTime.now().toString());
        errors.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
/*
    // Tratamento para recursos não encontrados
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
        ResourceNotFoundException ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now().toString(),
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Tratamento genérico para outras exceções
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(
        Exception ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now().toString(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
 */   
}

/*
  Como Usar na Prática
  
     1 - Lance exceções específicas em seus serviços:
  
          public User getUserById(Long id) {
             return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
          } 
      
    2 - O GlobalExceptionHandler capturará automaticamente e retornará a resposta apropriada.  
    
    3 - Exemplo de resposta de erro para validação:
		{
		    "nome": "Nome não pode estar vazio",
		    "email": "Email deve ser válido"
		}
    
    4 - Exemplo de resposta para recurso não encontrado:
		{
		    "timestamp": "2023-05-20T14:30:45",
		    "status": 404,
		    "error": "Not Found",
		    "message": "Usuário não encontrado",
		    "path": "/api/users/999"
		}    
    
    
    
 */










